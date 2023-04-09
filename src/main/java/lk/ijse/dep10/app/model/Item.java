package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.util.Size;

import java.io.Serializable;

public class Item implements Serializable {
    private String id;
    private String category;
    private String itemName;
    private Size size;
    private int prize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public Item() {
    }

    public Item(String id, String category, String itemName, Size size, int prize) {
        this.id = id;
        this.category = category;
        this.itemName = itemName;
        this.size = size;
        this.prize = prize;
    }
}
