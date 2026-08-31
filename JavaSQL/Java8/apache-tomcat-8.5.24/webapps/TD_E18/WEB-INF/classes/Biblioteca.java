import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Biblioteca extends HttpServlet {
    public static final long serialVersionUID = 1L;
    String fileName;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("Iniciando Biblioteca...");
        fileName = config.getServletContext().getRealPath("lista.txt");
        System.out.println("File: " + fileName);
    } 
    public void destroy() {
        System.out.println("No hay nada que hacer...");
    } 
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        devolverPaginaHTML(response);
    }   
    public void devolverPaginaHTML(HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out=response.getWriter();
        } catch (IOException io) {
            System.out.println("Se ha producido una excepcion");        
        }
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Biblioteca</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<B><h1>Biblioteca</h1></B>");
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String line = null;
        String strout = "";
        strout += "<TABLE border=1>";
        strout += "<TR>";
        strout += "<TH>";
        strout += "Titulo";
        strout += "</TH>";
        strout += "<TH>";
        strout += "Autor";
        strout += "</TH>";
        strout += "<TH>";
        strout += "Fecha";
        strout += "</TH>";
		strout += "<TH>";
        strout += "Genero";
        strout += "</TH>";
		strout += "<TH>";
        strout += "Idioma";
        strout += "</TH>";
        strout += "</TR>";
		strout += "<tr>";
		strout +="<td>El principito</td>";
		strout +="<td>Antoine de Saint-Exup&eacute;ry</td>";
		strout +="<td>6 de abril de 1943</td>";
		strout +="<td>Literatura infantil</td>";
		strout +="<td>Franc&eacute;s</td>";
		strout += "</tr>";
        while (scanner.hasNext()) {
                line = scanner.nextLine();
                Scanner lineSc = new Scanner(line);
                lineSc.useDelimiter("\t");
                try {
                        String titulo = lineSc.next();
                        String autor = lineSc.next();
                        String fecha = lineSc.next();
						String genero = lineSc.next();
						String idioma = lineSc.next();
                        strout += "<TR>";
                        strout += "<TD>";
                        strout += titulo;
                        strout += "</TD>";
                        strout += "<TD>";
                        strout += autor;
                        strout += "</TD>";
                        strout += "<TD>";
                        strout += fecha;
                        strout += "</TD>";
						strout += "<TD>";
                        strout += genero;
                        strout += "</TD>";
						strout += "<TD>";
                        strout += idioma;
                        strout += "</TD>";
                        strout += "</TR>";
                } catch (NoSuchElementException ex) {
                        System.out.println("Error en Biblioteca " + ex);
                }

        }
        strout += "</TABLE>";

        out.println(strout);
        out.println("<BR><a href=\"index.html\">Volver a la p&aacute;gina principal</a>");
        out.println("</BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
    public String getServletInfo() {
        return "Este servlet lee los datos de un formulario y los muestra en pantalla";
    }
}
