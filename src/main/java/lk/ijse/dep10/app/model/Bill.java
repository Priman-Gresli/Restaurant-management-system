package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.util.Size;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {

    private ArrayList<OrderedBill> arrayList;
    private double totalPrize;
    private String date;
    private String time;
    private String cashierName;
    private String cashierId;


    public Bill() {
    }

    public ArrayList<OrderedBill> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<OrderedBill> arrayList) {
        this.arrayList = arrayList;
    }

    public double getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(int totalPrize) {
        this.totalPrize = totalPrize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public Bill(ArrayList<OrderedBill> arrayList, double totalPrize, String date, String time, String cashierName) {
        this.arrayList = arrayList;
        this.totalPrize = totalPrize;
        this.date = date;
        this.time = time;
        this.cashierName = cashierName;
    }

    public Bill(ArrayList<OrderedBill> arrayList, double totalPrize, String date, String time, String cashierName, String cashierId) {
        this.arrayList = arrayList;
        this.totalPrize = totalPrize;
        this.date = date;
        this.time = time;
        this.cashierName = cashierName;
        this.cashierId = cashierId;
    }

    @Override
    public String toString() {
        return totalPrize+"";
    }
}
