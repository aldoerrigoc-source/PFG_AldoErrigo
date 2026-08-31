import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class registro extends HttpServlet {
    public static final long serialVersionUID = 2L;
    String fileName;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("Iniciando registro...");
        fileName = config.getServletContext().getRealPath("lista.txt");
    } 
    public void destroy() {
        System.out.println("No hay nada que hacer...");
    }
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strTitulo = request.getParameter("Titulo");
        String strAutor = request.getParameter("Autor");
        String strFecha = request.getParameter("Fecha de publicacion");
		String strGenero = request.getParameter("Genero");
		String strIdioma = request.getParameter("Idioma");
                                 
        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter toFile = new PrintWriter(fileWriter);
        toFile.println(strTitulo + "\t" + strAutor + "\t" + strFecha+ "\t" + strGenero+ "\t" + strIdioma);
        fileWriter.close();
        devolverPaginaHTML(response, strTitulo, strAutor, strFecha,strGenero, strIdioma );
    }
        
    public void devolverPaginaHTML(HttpServletResponse response,
        String titulo, String autor, String fecha, String genero, String idioma ) {
        response.setContentType("text/html");

        PrintWriter out = null;
        try {
            out=response.getWriter();
        } catch (IOException io) {
            System.out.println("Se ha producido una excepcion");        
        }
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Valores recogidos en el formulario</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<B><FONT size=+2>Valores recogidos del formulario: </FONT></B>");
        out.println("<P><FONT size=+1> <B>T&iacute;tulo: </B><I>" + titulo + "</I></FONT>");
		out.println("<P><FONT size=+1><B>Autor: </B>" + autor + "</FONT>");
        out.println("<P><FONT size=+1><B>Fecha: </B>" + fecha + "</FONT>");
		out.println("<BR><FONT size=+1><B>Genero: </B>" + genero + "</FONT>");
		out.println("<P><FONT size=+1><B>Idioma: </B>" + idioma + "</FONT>");
        out.println("</BODY>");
        out.println("<BR><a href=\"index.html\">Volver a la p&aacute;gina principal</a>");

        out.println("</HTML>");
        out.flush();
        out.close();
    }
    public String getServletInfo() {
        return "Este servlet lee los datos de un formulario y los muestra en pantalla";
    }
}
