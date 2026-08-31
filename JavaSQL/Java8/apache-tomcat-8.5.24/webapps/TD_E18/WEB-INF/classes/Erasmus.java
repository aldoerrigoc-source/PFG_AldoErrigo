import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Erasmus extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
		String correo = request.getParameter("Correo");
		String semestre = request.getParameter("Semestre");
        String pais = request.getParameter("Pais");
		
        out.println(ErasmusHTML.erasmus(nombre, apellido, correo, semestre, pais));
    }
}
