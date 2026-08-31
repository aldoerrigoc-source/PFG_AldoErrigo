import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Binomial extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String Binomial = request.getParameter("Binomial");
        String probstr = request.getParameter("p");
        String nstr = request.getParameter("n");
        String kstr = request.getParameter("k");
        double p=Double.parseDouble(probstr);
        int n=Integer.parseInt(nstr);
        int k=Integer.parseInt(kstr);
        out.println(BinomialHTML.masa(p,n,k));
        out.println(BinomialHTML.distr(p,n,k));
    }
}