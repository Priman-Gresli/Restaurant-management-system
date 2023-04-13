package lk.ijse.dep10.app.model;

public class WeeklyQty {
    private int weekNum;


    private int weekQty;

    public WeeklyQty() {
    }

    public WeeklyQty(int weekNum, int weekQty) {
        this.weekNum = weekNum;
        this.weekQty = weekQty;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }


    public int getWeekQty() {
        return weekQty;
    }

    public void setWeekQty(int weekQty) {
        this.weekQty = weekQty;
    }
}
