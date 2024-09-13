package likz;

import java.io.Serializable;

public class Chocolates implements Serializable {

    private final int id;
    private final int maker_id;
    private final String name;
    private final String structure;
    private final int price;
    private final String prodDate;
    private final int storageLifeDays;

    public Chocolates(int id, int maker_id, String name, String structure, int price, String prodDate, int storageLifeDays) {
        this.id = id;
        this.maker_id = maker_id;
        this.name = name;
        this.structure = structure;
        this.price = price;
        this.prodDate = prodDate;
        this.storageLifeDays = storageLifeDays;
    }

    public int getId() {
        return id;
    }

    public int getMaker_id() {
        return maker_id;
    }

    public String getName() {
        return name;
    }

    public String getStructure() {
        return structure;
    }

    public int getPrice() {
        return price;
    }

    public String getProdDate() {
        return prodDate;
    }

    public int getStorageLifeDays() {
        return storageLifeDays;
    }
}
