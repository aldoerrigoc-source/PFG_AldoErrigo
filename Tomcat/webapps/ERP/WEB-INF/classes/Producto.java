/**
 * Producto
 *
 * Plain data object representing a row from the Productos table.
 * Used by ProductoData and the Stock* servlets.
 */
public class Producto {
    public String id;
    public String nombre;
    public String categoria;
    public int cantidad;
    public double coste;
    public String imagenUrl;

    public Producto(String id, String nombre, String categoria, int cantidad, double coste, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.coste = coste;
        this.imagenUrl = imagenUrl;
    }
}
