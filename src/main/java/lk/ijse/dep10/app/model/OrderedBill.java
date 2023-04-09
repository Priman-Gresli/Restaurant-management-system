package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.util.Size;

import java.io.Serializable;

public class OrderedBill implements Serializable {
    private String itemName;
    private int quantity;
    private String itemCategory;
    private int unitPrize;
    private Size size;
    private int prize;

    public OrderedBill(String itemName, int quantity, String itemCategory, int unitPrize, Size size, int prize) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemCategory = itemCategory;
        this.unitPrize = unitPrize;
        this.size = size;
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
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

    public int getUnitPrize() {
        return unitPrize;
    }

    public void setUnitPrize(int unitPrize) {
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

    @Override
    public String toString() {
        return "OrderedBill{" +
                "itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", itemCategory='" + itemCategory + '\'' +
                ", unitPrize=" + unitPrize +
                ", size=" + size +
                ", prize=" + prize +
                '}';
    }
}
