import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class ItemList extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Vector<ProductoMockup> productos = ProductoMockup.getAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <title>Developed by NOMBRE APELLIDO</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <h2>Developed by NOMBRE APELLIDO</h2>");
        out.println("  <table border='1'>");

        // BUG: debe usar <th> no <td>
        out.println("    <tr>");
        out.println("      <td>ID</td>");
        out.println("      <td>Nombre</td>");
        out.println("      <td>Categoria</td>");
        out.println("      <td>Precio</td>");
        // TODO: añadir <th>Stock</th>
        // TODO: añadir <th>Imagen</th>
        out.println("    </tr>");

        for (ProductoMockup p : productos) {
            out.println("    <tr>");
            out.println("      <td>" + p.getId() + "</td>");
            out.println("      <td>" + p.getNombre() + "</td>");
            out.println("      <td>" + p.getCategoria() + "</td>");
            out.println("      <td>" + p.getPrecio() + "</td>");
            // TODO: añadir <td> con p.getStock()</td>
            // TODO: añadir <td><img src="img/producto_ID.png"></td>
            out.println("    </tr>");
        }

        out.println("  </table>");
        out.println("</body>");
        out.println("</html>");
    }
}
