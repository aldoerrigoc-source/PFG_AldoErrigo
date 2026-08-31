import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet receptor (prueba de concepto de integracion tipo "Codex").
 *
 * POST /notas  -> recibe un JSON en batch (test + criterios + alumnos con valores) y lo guarda en memoria.
 * GET  /notas  -> muestra, por cada test recibido, una tabla con todos sus criterios como columnas.
 *
 * Persistencia: en memoria. Se pierde al reiniciar Tomcat.
 */
public class ReceptorServlet extends HttpServlet {

    // Un batch por cada POST recibido. Si el mismo test se envia varias veces, se listan por separado.
    private static final List<BatchRecibido> historial = new ArrayList<BatchRecibido>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String body = leerBody(request);
        BatchRecibido batch = parsearJson(body);
        guardarBatch(batch);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println("{\"status\":\"ok\",\"alumnosRecibidos\":" + batch.alumnos.size() + "}");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Notas recibidas</title>");
        out.println("<style>");
        out.println("body{font-family:sans-serif;}");
        out.println("table{border-collapse:collapse;margin-bottom:24px;}");
        out.println("td,th{border:1px solid #ccc;padding:6px 10px;text-align:center;font-size:13px;}");
        out.println("th{background:#eee;}");
        out.println("h3{margin-top:32px;}");
        out.println("</style></head><body>");
        out.println("<h2>Notas recibidas (en memoria)</h2>");

        if (historial.isEmpty()) {
            out.println("<p>Todavia no se ha recibido ningun batch.</p>");
        } else {
            for (BatchRecibido batch : historial) {
                out.println("<h3>Test: " + escaparHtml(batch.test) + "</h3>");
                out.println("<table>");

                out.print("<tr><th>Alumno</th>");
                for (String criterio : batch.criterios) {
                    out.print("<th>" + escaparHtml(criterio) + "</th>");
                }
                out.println("</tr>");

                List<Alumno> alumnosOrdenados = new ArrayList<Alumno>(batch.alumnos);
                Collections.sort(alumnosOrdenados, new Comparator<Alumno>() {
                    public int compare(Alumno a1, Alumno a2) {
                        return a1.nombre.compareToIgnoreCase(a2.nombre);
                    }
                });

                for (Alumno a : alumnosOrdenados) {
                    out.print("<tr><td>" + escaparHtml(a.nombre) + "</td>");
                    for (String valor : a.valores) {
                        out.print("<td>" + escaparHtml(valor) + "</td>");
                    }
                    out.println("</tr>");
                }

                out.println("</table>");
            }
        }

        out.println("</body></html>");
    }

    private synchronized void guardarBatch(BatchRecibido batch) {
        historial.add(batch);
    }

    private String leerBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = reader.readLine()) != null) {
            sb.append(linea).append("\n");
        }
        return sb.toString();
    }

    /**
     * Parser minimo hecho a mano (sin libreria JSON), a medida de la estructura
     * fija que genera ClienteNotas.java (test / criterios / alumnos con valores).
     */
    private BatchRecibido parsearJson(String json) {
        BatchRecibido batch = new BatchRecibido();

        Matcher mTest = Pattern.compile("\"test\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        if (mTest.find()) batch.test = mTest.group(1);

        Matcher mCriterios = Pattern.compile("\"criterios\"\\s*:\\s*\\[([^\\]]*)\\]").matcher(json);
        if (mCriterios.find()) {
            batch.criterios = extraerListaStrings(mCriterios.group(1));
        }

        Matcher mAlumno = Pattern.compile(
                "\\{\\s*\"alumno\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"valores\"\\s*:\\s*\\[([^\\]]*)\\]\\s*\\}"
        ).matcher(json);

        while (mAlumno.find()) {
            Alumno a = new Alumno();
            a.nombre = mAlumno.group(1);
            a.valores = extraerListaNumeros(mAlumno.group(2));
            batch.alumnos.add(a);
        }

        return batch;
    }

    private List<String> extraerListaStrings(String contenido) {
        List<String> resultado = new ArrayList<String>();
        Matcher m = Pattern.compile("\"([^\"]*)\"").matcher(contenido);
        while (m.find()) resultado.add(m.group(1));
        return resultado;
    }

    private List<String> extraerListaNumeros(String contenido) {
        List<String> resultado = new ArrayList<String>();
        for (String parte : contenido.split(",")) {
            String limpio = parte.trim();
            if (!limpio.isEmpty()) resultado.add(limpio);
        }
        return resultado;
    }

    private String escaparHtml(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static class BatchRecibido {
        String test = "";
        List<String> criterios = new ArrayList<String>();
        List<Alumno> alumnos = new ArrayList<Alumno>();
    }

    private static class Alumno {
        String nombre = "";
        List<String> valores = new ArrayList<String>();
    }
}
