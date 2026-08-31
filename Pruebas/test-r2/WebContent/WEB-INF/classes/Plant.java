public class Plant {

    private int id;
    private String name;
    private String image;
    private int stock;

    public Plant(int id, String name, String image, int stock) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
