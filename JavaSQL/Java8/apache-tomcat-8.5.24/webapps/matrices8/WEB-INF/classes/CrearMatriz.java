import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import Jama.*;

public class CrearMatriz extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
                                          throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String strFilas = request.getParameter("filas");
        int filas = Integer.parseInt(strFilas);
        String strColumnas = request.getParameter("columnas");
        int columnas = Integer.parseInt(strColumnas);

        double[][] val = new double[filas][columnas];
        for (int i=0; i< filas; i++) {
            for (int j=0; j< columnas; j++) {
                String celda = request.getParameter("celda" + i + "-" + j);
                double value = Double.parseDouble(celda);
                val[i][j] = value;
            }
        }
        Matrix m = new Matrix (val, filas, columnas);
        MatrixVector.v.addElement(m);
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Mostrar matriz</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#FFFFFF\" text=\"#631818\">");
        out.println("<p align=\"center\"><font size=\"6\"><b>MATRIZ LEIDA</b></font></p>");

        out.println("<p align=\"center\">");
        out.println(MatrixVector.printMatrix(m));
        out.println("<a href=\"menu.html\">Ir al men&uacute;</a>");
        out.println("</body>");
        out.println("</html>");
            

    }

}
