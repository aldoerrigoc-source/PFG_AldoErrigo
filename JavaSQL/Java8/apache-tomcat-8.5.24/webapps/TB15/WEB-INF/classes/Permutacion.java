import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Permutacion extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String permutacion = request.getParameter("Permutacion");
        String Nstr = request.getParameter("N");
        String numeros = request.getParameter("numeros").split(",");
		
        int N=Integer.parseInt(Nstr);
        int numeros=Integer.parseInt(numerosstr);
        
        if(Permutacion.equals("Normal")){
        out.println(PermutacionHTML.normal(N));

        }
        else{
        out.println(PermutacionHTML.repeticion(N,numeros));
        }
    }
}