import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String val = request.getParameter("machines");
        System.out.println("machines: " + val);
        int valInt = Integer.parseInt(val);
        String [] values = new String[valInt];
        for (int i=0; i<valInt; i++) {
            values[i] = request.getParameter("machine_" + (char)(65 + i));
        }
        String html = C2.printMachines(values);
        out.println(html);
    }
}