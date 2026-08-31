import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MovimientoData
 *
 * Static SQL helpers for the Movimientos table.
 */
public class MovimientoData {

    /** Inserts a new stock movement record. fecha is set to the current date/time. */
    public static void insertar(Connection conn, String productoId, String tipo, int cantidad, String motivo) throws SQLException {
        String sql = "INSERT INTO Movimientos (producto_id, tipo, cantidad, fecha, motivo) VALUES (?, ?, ?, NOW(), ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, productoId);
        ps.setString(2, tipo);
        ps.setInt(3, cantidad);
        ps.setString(4, motivo);
        ps.executeUpdate();
        ps.close();
    }
}
