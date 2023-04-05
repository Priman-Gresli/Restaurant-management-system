package lk.ijse.dep10.app.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OwnerSceneController extends LoggedUserDetails{

    public ToggleGroup Mode;
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

    @FXML
    private TableView<?> tblBill;

    public void initialize() {

        KeyFrame key =new KeyFrame(Duration.seconds(1), event ->{
            lblDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
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

    @FXML
    void btn0OnAction(ActionEvent event) {

    }

    @FXML
    void btn1OnAction(ActionEvent event) {

    }

    @FXML
    void btn2OnAction(ActionEvent event) {

    }

    @FXML
    void btn3OnAction(ActionEvent event) {

    }

    @FXML
    void btn4OnAction(ActionEvent event) {

    }

    @FXML
    void btn5OnAction(ActionEvent event) {

    }

    @FXML
    void btn6OnAction(ActionEvent event) {

    }

    @FXML
    void btn7OnAction(ActionEvent event) {

    }

    @FXML
    void btn8OnAction(ActionEvent event) {

    }

    @FXML
    void btn9OnAction(ActionEvent event) {

    }

    @FXML
    void btnBillOnAction(ActionEvent event) {

    }
    @FXML
    void btnDotOnAction(ActionEvent event) {

    }

    @FXML
    void btnEnterOnAction(ActionEvent event) {

    }

    @FXML
    void btnExitOnAction(ActionEvent event) {

    }

    @FXML
    void btnOpenCashBoxOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlusOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {

    }

    @FXML
    void btnSkipOnAction(ActionEvent event) {

    }

    @FXML
    void btnBusinessSummaryOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ShopDataScene.fxml")).load()));
        stage.show();
        stage.centerOnScreen();

    }
    @FXML
    void btnAddNewCashierOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddCashier.fxml")).load()));
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.centerOnScreen();
        System.out.println("1");
    }

    @FXML
    void btnAddNewItemOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddNewItem.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
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
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryRice.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }
    private void toKottuCategory()  {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryKottu.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }

    private void toBakeryCategory()  {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryBakery.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }

    private void toDessertCategory()  {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryDessert.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }

    private void toDrinksCategory()  {
        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CategoryDrinks.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }
    private void logout()  {

        Stage stage = (Stage) btnAddNewCashier.getScene().getWindow();
        stage.setMaximized(false);
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
}
