import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String val = request.getParameter("rows");
        System.out.println("rows: " + val);
        int valInt = Integer.parseInt(val);
        String [] values = new String[valInt];
        for (int i=1; i<=valInt; i++) {
            values[i-1] = request.getParameter("n" + i);
        }
        String html = C2.printChecks(values);
        out.println(html);
    }
}