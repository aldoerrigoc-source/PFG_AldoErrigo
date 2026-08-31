import java.util.Vector;

public class ProductoMockup {

    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    // TODO: añadir campo 'stock' (int)

    public ProductoMockup(int id, String nombre, String categoria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    public static Vector<ProductoMockup> getAll() {
        Vector<ProductoMockup> lista = new Vector<>();
        lista.add(new ProductoMockup(1, "Teclado mecanico",  "Perifericos", 89.99));
        lista.add(new ProductoMockup(2, "Monitor 27",        "Pantallas",   349.00));
        lista.add(new ProductoMockup(3, "Raton inalambrico", "Perifericos",  45.50));
        lista.add(new ProductoMockup(4, "Webcam HD",         "Accesorios",   62.00));
        return lista;
    }

    public int getId()           { return id; }
    public String getNombre()    { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio()    { return precio; }
    // TODO: añadir getter getStock()
}
