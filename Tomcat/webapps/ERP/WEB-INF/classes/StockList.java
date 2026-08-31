import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * StockList
 *
 * Displays the current product inventory: name, category, quantity,
 * unit cost and an image for each product, read from the Productos
 * table via ProductoData. Each row includes an Edit link that opens
 * StockEdit for that product's id.
 */
public class StockList extends HttpServlet {

    Connection connection;

    /** Cycled placeholder images (DB imagen_url is ignored for now). */
    private static final String[] PLACEHOLDER_IMAGES = {
        "images/placeholder-1.svg",
        "images/placeholder-2.svg",
        "images/placeholder-3.svg"
    };

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Vector<Producto> productos;
        try {
            productos = ProductoData.listarTodos(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            productos = new Vector<Producto>();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>ERP - Stock List | Developed by Aldo</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("    <header class=\"app-header\">");
        out.println("        <h1>Stock List</h1>");
        out.println("        <div class=\"credit\">Developed by Aldo</div>");
        out.println("    </header>");

        out.println("    <nav class=\"app-nav\">");
        out.println("        <a href=\"index.html\">Home</a>");
        out.println("    </nav>");

        out.println("    <main class=\"app-content\">");
        out.println("        <h2>Current Inventory</h2>");
        out.println("        <table class=\"data-table\">");
        out.println("            <tr>");
        out.println("                <th>Image</th>");
        out.println("                <th>Product</th>");
        out.println("                <th>Category</th>");
        out.println("                <th>Quantity</th>");
        out.println("                <th>Unit Cost</th>");
        out.println("                <th>Actions</th>");
        out.println("            </tr>");

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            String imagenUrl = PLACEHOLDER_IMAGES[i % PLACEHOLDER_IMAGES.length];
            out.println("            <tr>");
            out.println("                <td><img class=\"product-img\" src=\"" + imagenUrl + "\" alt=\"" + p.nombre + "\"></td>");
            out.println("                <td>" + p.nombre + "</td>");
            out.println("                <td>" + p.categoria + "</td>");
            out.println("                <td>" + p.cantidad + "</td>");
            out.println("                <td>$" + String.format("%.2f", p.coste) + "</td>");
            out.println("                <td><a href=\"StockEdit?id=" + p.id + "\">Edit</a></td>");
            out.println("            </tr>");
        }

        out.println("        </table>");
        out.println("    </main>");

        out.println("    <footer class=\"app-footer\">");
        out.println("        &copy; 2026 ERP System - Developed by Aldo");
        out.println("    </footer>");

        out.println("</body>");
        out.println("</html>");
    }
}
