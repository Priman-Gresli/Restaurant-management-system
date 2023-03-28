package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class MainLoggingController implements Initializable {

    public Button btnLogin;


    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    @FXML
    private AnchorPane anchorImage;

    @FXML
    private AnchorPane anchorVbox;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUsername;

    private String name;
    private int id;
    private String status;

    @FXML
    private MediaView mediaView;

    @FXML
    private AnchorPane root;
    private MediaPlayer mediaPlayer;

    @FXML
    private AnchorPane userSideMainAnchor;

    public void initialize(URL url , ResourceBundle resourceBundle) {

//        txtPassword.textProperty().addListener((observableValue, previous, current) -> {
//            if (txtPassword.getText().isEmpty()) {
//                txtPassword.requestFocus();
//                txtPassword.getStyleClass().add("invalid");
////                flag=false;
//            }
//        });
//        File file = new File("data/video.mp4");
//        Media media = new Media(file.toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
//        System.out.println(mediaPlayer==null);
//        mediaView.setMediaPlayer(mediaPlayer);
//        Platform.runLater(()->{
//           mediaPlayer.play();
//        });
    }

//    private void videoPlay() {
//        File file = new File("data/video.mp4");
//        Media media = new Media(file.toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
//        mediaView.setMediaPlayer(mediaPlayer);
//
//    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {

        if (isValid()) {
            if (checkLoginData()){
                Stage stage = (Stage) txtPassword.getScene().getWindow();
                txtPassword.getScene().getWindow().hide();
                if (status.equals("OWNER")) {
                    stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
                    stage.setMaximized(true);
                    stage.centerOnScreen();
                    stage.setTitle("OWNER MODE");
                    stage.show();
                    System.out.println("admin");
                }else {
                    stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/CashierScene.fxml")).load()));
                    stage.setMaximized(true);
                    stage.centerOnScreen();
                    stage.setTitle("CASHIER MODE");
                    stage.show();
                    System.out.println("cashier");
                }
            }
        }
    }

    private boolean checkLoginData() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Staff WHERE user_name=? AND password=?");
            preparedStatement.setString(1,txtUsername.getText());
            preparedStatement.setString(2,txtPassword.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("full_name");
                status = resultSet.getString("status");
                id = resultSet.getInt("staff_id");
                System.out.println(name);
                System.out.println(status);
                System.out.println(id);
                return true;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Login!").showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to check login data with database").showAndWait();
            return false;
        }
    }

    private boolean isValid() {
        boolean flag = true;

        if (txtPassword.getText().isEmpty()) {
            txtPassword.requestFocus();
            txtPassword.getStyleClass().add("invalid");
            flag = false;
        }
        if (txtUsername.getText().isEmpty()) {
            txtUsername.requestFocus();
            txtUsername.getStyleClass().add("invalid");
            flag = false;
        }

        return flag;
    }

}
