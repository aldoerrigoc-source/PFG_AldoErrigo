import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S1 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String rows= request.getParameter("rows");
        String cols= request.getParameter("cols");
        String salida = C1.salidaHTML(rows, cols);
        out.println(salida);
    }
}