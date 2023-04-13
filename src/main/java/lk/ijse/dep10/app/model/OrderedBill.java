package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.util.Size;

import java.io.Serializable;

public class OrderedBill implements Serializable {
    private String itemName;
    private int quantity;
    private String itemCategory;
    private double unitPrize;
    private Size size;
    private double prize;

    public OrderedBill(String itemName, int quantity, String itemCategory, double unitPrize, Size size, double prize) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemCategory = itemCategory;
        this.unitPrize = unitPrize;
        this.size = size;
        this.prize = prize;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public double getUnitPrize() {
        return unitPrize;
    }

    public void setUnitPrize(double unitPrize) {
        this.unitPrize = unitPrize;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public OrderedBill() {
    }

}
