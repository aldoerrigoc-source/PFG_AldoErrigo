import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class S3 extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        // Obtener los parámetros enviados por S2
        String[] valoraciones = request.getParameterValues("v_1");

        // Verificar si se recibieron valoraciones
        if (valoraciones != null) {
            int acumulado = 0;

            // Sumar las valoraciones
            for (String valoracion : valoraciones) {
                acumulado += Integer.parseInt(valoracion);
            }

            // Calcular el promedio
            double promedio = (double) acumulado / valoraciones.length;

            // Mostrar el resultado
            out.println("<html>");
            out.println("<head><title>Resultado de valoraciones</title></head>");
            out.println("<body>");
            out.println("<h1>Resultado de valoraciones</h1>");
            out.println("<p>El acumulado de las valoraciones es: " + acumulado + "</p>");
            out.println("<p>El promedio de las valoraciones es: " + promedio + "</p>");
            out.println("</body>");
            out.println("</html>");
        } else {
            out.println("<html>");
            out.println("<head><title>No se recibieron valoraciones</title></head>");
            out.println("<body>");
            out.println("<h1>No se recibieron valoraciones</h1>");
            out.println("<p>No se han enviado valoraciones desde S2.</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
