import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * StockEdit
 *
 * Lets the user edit a single product's stock quantity.
 * GET  ?id=X  -> shows a form with the product's current data (cantidad editable).
 * POST id+cantidad -> updates Productos.cantidad via ProductoData and redirects to StockList.
 *
 * NOTE: This only updates Productos.cantidad directly. No Movimientos
 * record is created (that will be a separate future module).
 */
public class StockEdit extends HttpServlet {

    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        Producto producto = null;
        try {
            producto = ProductoData.obtenerPorId(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>ERP - Edit Product | Developed by Aldo</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("    <header class=\"app-header\">");
        out.println("        <h1>Edit Product</h1>");
        out.println("        <div class=\"credit\">Developed by Aldo</div>");
        out.println("    </header>");

        out.println("    <nav class=\"app-nav\">");
        out.println("        <a href=\"index.html\">Home</a>");
        out.println("        <a href=\"StockList\">Stock List</a>");
        out.println("    </nav>");

        out.println("    <main class=\"app-content\">");

        if (producto == null) {
            out.println("        <p>Product not found.</p>");
        } else {
            out.println("        <h2>" + producto.nombre + "</h2>");
            out.println("        <form method=\"post\" action=\"StockEdit\">");
            out.println("            <input type=\"hidden\" name=\"id\" value=\"" + producto.id + "\">");
            out.println("            <p><strong>Category:</strong> " + producto.categoria + "</p>");
            out.println("            <p><strong>Unit Cost:</strong> $" + String.format("%.2f", producto.coste) + "</p>");
            out.println("            <p>");
            out.println("                <label for=\"cantidad\"><strong>Quantity:</strong></label><br>");
            out.println("                <input type=\"number\" id=\"cantidad\" name=\"cantidad\" value=\"" + producto.cantidad + "\" min=\"0\" required>");
            out.println("            </p>");
            out.println("            <p>");
            out.println("                <button type=\"submit\">Save</button>");
            out.println("                <a href=\"StockList\">Cancel</a>");
            out.println("            </p>");
            out.println("        </form>");
        }

        out.println("    </main>");

        out.println("    <footer class=\"app-footer\">");
        out.println("        &copy; 2026 ERP System - Developed by Aldo");
        out.println("    </footer>");

        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String cantidadStr = request.getParameter("cantidad");

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            ProductoData.actualizarCantidad(connection, id, cantidad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("StockList");
    }
}
