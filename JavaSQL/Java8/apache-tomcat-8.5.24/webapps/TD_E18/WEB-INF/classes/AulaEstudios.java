import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class AulaEstudios extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
		String correo = request.getParameter("Correo");
		String dia = request.getParameter("Date");
		String horario = request.getParameter("Horario");
		String aula = request.getParameter("Aula");
		
        out.println(AulasHTML.aulas(nombre, apellido, correo, dia, horario, aula));
    }
}
