package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddOwnerController {


    @FXML
    private Button btnConfirm;

    @FXML
    private TextField txtAdminName;

    @FXML
    private TextField txtAdminPassword;

    @FXML
    private TextField txtAdminUsername;

    @FXML
    private TextField txtRePassword;


    public void initialize() {
        txtAdminName.requestFocus();
    }
    @FXML
    public void btnConfirmOnAction(ActionEvent actionEvent) throws IOException {
        if (!isValid()) return;
        addOwnerDatabase();
        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
        stage.setTitle("Shop Owner Mode");
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();


    }

    private boolean isValid(){
        boolean flag = true;
        txtAdminUsername.getStyleClass().remove("invalid");
        txtRePassword.getStyleClass().remove("invalid");
        txtAdminPassword.getStyleClass().remove("invalid");
        txtAdminName.getStyleClass().remove("invalid");

        if (txtRePassword.getText().isEmpty() || !txtRePassword.getText().equals(txtAdminPassword.getText())) {
            flag=false;
            txtRePassword.requestFocus();
            txtRePassword.selectAll();
            txtRePassword.getStyleClass().add("invalid");

        }
        if (txtAdminPassword.getText().isEmpty() || !txtAdminPassword.getText().matches("^\\b(\\w+[!@#\\$%&]+)+$")) {
            flag=false;
            txtAdminPassword.requestFocus();
            txtAdminPassword.selectAll();
            txtAdminPassword.getStyleClass().add("invalid");
        }

        if (txtAdminUsername.getText().isEmpty() || !txtAdminUsername.getText().matches("^[A-z]+( ?[A-z]*)*$")) {
            flag=false;
            txtAdminUsername.requestFocus();
            txtAdminUsername.selectAll();
            txtAdminUsername.getStyleClass().add("invalid");
        }
        if (txtAdminName.getText().isEmpty() || !txtAdminName.getText().matches("^[A-z]+( ?[A-z]*)*$")) {
            flag=false;
            txtAdminName.requestFocus();
            txtAdminName.selectAll();
            txtAdminName.getStyleClass().add("invalid");
        }

        return flag;
    }

    private void addOwnerDatabase() {
        String name = txtAdminName.getText();
        String username = txtAdminUsername.getText();
        String password = txtRePassword.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Staff (full_name,user_name,password,status) VALUES (?,?,?,?)");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,password);
            preparedStatement.setString(4,"OWNER");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to Add Owner to Database").showAndWait();
            e.printStackTrace();
        }
    }




}
