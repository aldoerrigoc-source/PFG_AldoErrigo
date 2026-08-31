import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Celdas extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
                                          throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String strFilas = request.getParameter("filas");
    int intFilas = Integer.parseInt(strFilas);
    String strColumnas = request.getParameter("columnas");
    int intColumnas = Integer.parseInt(strColumnas);
    
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Leer matriz</title>");
    out.println("</head>");
    out.println("<body bgcolor=\"#FFFFFF\" text=\"#631818\">");
    out.println("<p align=\"center\"><font size=\"6\"><b>VALORES DE LA MATRIZ</b></font></p>");
    out.println("<form method=\"GET\" action=\"CrearMatriz\"><BR>");
    out.println("<input type=\"hidden\" name=\"filas\" value=\"" + strFilas + "\">");
    out.println("<input type=\"hidden\" name=\"columnas\" value=\"" + strColumnas + "\">");
    out.println("<p align=\"center\">");
    out.println("<TABLE BORDER=\"0\">");
    for (int i=0; i< intFilas; i++) {
        out.println("<TR>");
        for (int j=0; j< intColumnas; j++) {
            out.println("<TD><input type=\"text\" name=\"celda" + i + "-" + j + "\"" +
            " value=\"0\" size=\"3\"></TD>");
        }
        out.println("</TR>");
    }
    out.println("</TABLE>  <p align=\"center\">");
    out.println("<input type=\"submit\" value=\"Enviar\">&nbsp;&nbsp;&nbsp;");
    out.println("<input type=\"reset\" value=\"Borrar\"></p>");
    out.println("</form>");
    out.println("</body>");
    out.println("</html>");
    }

}
