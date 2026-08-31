import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Parking extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String parking = request.getParameter("Parking");
        String nombre = request.getParameter("Nombre y Apellidos");
		String matricula = request.getParameter("Matricula");
		String dia = request.getParameter("Dia");
		
        out.println(ParkingHTML.parking(parking, nombre, matricula,dia));
    }
}
