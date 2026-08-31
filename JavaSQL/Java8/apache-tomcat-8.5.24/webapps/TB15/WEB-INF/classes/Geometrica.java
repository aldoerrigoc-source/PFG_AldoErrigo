import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Geometrica extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String Geometrica = request.getParameter("Geometrica");
        String probstr = request.getParameter("p");
        
        String kstr = request.getParameter("k");
        double p=Double.parseDouble(probstr);
        
        int k=Integer.parseInt(kstr);
        out.println(GeometricaHTML.masa(p,k));
        out.println(GeometricaHTML.distr(p,k));
    }
}