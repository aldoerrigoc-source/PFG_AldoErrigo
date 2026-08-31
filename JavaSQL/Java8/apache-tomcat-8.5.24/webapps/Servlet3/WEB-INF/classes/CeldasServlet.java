import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class CeldasServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String filas = request.getParameter("filas");
        String columnas = request.getParameter("columnas");
        System.out.println("filas: " + filas);
        System.out.println("columnas: " + columnas);
        String salida = CeldasHTML.salidaHTML(filas, columnas);
        out.println(salida);
    }
}