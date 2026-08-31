import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String casillasStr = request.getParameter("hr");
        int hr= Integer.parseInt(casillasStr);
        String salida = HR.salidaHR(hr);
        out.println(salida);
    }
}