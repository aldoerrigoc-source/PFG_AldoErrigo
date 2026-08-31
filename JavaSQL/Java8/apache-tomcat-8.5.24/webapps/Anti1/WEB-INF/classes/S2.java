import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String texto = request.getParameter("texto");
        String [] palabras = texto.split(" " );
        System.out.println("palabras: " + palabras.length);
        String salida = C2.salidaHTML(palabras);
        out.println(salida);
    }
}