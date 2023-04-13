package lk.ijse.dep10.app.model;

public class WeeklySale {
    private int week;
    private double totalSales;

    public WeeklySale() {
    }

    public WeeklySale(int week, double totalSales) {
        this.week = week;
        this.totalSales = totalSales;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }
}
