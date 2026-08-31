import java.util.Vector;

public class Data {

    public static Vector<Plant> inventory = createInventory();

    private static Vector<Plant> createInventory() {
        Vector<Plant> v = new Vector<Plant>();
        v.add(new Plant(1, "Ficus", "img/plant.svg", 12));
        v.add(new Plant(2, "Lavender", "img/plant.svg", 30));
        v.add(new Plant(3, "Aloe Vera", "img/plant.svg", 18));
        v.add(new Plant(4, "Cactus", "img/plant.svg", 25));
        return v;
    }
}
