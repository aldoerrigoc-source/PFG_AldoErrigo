import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Cafeteria extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String bocatas = request.getParameter("Bocatas");
        String[] hamburguesa = request.getParameterValues("Hamburguesa");
		String almuerzo = request.getParameter("Almuerzo");
		
        out.println(CafeteriaHTML.cafeteria(bocatas, hamburguesa, almuerzo));
    }
}
