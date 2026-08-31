import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * InventarioFisicoData
 *
 * Static SQL helpers for the InventarioFisico table.
 */
public class InventarioFisicoData {

    /** Inserts a new physical inventory count record. fecha is set to the current date/time. */
    public static void insertar(Connection conn, String productoId, int cantidadSistema, int cantidadFisica, int discrepancia) throws SQLException {
        String sql = "INSERT INTO InventarioFisico (producto_id, cantidad_sistema, cantidad_fisica, discrepancia, fecha) VALUES (?, ?, ?, ?, NOW())";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, productoId);
        ps.setInt(2, cantidadSistema);
        ps.setInt(3, cantidadFisica);
        ps.setInt(4, discrepancia);
        ps.executeUpdate();
        ps.close();
    }
}
