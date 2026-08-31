import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el CSV generado por CUALQUIER evaluador (R1, R2, R3...), sin asumir un
 * numero fijo de columnas: los nombres de los criterios se leen del propio
 * header del CSV. Arma un JSON en batch y lo envia por POST al receptor.
 *
 * Uso: java ClienteNotas <ruta_csv> <nombre_test>
 * Ejemplo: java ClienteNotas resultado_r1.csv R1
 * Ejemplo: java ClienteNotas resultado_r3.csv R3
 */
public class ClienteNotas {

    private static final String URL_RECEPTOR = "http://localhost:8082/notasReceptor/notas";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java ClienteNotas <ruta_csv> <nombre_test>");
            return;
        }

        String rutaCsv = args[0];
        String nombreTest = args[1];

        try {
            DatosCsv datos = leerCsv(rutaCsv);
            String json = construirJson(nombreTest, datos);

            System.out.println("JSON a enviar:");
            System.out.println(json);

            enviarPost(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee el CSV saltando la linea 1 (timestamp). La linea 2 es el header
     * (se guarda para saber los nombres de los criterios). El resto son
     * las filas de datos.
     */
    private static DatosCsv leerCsv(String ruta) throws IOException {
        DatosCsv datos = new DatosCsv();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8));

        try {
            String linea;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;

                if (numLinea == 1) continue; // timestamp

                if (numLinea == 2) {
                    // Header: primera columna es "Alumno", el resto son los criterios
                    String[] columnas = linea.split(";");
                    for (int i = 1; i < columnas.length; i++) {
                        datos.criterios.add(columnas[i].trim());
                    }
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                datos.filas.add(linea.split(";"));
            }
        } finally {
            br.close();
        }

        return datos;
    }

    /**
     * Construye el JSON del batch. Usa listas paralelas (criterios / valores)
     * en vez de campos fijos, para soportar cualquier numero de columnas.
     */
    private static String construirJson(String nombreTest, DatosCsv datos) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append("  \"test\": \"").append(escapar(nombreTest)).append("\",\n");

        sb.append("  \"criterios\": [");
        for (int i = 0; i < datos.criterios.size(); i++) {
            sb.append("\"").append(escapar(datos.criterios.get(i))).append("\"");
            if (i < datos.criterios.size() - 1) sb.append(", ");
        }
        sb.append("],\n");

        sb.append("  \"alumnos\": [\n");

        for (int i = 0; i < datos.filas.size(); i++) {
            String[] col = datos.filas.get(i);
            String alumno = extraerNombreAlumno(col[0]);

            sb.append("    {\n");
            sb.append("      \"alumno\": \"").append(escapar(alumno)).append("\",\n");
            sb.append("      \"valores\": [");

            // col[0] es el alumno; el resto son los valores, en el mismo orden que "criterios"
            for (int j = 1; j < col.length; j++) {
                sb.append(parseDecimal(col[j]));
                if (j < col.length - 1) sb.append(", ");
            }

            sb.append("]\n");
            sb.append("    }");
            if (i < datos.filas.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}\n");

        return sb.toString();
    }

    private static String extraerNombreAlumno(String urlAlumno) {
        String limpio = urlAlumno;
        if (limpio.endsWith("/")) limpio = limpio.substring(0, limpio.length() - 1);
        int idx = limpio.lastIndexOf('/');
        return idx >= 0 ? limpio.substring(idx + 1) : limpio;
    }

    /** Convierte "1,00" (Locale.GERMANY) a "1.00". Si la celda no es numerica (ej. mensaje de error), la deja en 0. */
    private static String parseDecimal(String valor) {
        String v = valor.trim().replace(",", ".");
        if (v.matches("-?\\d+(\\.\\d+)?")) {
            return v;
        }
        return "0";
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void enviarPost(String json) throws IOException {
        URL url = new URL(URL_RECEPTOR);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        byte[] bytesJson = json.getBytes(StandardCharsets.UTF_8);

        OutputStream os = conn.getOutputStream();
        try {
            os.write(bytesJson);
            os.flush();
        } finally {
            os.close();
        }

        int codigo = conn.getResponseCode();
        System.out.println("Respuesta del receptor (" + codigo + "):");

        InputStream is = (codigo >= 200 && codigo < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            try {
                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                }
            } finally {
                br.close();
            }
        }

        conn.disconnect();
    }

    private static class DatosCsv {
        List<String> criterios = new ArrayList<String>();
        List<String[]> filas = new ArrayList<String[]>();
    }
}
