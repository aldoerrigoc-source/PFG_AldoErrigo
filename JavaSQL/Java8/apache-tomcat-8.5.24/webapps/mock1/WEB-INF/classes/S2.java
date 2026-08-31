import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String valoraciones = request.getParameter("valoraciones");
        System.out.println("valoraciones: " + valoraciones);
        String salida = C2.salidaHTML(valoraciones);
        out.println(salida);
    }
}