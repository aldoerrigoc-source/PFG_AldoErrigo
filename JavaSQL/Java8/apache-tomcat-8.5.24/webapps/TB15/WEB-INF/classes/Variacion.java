import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Variacion extends HttpServlet{
     public void doGet(HttpServletRequest request, HttpServletResponse response)
	 throws ServletException, IOException{
	 
	 response.setContentType("text/htlm");
	 PrintWriter out= response.getWriter();
	 String variacion = request.getParameter("Variacion");
	 String Nstr = request.getParameter("N");
     String nstr = request.getParameter("n");
     int N=Integer.parseInt(Nstr);
     int n=Integer.parseInt(nstr);
        
        if(variacion.equals("Normal")){
        out.println(VariacionHTML.normal(N,n));
        }
        else{
       out.println(VariacionHTML.repeticion(N,n));
        }
    }
}





