/**
 * Movimiento
 *
 * Plain data object representing a row from the Movimientos table.
 * Used by MovimientoData and the MovimientoNuevo servlet.
 */
public class Movimiento {
    public String id;
    public String productoId;
    public String tipo;
    public int cantidad;
    public String fecha;
    public String motivo;

    public Movimiento(String id, String productoId, String tipo, int cantidad, String fecha, String motivo) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.motivo = motivo;
    }
}
