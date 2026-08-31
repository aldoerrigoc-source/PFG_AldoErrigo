import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Combinacion extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String combinacion = request.getParameter("Combinacion");
        String Nstr = request.getParameter("N");
        String nstr = request.getParameter("n");
        int N=Integer.parseInt(Nstr);
        int n=Integer.parseInt(nstr);
        
        if(combinacion.equals("Normal")){
        out.println(CombinacionHTML.normal(N,n));

        }
        else{
        out.println(CombinacionHTML.repeticion(N,n));
        }
    }
}