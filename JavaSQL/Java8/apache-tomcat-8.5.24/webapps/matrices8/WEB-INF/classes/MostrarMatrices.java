import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import Jama.*;

public class MostrarMatrices extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
                                          throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Mostrar matrices</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#FFFFFF\" text=\"#631818\">");
        out.println("<p align=\"center\"><font size=\"6\"><b>MATRICES</b></font></p>");

        for(int i=0; i<MatrixVector.v.size(); i++){
            out.println("<H2 align=\"center\">Matriz " + i + "</H2>");
            Matrix m = MatrixVector.v.elementAt(i);
            out.println("<p align=\"center\">");
            out.println(MatrixVector.printMatrix(m));
        }
        out.println("<a href=\"menu.html\">Ir al men&uacute;</a>");
        out.println("</body>");
        out.println("</html>");
            

    }

}
