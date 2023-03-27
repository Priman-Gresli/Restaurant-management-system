package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainLoggingController {

    public ToggleGroup Status;
    public Button btnLogin;
    public ToggleButton tglShopOwner;
    public ToggleButton tglCashier;
    public ToggleGroup tglStatus;


    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    Toggle selectedToggle;
    public void initialize() {
        txtPassword.textProperty().addListener((observableValue, previous, current) -> {
            if (txtPassword.getText().isEmpty()) {
                txtPassword.requestFocus();
                txtPassword.getStyleClass().add("invalid");
//                flag=false;
            }
        });

        tglShopOwner.toggleGroupProperty().addListener((observableValue, previous, current) -> {
            tglShopOwner.getStyleClass().add("click");
            System.out.println("aaa");
        });


    }


    public void btnLoginOnAction(ActionEvent actionEvent) {
        if (isValid()){
            checkLoginData();
        }
    }
    private void checkLoginData(){

    }
    private boolean isValid(){
        boolean flag= true;
        selectedToggle = tglStatus.getSelectedToggle();
        if (txtPassword.getText().isEmpty()) {
            txtPassword.requestFocus();
            txtPassword.getStyleClass().add("invalid");
            flag=false;
        }
        if (txtUsername.getText().isEmpty()) {
            txtUsername.requestFocus();
            txtUsername.getStyleClass().add("invalid");
            flag=false;
        }
        if (selectedToggle == null) {
            tglShopOwner.requestFocus();
            tglCashier.getStyleClass().add("invalid");
            tglShopOwner.getStyleClass().add("invalid");
            flag=false;
        }
        return flag;
    }

    public void tglShopOwnerOnAction(ActionEvent actionEvent) {
        tglCashier.getStyleClass().remove("click");
        tglShopOwner.getStyleClass().add("click");
        System.out.println("16541");
    }

    public void tglCashierOnAction(ActionEvent actionEvent) {
        tglShopOwner.getStyleClass().remove("click");
        tglCashier.getStyleClass().add("click");
        System.out.println("8888");
    }
}
