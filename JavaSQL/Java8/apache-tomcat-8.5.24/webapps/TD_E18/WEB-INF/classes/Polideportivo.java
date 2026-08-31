import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Polideportivo extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
		String correo = request.getParameter("Correo");
		String dia = request.getParameter("Date");
		String horario = request.getParameter("Horario");
		
        out.println(PolideportivoHTML.polideportivo(nombre, apellido, correo,dia, horario));
    }
}