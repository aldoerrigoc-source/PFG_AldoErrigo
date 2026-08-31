import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String dimensiones= request.getParameter("dimensiones");
        String escala= request.getParameter("escala");
        System.out.println("dimensiones: " + dimensiones); 
        System.out.println("escala: " + escala);
        String salida = C2.salidaHTML(dimensiones, escala);
        out.println(salida);
    }
}