/**
 * InventarioFisico
 *
 * Plain data object representing a row from the InventarioFisico table.
 * Used by InventarioFisicoData and the InventarioFisicoNuevo servlet.
 */
public class InventarioFisico {
    public String id;
    public String productoId;
    public int cantidadSistema;
    public int cantidadFisica;
    public int discrepancia;
    public String fecha;

    public InventarioFisico(String id, String productoId, int cantidadSistema, int cantidadFisica, int discrepancia, String fecha) {
        this.id = id;
        this.productoId = productoId;
        this.cantidadSistema = cantidadSistema;
        this.cantidadFisica = cantidadFisica;
        this.discrepancia = discrepancia;
        this.fecha = fecha;
    }
}
