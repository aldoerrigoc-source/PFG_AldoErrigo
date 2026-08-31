import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Login extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String nombre = request.getParameter("nombre");
        String contrasena = request.getParameter("contrasena");
        System.out.println("nombre: " + nombre);
        System.out.println("contrasena: " + contrasena);
        boolean res = LoginHTML.loginCorrecto(nombre, contrasena);
        out.println(LoginHTML.salidaHTML(res));
    }
}