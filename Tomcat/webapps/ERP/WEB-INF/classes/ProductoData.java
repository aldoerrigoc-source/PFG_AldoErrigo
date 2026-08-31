import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * ProductoData
 *
 * Static SQL helpers for the Productos table.
 */
public class ProductoData {

    /** Returns all products in the Productos table. */
    public static Vector<Producto> listarTodos(Connection conn) throws SQLException {
        Vector<Producto> productos = new Vector<Producto>();
        String sql = "SELECT id, nombre, categoria, cantidad, coste, imagen_url FROM Productos";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            productos.add(mapRow(rs));
        }
        rs.close();
        ps.close();
        return productos;
    }

    /** Returns a single product by id, or null if not found. */
    public static Producto obtenerPorId(Connection conn, String id) throws SQLException {
        Producto producto = null;
        String sql = "SELECT id, nombre, categoria, cantidad, coste, imagen_url FROM Productos WHERE id+'' = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            producto = mapRow(rs);
        }
        rs.close();
        ps.close();
        return producto;
    }

    /** Updates only the cantidad (stock quantity) for the given product id. */
    public static void actualizarCantidad(Connection conn, String id, int cantidad) throws SQLException {
        String sql = "UPDATE Productos SET cantidad = ? WHERE id+'' = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setString(2, id);
        ps.executeUpdate();
        ps.close();
    }

    private static Producto mapRow(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getString("id"),
            rs.getString("nombre"),
            rs.getString("categoria"),
            rs.getInt("cantidad"),
            rs.getDouble("coste"),
            rs.getString("imagen_url")
        );
    }
}
