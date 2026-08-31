import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * InventarioFisicoNuevo
 *
 * Lets the user perform a physical inventory count. Shows every product
 * with its current system quantity and a blank input where the user types
 * the quantity actually counted. On save, for every product that received
 * a value, the difference (cantidad_fisica - cantidad_sistema) is computed
 * and a row is stored in InventarioFisico via InventarioFisicoData.insertar.
 *
 * Products left blank are skipped (not counted this time). Products.cantidad
 * is never modified by this screen; it only records the comparison.
 * A summary of the saved counts (and any invalid entries) is shown after saving.
 */
public class InventarioFisicoNuevo extends HttpServlet {

    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        renderForm(response, null);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Vector<Producto> productos;
        try {
            productos = ProductoData.listarTodos(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            renderForm(response, "A database error occurred while loading products.");
            return;
        }

        Vector<InventarioFisico> guardados = new Vector<InventarioFisico>();
        Vector<String> invalidas = new Vector<String>();

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            String fisicaStr = request.getParameter("cantidad_" + p.id);

            if (fisicaStr == null || fisicaStr.trim().length() == 0) {
                continue;
            }

            int cantidadFisica;
            try {
                cantidadFisica = Integer.parseInt(fisicaStr.trim());
            } catch (NumberFormatException e) {
                invalidas.add(p.nombre + " (value \"" + fisicaStr + "\" is not a valid whole number)");
                continue;
            }

            if (cantidadFisica < 0) {
                invalidas.add(p.nombre + " (counted quantity cannot be negative)");
                continue;
            }

            int discrepancia = cantidadFisica - p.cantidad;

            try {
                InventarioFisicoData.insertar(connection, p.id, p.cantidad, cantidadFisica, discrepancia);
            } catch (SQLException e) {
                e.printStackTrace();
                invalidas.add(p.nombre + " (database error while saving)");
                continue;
            }

            guardados.add(new InventarioFisico(null, p.id, p.cantidad, cantidadFisica, discrepancia, null));
        }

        renderSummary(response, productos, guardados, invalidas);
    }

    /**
     * Renders the count form. If error is non-null, an error banner is shown.
     */
    private void renderForm(HttpServletResponse response, String error) throws IOException {
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
        out.println("    <title>ERP - Physical Inventory Count | Developed by Aldo</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("    <header class=\"app-header\">");
        out.println("        <h1>Physical Inventory Count</h1>");
        out.println("        <div class=\"credit\">Developed by Aldo</div>");
        out.println("    </header>");

        out.println("    <nav class=\"app-nav\">");
        out.println("        <a href=\"index.html\">Home</a>");
        out.println("        <a href=\"StockList\">Stock List</a>");
        out.println("    </nav>");

        out.println("    <main class=\"app-content\">");

        if (error != null) {
            out.println("        <p class=\"error-message\">" + error + "</p>");
        }

        out.println("        <p>Enter the quantity you physically counted for each product. Leave a field blank to skip a product.</p>");

        out.println("        <form method=\"post\" action=\"InventarioFisicoNuevo\">");
        out.println("        <table class=\"data-table\">");
        out.println("            <tr>");
        out.println("                <th>Product</th>");
        out.println("                <th>Category</th>");
        out.println("                <th>System Quantity</th>");
        out.println("                <th>Counted Quantity</th>");
        out.println("            </tr>");

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            out.println("            <tr>");
            out.println("                <td>" + p.nombre + "</td>");
            out.println("                <td>" + p.categoria + "</td>");
            out.println("                <td>" + p.cantidad + "</td>");
            out.println("                <td><input type=\"number\" name=\"cantidad_" + p.id + "\" min=\"0\"></td>");
            out.println("            </tr>");
        }

        out.println("        </table>");
        out.println("        <p>");
        out.println("            <button type=\"submit\">Save Count</button>");
        out.println("            <a href=\"StockList\">Cancel</a>");
        out.println("        </p>");
        out.println("        </form>");

        out.println("    </main>");

        out.println("    <footer class=\"app-footer\">");
        out.println("        &copy; 2026 ERP System - Developed by Aldo");
        out.println("    </footer>");

        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Renders the summary page after saving: one row per counted product with
     * its system quantity, physical quantity and discrepancy, plus a list of
     * any entries that could not be saved.
     */
    private void renderSummary(HttpServletResponse response, Vector<Producto> productos,
                                Vector<InventarioFisico> guardados, Vector<String> invalidas)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>ERP - Inventory Count Results | Developed by Aldo</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("    <header class=\"app-header\">");
        out.println("        <h1>Inventory Count Results</h1>");
        out.println("        <div class=\"credit\">Developed by Aldo</div>");
        out.println("    </header>");

        out.println("    <nav class=\"app-nav\">");
        out.println("        <a href=\"index.html\">Home</a>");
        out.println("        <a href=\"StockList\">Stock List</a>");
        out.println("        <a href=\"InventarioFisicoNuevo\">New Count</a>");
        out.println("    </nav>");

        out.println("    <main class=\"app-content\">");

        if (guardados.isEmpty()) {
            out.println("        <p>No products were counted.</p>");
        } else {
            out.println("        <h2>Counted Products</h2>");
            out.println("        <table class=\"data-table\">");
            out.println("            <tr>");
            out.println("                <th>Product</th>");
            out.println("                <th>System Quantity</th>");
            out.println("                <th>Physical Quantity</th>");
            out.println("                <th>Discrepancy</th>");
            out.println("            </tr>");

            for (int i = 0; i < guardados.size(); i++) {
                InventarioFisico inv = guardados.get(i);
                String nombre = buscarNombre(productos, inv.productoId);
                String rowClass = inv.discrepancia != 0 ? " class=\"error-message\"" : "";
                out.println("            <tr" + rowClass + ">");
                out.println("                <td>" + nombre + "</td>");
                out.println("                <td>" + inv.cantidadSistema + "</td>");
                out.println("                <td>" + inv.cantidadFisica + "</td>");
                out.println("                <td>" + (inv.discrepancia > 0 ? "+" : "") + inv.discrepancia + "</td>");
                out.println("            </tr>");
            }

            out.println("        </table>");
        }

        if (!invalidas.isEmpty()) {
            out.println("        <h2>Skipped (Invalid Entries)</h2>");
            out.println("        <ul>");
            for (int i = 0; i < invalidas.size(); i++) {
                out.println("            <li class=\"error-message\">" + invalidas.get(i) + "</li>");
            }
            out.println("        </ul>");
        }

        out.println("    </main>");

        out.println("    <footer class=\"app-footer\">");
        out.println("        &copy; 2026 ERP System - Developed by Aldo");
        out.println("    </footer>");

        out.println("</body>");
        out.println("</html>");
    }

    private String buscarNombre(Vector<Producto> productos, String productoId) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.id.equals(productoId)) {
                return p.nombre;
            }
        }
        return "(unknown product)";
    }
}
