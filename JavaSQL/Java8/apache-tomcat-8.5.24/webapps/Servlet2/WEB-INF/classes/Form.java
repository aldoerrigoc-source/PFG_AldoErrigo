import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Form extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String opcionesStr = request.getParameter("opciones");
        int opciones = Integer.parseInt(opcionesStr);
        String salida = FormHTML.salidaHTML(opciones);
        out.println(salida);
    }
}