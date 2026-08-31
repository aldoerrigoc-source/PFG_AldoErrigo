public class Record {

    private int id;
    private String title;
    private String artist;
    private String cover;
    private double price;

    public Record(int id, String title, String artist, String cover, double price) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.cover = cover;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCover() {
        return cover;
    }

    public double getPrice() {
        return price;
    }
}
