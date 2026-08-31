import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class LinksServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String nString = request.getParameter("n");
        int n = Integer.parseInt(nString);
        System.out.println("n: " + n);
        String salida = Links.salida(n);
        out.println(salida);
    }
}