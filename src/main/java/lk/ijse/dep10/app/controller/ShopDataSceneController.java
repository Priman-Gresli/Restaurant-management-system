package lk.ijse.dep10.app.controller;

import javafx.animation.KeyFrame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.*;
import lk.ijse.dep10.app.util.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

public class ShopDataSceneController {

    public ComboBox<String> cmbPeriod;
    public TableView<Bill> tblBills;
    public TableView<OrderedBill> tblItems;
    public LineChart chtLineChart;
    public Label lblTimePeriod;
    public BarChart chtBarChart;
    public Label lblTodayTotal;
    ArrayList<OrderedBill> itemArrayList = new ArrayList<>();
    ArrayList<Bill> itemArrayALL = new ArrayList<>();
    List<Bill> itemListALL = new ArrayList<>();
    ArrayList<Bill> itemArrayThisWeek = new ArrayList<>();
    List<Bill> itemListThisWeek = new ArrayList<>();
    ArrayList<Bill> itemArrayLastWeek = new ArrayList<>();
    List<Bill> itemListLastWeek = new ArrayList<>();
    ArrayList<Bill> itemArrayLastMonth = new ArrayList<>();
    List<Bill> itemListLastMonth = new ArrayList<>();
    @FXML
    private BorderPane borderPaneBelow;
    @FXML
    private BorderPane borderPaneUpper;

    public void initialize() {


        tblBills.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblBills.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("time"));
        tblBills.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        tblBills.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("totalPrize"));

        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrize"));

        ObservableList<String> items = cmbPeriod.getItems();
        items.add("ALL");
//        items.add("THIS WEEK");
        items.add("LAST 7 DAYS");
        items.add("LAST MONTH");

        cmbPeriod.getSelectionModel().selectedItemProperty().addListener(((observableValue, previous, current) -> {
            switch (current) {
                case "ALL":
                    addDataToTable("ALL");
                    break;
//                case "THIS WEEK":
//                    addDataToTable("THIS WEEK");
//                    break;
                case "LAST 7 DAYS":
                    addDataToTable("LAST WEEK");
                    break;
                case "LAST MONTH":
                    addDataToTable("LAST MONTH");
                    break;
            }

        }));
        String sqlAll = "SELECT * FROM Bill";
        String sqlThisWeek = "SELECT * FROM Bill WHERE YEAR(bill_date) = YEAR(NOW()) AND WEEK(bill_date) = WEEK(NOW())";
        String sqlLastWeek = "SELECT * FROM Bill WHERE bill_date >= DATE_SUB(DATE(NOW()), INTERVAL 1 WEEK) AND bill_date < DATE(NOW())";
        String sqlLastMonth = "SELECT * FROM Bill WHERE bill_date >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 1 MONTH) AND bill_date < DATE_FORMAT(NOW(), '%Y-%m-01')";
//        String sqlLastMonth = "SELECT * FROM Bill WHERE bill_date >= DATE_SUB(NOW(), INTERVAL 28 DAY)";
        loadData(itemArrayALL, itemListALL, sqlAll);
        loadData(itemArrayThisWeek, itemListThisWeek, sqlThisWeek);
        loadData(itemArrayLastWeek, itemListLastWeek, sqlLastWeek);
        loadData(itemArrayLastMonth, itemListLastMonth, sqlLastMonth);
        tblBills.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            tblItems.getItems().clear();
            if (current == null) return;
            ArrayList<OrderedBill> arrayList = current.getArrayList();
            for (OrderedBill orderedBill : arrayList) {
                tblItems.getItems().add(orderedBill);
            }
        });
        Comparator<String> dateComparator = Comparator.comparing(
                key -> LocalDate.parse(key, DateTimeFormatter.ISO_LOCAL_DATE)
        );
        Map<String, Double> sellAmountByItem = itemArrayThisWeek.stream()
                .collect(Collectors.groupingBy(Bill::getDate,
                        Collectors.summingDouble(Bill::getTotalPrize)));
        Map<String, Double> sortedMap0 = new TreeMap<>(dateComparator);
        sortedMap0.putAll(sellAmountByItem);
        sortedMap0.forEach((date, sellAmount) -> {
            System.out.println("DATE " + date + " sold for a total of " + sellAmount);
            lblTodayTotal.setText(sellAmount+"");
        });
    }

    public void addDataToTable(String period) {
        tblBills.getItems().clear();
        XYChart.Series series = new XYChart.Series();
        XYChart.Series seriesDessert = new XYChart.Series();
        XYChart.Series seriesDrinks = new XYChart.Series();
        XYChart.Series seriesRice = new XYChart.Series();
        XYChart.Series seriesKottu = new XYChart.Series();
        XYChart.Series seriesBakery = new XYChart.Series();
        seriesDessert.setName("Dessert");
        seriesDrinks.setName("Drinks");
        seriesRice.setName("Rice");
        seriesKottu.setName("Kottu");
        seriesBakery.setName("Bakery");
        chtBarChart.getData().clear();
        chtLineChart.getData().clear();
        Comparator<String> dateComparator = Comparator.comparing(
                key -> LocalDate.parse(key, DateTimeFormatter.ISO_LOCAL_DATE)
        );

        switch (period) {
            case "ALL":
                for (Bill bill : itemArrayALL) {
                    tblBills.getItems().add(bill);
                }
                break;
//            case "THIS WEEK":
//                for (Bill bill : itemArrayThisWeek) {
//                    tblBills.getItems().add(bill);
//                }
//
//                Map<String, Double> sellAmountByItem = itemArrayThisWeek.stream()
//                        .collect(Collectors.groupingBy(Bill::getDate,
//                                Collectors.summingDouble(Bill::getTotalPrize)));
//                Map<String, Double> sortedMap0 = new TreeMap<>(dateComparator);
//                sortedMap0.putAll(sellAmountByItem);
//                sortedMap0.forEach((date, sellAmount) -> {
//                    System.out.println("DATE " + date + " sold for a total of " + sellAmount);
//                    series.getData().add(new XYChart.Data(date,sellAmount));
//                });
//                chtLineChart.getData().add(series);
//
//                Map<String, Map<String, Integer>> groupedSales = itemArrayThisWeek.stream()
//                        .collect(Collectors.groupingBy(Bill::getDate,
//                                Collectors.flatMapping(sale -> sale.getArrayList().stream(),
//                                        Collectors.groupingBy(OrderedBill::getItemCategory,
//                                                Collectors.summingInt(OrderedBill::getQuantity)))));
//
//                for (String date : groupedSales.keySet()) {
//                    Map<String, Integer> value = groupedSales.get(date);
//                    for (String categoryQty : value.keySet()) {
//                        int qty=value.get(categoryQty);
//                        switch (categoryQty){
//                            case "RICE":
//                                seriesRice.getData().add(new XYChart.Data(date,qty));
//                                System.out.println(date+ ":"+categoryQty+":"+qty);
//                                System.out.println("---------------");
//                                break;
//                            case "KOTTU":
//                                seriesKottu.getData().add(new XYChart.Data(date,qty));
//                                System.out.println(date+ ":"+categoryQty+":"+qty);
//                                System.out.println("---------------");
//                                break;
//                            case "DESSERTS":
//                                seriesDessert.getData().add(new XYChart.Data(date,qty));
//                                System.out.println(date+ ":"+categoryQty+":"+qty);
//                                System.out.println("---------------");
//                                break;
//                            case "DRINKS":
//                                seriesDrinks.getData().add(new XYChart.Data(date,qty));
//                                System.out.println(date+ ":"+categoryQty+":"+qty);
//                                System.out.println("---------------");
//                                break;
//                            case "BAKERY ITEMS":
//                                seriesBakery.getData().add(new XYChart.Data(date,qty));
//                                System.out.println(date+ ":"+categoryQty+":"+qty);
//                                System.out.println("---------------");
//                                break;
//                        }
//                    }
//                }
//                System.out.println(groupedSales);
//                chtBarChart.getData().addAll(seriesRice,seriesKottu,seriesBakery,seriesDrinks,seriesDessert);
//                break;
            case "LAST WEEK":
                for (Bill bill : itemArrayLastWeek) {
                    tblBills.getItems().add(bill);
                }

                Map<String, Double> sellAmountByItem2 = itemArrayLastWeek.stream()
                        .collect(Collectors.groupingBy(Bill::getDate,
                                Collectors.summingDouble(Bill::getTotalPrize)));
                Map<String, Double> sortedMap2 = new TreeMap<>(dateComparator);
                sortedMap2.putAll(sellAmountByItem2);
                sortedMap2.forEach((date, sellAmount) -> {
                    System.out.println("DATE " + date + " sold for a total of " + sellAmount);
                    series.getData().add(new XYChart.Data(date,sellAmount));
                });
                chtLineChart.getData().add(series);

                Map<String, Map<String, Integer>> groupedSales2 = itemArrayLastWeek.stream()
                        .collect(Collectors.groupingBy(Bill::getDate,
                                Collectors.flatMapping(sale -> sale.getArrayList().stream(),
                                        Collectors.groupingBy(OrderedBill::getItemCategory,
                                                Collectors.summingInt(OrderedBill::getQuantity)))));

                Map<String, Map<String, Integer>> sortedMap = new TreeMap<>(dateComparator);
                sortedMap.putAll(groupedSales2);

                for (String date : sortedMap.keySet()) {
                    Map<String, Integer> value = sortedMap.get(date);
                    for (String categoryQty : value.keySet()) {
                            int qty=value.get(categoryQty);
                                switch (categoryQty){
                                    case "RICE":
                                        seriesRice.getData().add(new XYChart.Data(date,qty));
                                        System.out.println(date+ ":"+categoryQty+":"+qty);
                                        System.out.println("---------------");
                                        break;
                                    case "KOTTU":
                                        seriesKottu.getData().add(new XYChart.Data(date,qty));
                                        System.out.println(date+ ":"+categoryQty+":"+qty);
                                        System.out.println("---------------");
                                        break;
                                    case "DESSERTS":
                                        seriesDessert.getData().add(new XYChart.Data(date,qty));
                                        System.out.println(date+ ":"+categoryQty+":"+qty);
                                        System.out.println("---------------");
                                        break;
                                    case "DRINKS":
                                        seriesDrinks.getData().add(new XYChart.Data(date,qty));
                                        System.out.println(date+ ":"+categoryQty+":"+qty);
                                        System.out.println("---------------");
                                        break;
                                    case "BAKERY ITEMS":
                                        seriesBakery.getData().add(new XYChart.Data(date,qty));
                                        System.out.println(date+ ":"+categoryQty+":"+qty);
                                        System.out.println("---------------");
                                        break;
                                }
                        }
                }
                chtBarChart.getData().addAll(seriesRice,seriesKottu,seriesBakery,seriesDrinks,seriesDessert);
                System.out.println(sortedMap);
                break;
            case "LAST MONTH":
                for (Bill bill : itemArrayLastMonth) {
                    tblBills.getItems().add(bill);
                }
                //income chart
                ArrayList<WeeklySale> weeklySale = new ArrayList<>();
                for (Bill sale : itemArrayLastMonth) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime( new SimpleDateFormat("yyyy-MM-dd").parse(sale.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int week = cal.get(Calendar.WEEK_OF_MONTH);
                    System.out.println("WEEK: "+week);

                    weeklySale.add(new WeeklySale(week, sale.getTotalPrize()));
                }
                HashMap<Integer, Double> weeklySalesData = new HashMap<>();
                for (WeeklySale sale : weeklySale) {
                    weeklySalesData.put(sale.getWeek(), weeklySalesData.getOrDefault(sale.getWeek(), 0.0) + sale.getTotalSales());
                }

                for (int week : weeklySalesData.keySet()) {
                    double sales = weeklySalesData.get(week);

                    weeklySale.add(new WeeklySale(week, sales));
                }
                for (Integer weekNumber : weeklySalesData.keySet()) {
                    Double aDouble = weeklySalesData.get(weekNumber);
                    series.getData().add(new XYChart.Data("Week"+(weekNumber)+"",aDouble));
                    System.out.println(weekNumber+" : "+aDouble);
                }
                chtLineChart.getData().add(series);

                //qty per week
                ArrayList<WeeklyQty> weeklyRiceQty = new ArrayList<>();
                ArrayList<WeeklyQty> weeklyKottuQty = new ArrayList<>();
                ArrayList<WeeklyQty> weeklyBakeryQty = new ArrayList<>();
                ArrayList<WeeklyQty> weeklyDrinkQty = new ArrayList<>();
                ArrayList<WeeklyQty> weeklyDessertQty = new ArrayList<>();
                Map<String, Map<String, Integer>> groupedSales3 = itemArrayLastMonth.stream()
                        .collect(Collectors.groupingBy(Bill::getDate,
                                Collectors.flatMapping(sale -> sale.getArrayList().stream(),
                                        Collectors.groupingBy(OrderedBill::getItemCategory,
                                                Collectors.summingInt(OrderedBill::getQuantity)))));

                Map<String, Map<String, Integer>> sortedMap3 = new TreeMap<>(dateComparator);
                sortedMap3.putAll(groupedSales3);

                for (String date : sortedMap3.keySet()) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime( new SimpleDateFormat("yyyy-MM-dd").parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int week = cal.get(Calendar.WEEK_OF_MONTH);
                    Map<String, Integer> value = sortedMap3.get(date);
                    for (String categoryQty : value.keySet()) {
                        int qty=value.get(categoryQty);

                        switch (categoryQty){
                            case "RICE":
                                weeklyRiceQty.add(new WeeklyQty(week,qty));
                                break;
                            case "KOTTU":
                                weeklyKottuQty.add(new WeeklyQty(week,qty));
                                break;
                            case "DESSERTS":
                                weeklyDessertQty.add(new WeeklyQty(week,qty));
                                break;
                            case "DRINKS":
                                weeklyDrinkQty.add(new WeeklyQty(week,qty));
                                break;
                            case "BAKERY ITEMS":
                                weeklyBakeryQty.add(new WeeklyQty(week,qty));

                                break;
                        }
                    }
                }
                HashMap<Integer, Integer> weeklySalesData1 = new HashMap<>();
                for (WeeklyQty sale : weeklyRiceQty) {
                    weeklySalesData1.put(sale.getWeekNum(), weeklySalesData1.getOrDefault(sale.getWeekNum(), 0) + sale.getWeekQty());
                }HashMap<Integer, Integer> weeklySalesData2 = new HashMap<>();
                for (WeeklyQty sale : weeklyKottuQty) {
                    weeklySalesData2.put(sale.getWeekNum(), weeklySalesData2.getOrDefault(sale.getWeekNum(), 0) + sale.getWeekQty());
                }HashMap<Integer, Integer> weeklySalesData3 = new HashMap<>();
                for (WeeklyQty sale : weeklyBakeryQty) {
                    weeklySalesData3.put(sale.getWeekNum(), weeklySalesData3.getOrDefault(sale.getWeekNum(), 0) + sale.getWeekQty());
                }HashMap<Integer, Integer> weeklySalesData4 = new HashMap<>();
                for (WeeklyQty sale : weeklyDrinkQty) {
                    weeklySalesData4.put(sale.getWeekNum(), weeklySalesData4.getOrDefault(sale.getWeekNum(), 0) + sale.getWeekQty());
                }HashMap<Integer, Integer> weeklySalesData5 = new HashMap<>();
                for (WeeklyQty sale : weeklyDessertQty) {
                    weeklySalesData5.put(sale.getWeekNum(), weeklySalesData5.getOrDefault(sale.getWeekNum(), 0) + sale.getWeekQty());
                }

                for (Integer week : weeklySalesData1.keySet()) {
                    seriesRice.getData().add(new XYChart.Data("Week "+week,weeklySalesData1.get(week)));
                }for (Integer week : weeklySalesData2.keySet()) {
                seriesKottu.getData().add(new XYChart.Data("Week "+week,weeklySalesData2.get(week)));
                }for (Integer week : weeklySalesData3.keySet()) {
                seriesBakery.getData().add(new XYChart.Data("Week "+week,weeklySalesData3.get(week)));
                }for (Integer week : weeklySalesData4.keySet()) {
                seriesDrinks.getData().add(new XYChart.Data("Week "+week,weeklySalesData4.get(week)));
                }for (Integer week : weeklySalesData5.keySet()) {
                seriesDessert.getData().add(new XYChart.Data("Week "+week,weeklySalesData5.get(week)));
                }

                System.out.println("@#$ -> "+weeklySalesData1);
                chtBarChart.getData().addAll(seriesRice,seriesKottu,seriesBakery,seriesDrinks,seriesDessert);
                break;
        }

    }



    private void loadData(ArrayList arrayList, List list, String sql) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int billId = resultSet.getInt("bill_id");
                String billDate = resultSet.getString("bill_date");
                String billTime = resultSet.getString("bill_time");
                String cashierName = resultSet.getString("cashier");
                double totalPrize = Double.parseDouble(resultSet.getString("total_prize"));

                String sql2 = "SELECT * FROM BillItems WHERE id='%s'";
                sql2 = String.format(sql2, billId);
                ResultSet resultSet1 = statement2.executeQuery(sql2);
                itemArrayList = new ArrayList<>();
                while (resultSet1.next()) {
                    String itemName = resultSet1.getString("item_name");
                    int itemQty = resultSet1.getInt("item_qty");
                    String category = resultSet1.getString("category");
                    String itemSize = resultSet1.getString("item_size");
                    int itemPrize = resultSet1.getInt("item_prize");

                    OrderedBill orderedBill = new OrderedBill(itemName, itemQty, category, itemPrize, Size.valueOf(itemSize), itemQty * itemPrize);
                    itemArrayList.add(orderedBill);
                }

                Bill billedItem = new Bill(itemArrayList, totalPrize, billDate, billTime, cashierName);
                arrayList.add(billedItem);
                list.add(billedItem);

            }


            resultSet.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database connection lost").showAndWait();
            e.printStackTrace();
        }
    }

    public void cmbPeriodOnAction(ActionEvent actionEvent) {
    }
}
