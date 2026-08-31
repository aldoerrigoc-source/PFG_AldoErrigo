import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class S2 extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String selects = request.getParameter("selects");
        System.out.println("selects: " + selects);
        int selectsInt = Integer.parseInt(selects);
        String [] values = new String[selectsInt];
        for (int i=0; i<selectsInt; i++) {
            values[i] = request.getParameter("Select_" + i);
        }
        String html = C2.printSelects(values);
        out.println(html);
    }
}