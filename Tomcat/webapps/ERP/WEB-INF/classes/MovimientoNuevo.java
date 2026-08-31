import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * MovimientoNuevo
 *
 * Lets the user record a new stock movement (entrada/salida) for a product.
 * GET  -> shows a form to pick a product, movement type, quantity and reason.
 * POST -> validates the input, updates Productos.cantidad accordingly via
 *         ProductoData.actualizarCantidad, stores the movement via
 *         MovimientoData.insertar, and redirects to StockList.
 *
 * A 'salida' that would take the product's stock below zero is rejected;
 * the form is redisplayed with an error message and no changes are made.
 */
public class MovimientoNuevo extends HttpServlet {

    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        renderForm(response, null, null, null, null, null);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productoId = request.getParameter("producto_id");
        String tipo = request.getParameter("tipo");
        String cantidadStr = request.getParameter("cantidad");
        String motivo = request.getParameter("motivo");

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            renderForm(response, "Quantity must be a valid whole number.", productoId, tipo, cantidadStr, motivo);
            return;
        }

        if (cantidad <= 0) {
            renderForm(response, "Quantity must be greater than zero.", productoId, tipo, cantidadStr, motivo);
            return;
        }

        if (tipo == null || (!tipo.equals("entrada") && !tipo.equals("salida"))) {
            renderForm(response, "Please select a valid movement type.", productoId, tipo, cantidadStr, motivo);
            return;
        }

        try {
            Producto producto = ProductoData.obtenerPorId(connection, productoId);
            if (producto == null) {
                renderForm(response, "Selected product was not found.", productoId, tipo, cantidadStr, motivo);
                return;
            }

            int nuevaCantidad;
            if (tipo.equals("entrada")) {
                nuevaCantidad = producto.cantidad + cantidad;
            } else {
                nuevaCantidad = producto.cantidad - cantidad;
                if (nuevaCantidad < 0) {
                    renderForm(response, "Cannot record this exit: only " + producto.cantidad
                            + " unit(s) of \"" + producto.nombre + "\" are in stock.",
                            productoId, tipo, cantidadStr, motivo);
                    return;
                }
            }

            ProductoData.actualizarCantidad(connection, productoId, nuevaCantidad);
            MovimientoData.insertar(connection, productoId, tipo, cantidad, motivo);
        } catch (SQLException e) {
            e.printStackTrace();
            renderForm(response, "A database error occurred while saving the movement.", productoId, tipo, cantidadStr, motivo);
            return;
        }

        response.sendRedirect("StockList");
    }

    /**
     * Renders the movement form. If error is non-null, an error banner is shown
     * and the previously submitted values are pre-selected/filled in.
     */
    private void renderForm(HttpServletResponse response, String error, String selectedProductoId,
                             String selectedTipo, String cantidadValue, String motivoValue)
            throws IOException {
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
        out.println("    <title>ERP - New Stock Movement | Developed by Aldo</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("    <header class=\"app-header\">");
        out.println("        <h1>New Stock Movement</h1>");
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

        out.println("        <form method=\"post\" action=\"MovimientoNuevo\">");
        out.println("            <p>");
        out.println("                <label for=\"producto_id\"><strong>Product:</strong></label><br>");
        out.println("                <select id=\"producto_id\" name=\"producto_id\" required>");
        out.println("                    <option value=\"\">-- Select a product --</option>");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            boolean selected = selectedProductoId != null && selectedProductoId.equals(p.id);
            out.println("                    <option value=\"" + p.id + "\""
                    + (selected ? " selected" : "")
                    + ">" + p.nombre + " (in stock: " + p.cantidad + ")</option>");
        }
        out.println("                </select>");
        out.println("            </p>");
        out.println("            <p>");
        out.println("                <label for=\"tipo\"><strong>Movement Type:</strong></label><br>");
        out.println("                <select id=\"tipo\" name=\"tipo\" required>");
        out.println("                    <option value=\"\">-- Select type --</option>");
        out.println("                    <option value=\"entrada\"" + ("entrada".equals(selectedTipo) ? " selected" : "") + ">Entrada (stock in)</option>");
        out.println("                    <option value=\"salida\"" + ("salida".equals(selectedTipo) ? " selected" : "") + ">Salida (stock out)</option>");
        out.println("                </select>");
        out.println("            </p>");
        out.println("            <p>");
        out.println("                <label for=\"cantidad\"><strong>Quantity:</strong></label><br>");
        out.println("                <input type=\"number\" id=\"cantidad\" name=\"cantidad\" value=\""
                + (cantidadValue != null ? cantidadValue : "") + "\" min=\"1\" required>");
        out.println("            </p>");
        out.println("            <p>");
        out.println("                <label for=\"motivo\"><strong>Reason:</strong></label><br>");
        out.println("                <input type=\"text\" id=\"motivo\" name=\"motivo\" value=\""
                + (motivoValue != null ? motivoValue : "") + "\" maxlength=\"255\" required>");
        out.println("            </p>");
        out.println("            <p>");
        out.println("                <button type=\"submit\">Save</button>");
        out.println("                <a href=\"StockList\">Cancel</a>");
        out.println("            </p>");
        out.println("        </form>");

        out.println("    </main>");

        out.println("    <footer class=\"app-footer\">");
        out.println("        &copy; 2026 ERP System - Developed by Aldo");
        out.println("    </footer>");

        out.println("</body>");
        out.println("</html>");
    }
}
