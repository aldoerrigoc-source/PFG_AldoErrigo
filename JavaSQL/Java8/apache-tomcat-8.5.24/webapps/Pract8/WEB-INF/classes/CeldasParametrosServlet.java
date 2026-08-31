import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class CeldasParametrosServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String filas = request.getParameter("filas");
        String diagonal= request.getParameter("diagonal");
        System.out.println("filas: " + filas);
        System.out.println("diagonal: " + diagonal);
        String salida = CeldasParametrosHTML.salidaHTML(filas, diagonal);
        out.println(salida);
    }
}