package lk.ijse.dep10.app.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Bill;
import lk.ijse.dep10.app.model.Item;
import lk.ijse.dep10.app.model.OrderedBill;
import lk.ijse.dep10.app.model.StaffMember;
import lk.ijse.dep10.app.util.Size;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerSceneController extends LoggedUserDetails{

    public ToggleGroup Mode;
    public Label lblTotalBill;
    public Label lblTotalPrize;
    public Button btnPrint;
    @FXML
    private Button btn0;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private ToggleButton btnAddNewCashier;

    @FXML
    private ToggleButton btnAddNewItem;

    @FXML
    private Button btnBill;

    @FXML
    private ToggleButton btnBusinessSummary;

    @FXML
    private ToggleButton btnCashierMode;

    @FXML
    private Button btnDot;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnOpenCashBox;

    @FXML
    private Button btnPlus;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnSkip;

    @FXML
    private ImageView imgBakeryItem;

    @FXML
    private ImageView imgDesserts;

    @FXML
    private ImageView imgDrinks;

    @FXML
    private ImageView imgKottu;

    @FXML
    private ImageView imgLogOut;

    @FXML
    private ImageView imgRIce;

    @FXML
    private Label lblBakery;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDessert;

    @FXML
    private Label lblDrinks;

    @FXML
    private Label lblId;

    @FXML
    private Label lblIdNo;

    @FXML
    private Label lblKottu;

    @FXML
    private Label lblLogName;

    @FXML
    private Label lblLogOut;

    @FXML
    private Label lblName;

    @FXML
    private Label lblRice;

    @FXML
    private Label lblTime;

    @FXML
    private GridPane leftAnchor;

    @FXML
    private AnchorPane leftAnchorStage1;

    @FXML
    private VBox leftStage1Vbox;

    @FXML
    private AnchorPane rootRightStage2;
    public String quantity="";
    public static double itemPrize;
    public static String itemName;
    public static String itemCategory;
    public static Size size;
    private double total;
    String date;
    String date1;
    String time;
    String time1;
    double reportTotal;
    int qty;
    @FXML
    private TableView<OrderedBill> tblBill;
    List<OrderedBill> itemArrayList = new ArrayList<>();
    ArrayList<OrderedBill> itemArrayList2 = new ArrayList<>();
    private JasperReport jasperReport;

    public void initialize() throws JRException {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/report/bill.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);

        if (loggedId.substring(0,1).equals("C")){
            btnBusinessSummary.setDisable(true);
            btnAddNewCashier.setDisable(true);
        }

        tblBill.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblBill.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        tblBill.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("size"));
        tblBill.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblBill.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("unitPrize"));
        tblBill.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("prize"));


        KeyFrame key =new KeyFrame(Duration.seconds(1), event ->{
             date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
             time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            lblDate.setText(date);
            lblTime.setText(time);
        });

        tblBill.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            btnRemove.setDisable(current == null);
        });

        Timeline timeline = new Timeline(key);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

        lblName.setText(loggedName);
        lblIdNo.setText(loggedId+"");

        btnAddNewCashier.setSelected(false);
        btnAddNewItem.setSelected(false);
        btnCashierMode.setSelected(true);
        btnBusinessSummary.setSelected(false);


    }
    public void btnPrintOnAction(ActionEvent actionEvent) throws JRException {
        HashMap<String, Object> reportParams = new HashMap<>();
        reportParams.put("Date",date1);
        reportParams.put("Time",time1);
        reportParams.put("Total-Bill",reportTotal);
        Connection dataSource = DBConnection.getInstance().getConnection();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource);
        JasperViewer.viewReport(jasperPrint,false);


    }
    @FXML
    void btnEnterOnAction(ActionEvent event) {
        if (itemName==null || itemPrize==0.0 || quantity.equals("")|| quantity.equals("0")) return;
        ObservableList<OrderedBill> items = tblBill.getItems();
        qty = Integer.parseInt(quantity);
        total+=(itemPrize * qty);
        OrderedBill orderedBill = new OrderedBill(itemName, qty, itemCategory, itemPrize,size,qty*itemPrize);

        List<OrderedBill> filteredItems = itemArrayList.stream()
                .filter(itemsArrayList -> itemsArrayList.getSize()== size).filter(itemsArrayList -> itemsArrayList.getItemName().equals(itemName))
                .collect(Collectors.toList());

        for (OrderedBill filteredItem : filteredItems) {
            System.out.println(filteredItem.toString());
        }

        if (filteredItems.size()!=0) return;

        items.add(orderedBill);
        itemArrayList.add(orderedBill);
        itemArrayList2.add(orderedBill);
        lblTotalPrize.setText(total+"");
        reportTotal=total;
        itemName=null;
        itemCategory=null;
        itemPrize=0.0;
        quantity="";

    }
    @FXML
    void btnBillOnAction(ActionEvent event) {
        try {
            Bill billedItem = new Bill(itemArrayList2,total, date, time, loggedName, loggedId+"");
            date1=date;
            time1=time;
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
                String sql = "INSERT INTO Bill(bill_date, bill_time, cashier, total_prize) VALUES ('%s','%s', '%s', '%s') ";
                sql = String.format(sql, billedItem.getDate(), billedItem.getTime(), billedItem.getCashierName(), billedItem.getTotalPrize());
                statement.executeUpdate(sql);

                String sql2 ="SELECT LAST_INSERT_ID()";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet = statement1.executeQuery(sql2);
                resultSet.next();
            System.out.println(resultSet.getInt(1));
                int id = resultSet.getInt(1);
            System.out.println(id);

                PreparedStatement statement2 = connection.prepareStatement("INSERT INTO BillItems(id,item_name, item_qty, category, item_size, item_prize) VALUES (?,?,?,?,?,?)");
                for (OrderedBill bill : itemArrayList2) {
                    statement2.setInt(1,id);
                    statement2.setString(2,bill.getItemName());
                    statement2.setInt(3,bill.getQuantity());
                    statement2.setString(4,bill.getItemCategory());
                    statement2.setString(5,bill.getSize()+"");
                    statement2.setDouble(6,bill.getUnitPrize());
                    statement2.executeUpdate();
                }
                connection.commit();
                connection.rollback();
                tblBill.getSelectionModel().clearSelection();
                tblBill.getItems().clear();
            itemArrayList=new ArrayList<>();
            itemArrayList2=new ArrayList<>();
            total=0;
            lblTotalPrize.setText(total+"");
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the Item, try again!").showAndWait();
        }
    }

    @FXML
    void btn0OnAction(ActionEvent event) {
        quantity+="0";
    }

    @FXML
    void btn1OnAction(ActionEvent event) {
        quantity+="1";
    }

    @FXML
    void btn2OnAction(ActionEvent event) {
        quantity+="2";
    }

    @FXML
    void btn3OnAction(ActionEvent event) {
        quantity+="3";
    }

    @FXML
    void btn4OnAction(ActionEvent event) {
        quantity+="4";
    }

    @FXML
    void btn5OnAction(ActionEvent event) {
        quantity+="5";
    }

    @FXML
    void btn6OnAction(ActionEvent event) {
        quantity+="6";
    }

    @FXML
    void btn7OnAction(ActionEvent event) {
        quantity+="7";
    }

    @FXML
    void btn8OnAction(ActionEvent event) {
        quantity+="8";
    }

    @FXML
    void btn9OnAction(ActionEvent event) {
        quantity+="9";

    }
    @FXML
    void btnDotOnAction(ActionEvent event) {
        quantity+=".";
    }

    @FXML
    void btnOpenCashBoxOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlusOnAction(ActionEvent event) {
        quantity="";
    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        OrderedBill selectedItem = tblBill.getSelectionModel().getSelectedItem();
        total-=selectedItem.getPrize();
        tblBill.getItems().remove(tblBill.getSelectionModel().getSelectedItem());
        itemArrayList.clear();
        itemArrayList2.clear();
        ObservableList<OrderedBill> items = tblBill.getItems();
        for (OrderedBill orderedBill : items) {
            itemArrayList.add(orderedBill);
            itemArrayList2.add(orderedBill);
        }
        tblBill.getSelectionModel().clearSelection();
        lblTotalPrize.setText(total+"");
    }

    @FXML
    void btnSkipOnAction(ActionEvent event) {
        tblBill.getItems().clear();
        itemArrayList.clear();
        itemArrayList2.clear();
    }
    @FXML
    void btnBusinessSummaryOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//         stage = (Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ShopDataScene.fxml")).load()));
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        btnBusinessSummary.setSelected(false);
        btnCashierMode.setSelected(true);

    }

    @FXML
    void btnAddNewCashierOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddCashier.fxml")).load()));
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        btnAddNewCashier.setSelected(false);
        btnCashierMode.setSelected(true);
    }

    @FXML
    void btnAddNewItemOnAction(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddNewItem.fxml")).load()));
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        btnAddNewItem.setSelected(false);
        btnCashierMode.setSelected(true);
    }

    @FXML
    void btnCashierModeOnAction(ActionEvent event) throws IOException {
        btnCashierMode.setSelected(true);

    }

    @FXML
    void imgBakeryItemMouseReleased(MouseEvent event) {
        toBakeryCategory();
    }

    @FXML
    void imgBakeryItemTouchReleased(TouchEvent event) {
        toBakeryCategory();
    }

    @FXML
    void imgDessertsMouseReleased(MouseEvent event) {
        toDessertCategory();
    }

    @FXML
    void imgDessertsTouchReleased(TouchEvent event) {
        toDessertCategory();
    }

    @FXML
    void imgDrinksMouseReleased(MouseEvent event) {
        toDrinksCategory();
    }

    @FXML
    void imgDrinksTouchReleased(TouchEvent event) {
        toDrinksCategory();
    }

    @FXML
    void imgKottuMouseReleased(MouseEvent event) {
        toKottuCategory();
    }

    @FXML
    void imgKottuTouchReleased(TouchEvent event) {
        toKottuCategory();
    }

    @FXML
    void imgRIceMouseReleased(MouseEvent event)  {
        toRiceCategory();
    }

    @FXML
    void imgRIceTouchReleased(TouchEvent event) {
        toRiceCategory();
    }

    public void imgLogOutOnMouseReleased(MouseEvent event) {
        logout();
    }
    public void imgLogOutOnTouchReleased(TouchEvent touchEvent) {
        logout();
    }
    private void toRiceCategory()  {
        quantity="";
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryRice.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    private void toKottuCategory()  {
        quantity="";
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryKottu.fxml")).load()));

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    private void toBakeryCategory()  {
        quantity="";
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryBakery.fxml")).load()));

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void toDessertCategory()  {
        quantity="";
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryDessert.fxml")).load()));

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }
    private void toDrinksCategory()  {
        quantity="";
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryDrinks.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    private void logout()  {
        Stage stage1 =(Stage) btnAddNewCashier.getScene().getWindow();
        stage1.close();
        Stage stage = new Stage();
//        stage.setMaximized(false);
        stage.sizeToScene();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainLogging.fxml")).load()));
            stage.centerOnScreen();
            stage.setTitle("Main Logging");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }

    public void btnEnterKeyRelease(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) btnEnter.fire();
    }
}
