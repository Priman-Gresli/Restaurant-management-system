package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Item;
import lk.ijse.dep10.app.util.Size;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
public class CategoryDrinkController extends OwnerSceneController{
    @FXML
    private FlowPane FlowPlane1;
    List<Item> itemsArrayList = new ArrayList<>();
    public void initialize(){
        itemName=null;
        itemCategory=null;
        itemPrize=0.0;

        loadData();
    }
    private void loadData() {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Item WHERE category='DRINKS'");
            while (resultSet.next()) {

                String id = resultSet.getString("item_id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                itemCategory=category;
                double prize = Double.parseDouble(resultSet.getString("prize"));
                Size size1 = lk.ijse.dep10.app.util.Size.valueOf(resultSet.getString("size"));

                Item foodItem = new Item(id, category, name, size1, prize);
                itemsArrayList.add(foodItem);

                    Button button = new Button(name);
                    button.setMinSize(170,70);
                    button.setOnAction((ActionEvent event)-> {
                        itemName=name;
                        itemPrize= foodItem.getPrize();
                        size= lk.ijse.dep10.app.util.Size.SMALL;
                        Stage stage = (Stage) button.getScene().getWindow();
                        stage.close();
                    });
                    FlowPlane1.getChildren().add(button);

            }

            resultSet.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database connection lost").showAndWait();
            e.printStackTrace();
        }

    }




}
