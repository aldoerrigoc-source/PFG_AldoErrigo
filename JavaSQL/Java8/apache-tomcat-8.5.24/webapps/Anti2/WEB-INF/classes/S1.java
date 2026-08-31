import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S1 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String monedas = request.getParameter("monedas");
        String simulaciones = request.getParameter("simulaciones");
        String html = C1.createForm(monedas, simulaciones);
        out.println(html);
    }
    public static int resultado(int monedas) {
        int res = 0;
        for (int i = 0; i < monedas; i++) {
            if (Math.random() > .5) {
                res++;
            }
        }
        return res;
    }
}