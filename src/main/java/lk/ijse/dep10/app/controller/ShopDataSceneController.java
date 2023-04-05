package lk.ijse.dep10.app.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShopDataSceneController extends LoggedUserDetails{
    public ToggleGroup Mode;
    @FXML
    private ToggleButton btnAddNewCashier;

    @FXML
    private ToggleButton btnAddNewItem;

    @FXML
    private ToggleButton btnBusinessSummary;

    @FXML
    private ToggleButton btnCashierMode;

    @FXML
    private ImageView imgLogOut;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblIdNo;

    @FXML
    private Label lblLogName;

    @FXML
    private Label lblLogOut;

    @FXML
    private Label lblName;

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

    public void initialize(){
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
        btnCashierMode.setSelected(false);
        btnBusinessSummary.setSelected(true);
    }

    @FXML
    void btnBusinessSummaryOnAction(ActionEvent event) throws IOException {
        btnBusinessSummary.setSelected(true);
    }
    @FXML
    void btnAddNewCashierOnAction(ActionEvent event) throws IOException {
        Stage stage =(Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddCashier.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnAddNewItemOnAction(ActionEvent event) throws IOException {
        Stage stage =(Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddNewItem.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnCashierModeOnAction(ActionEvent event) throws IOException {
        Stage stage =(Stage) btnAddNewCashier.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
    }
}
