import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Poisson extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String poisson = request.getParameter("Poisson");
        String mediastr = request.getParameter("media");
        String kstr = request.getParameter("k");
        double media=Double.parseDouble(mediastr);
        double k=Double.parseDouble(kstr);
        out.println(PoissonHTML.masa(media,k));
        out.println("<p></p>");
        out.println(PoissonHTML.distr(media,k));
    }
}