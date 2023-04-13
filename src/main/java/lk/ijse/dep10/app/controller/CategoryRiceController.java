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
public class CategoryRiceController extends OwnerSceneController{
    @FXML
    private FlowPane FlowPlane1;

    @FXML
    private ToggleGroup Size;

    @FXML
    private ToggleButton tglLarge;

    @FXML
    private ToggleButton tglSmall;



    List<Item> itemsArrayList = new ArrayList<>();
    public void initialize(){
        itemName=null;
        itemCategory=null;
        itemPrize=0;

        tglLarge.setDisable(true);
        tglSmall.setDisable(true);
       loadData();
    }
    private void loadData() {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Item WHERE category='RICE'");
            while (resultSet.next()) {

                String id = resultSet.getString("item_id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                itemCategory=category;
                double prize = Double.parseDouble(resultSet.getString("prize"));
                Size size = lk.ijse.dep10.app.util.Size.valueOf(resultSet.getString("size"));

                Item foodItem = new Item(id, category, name, size, prize);
                itemsArrayList.add(foodItem);

                if (foodItem.getSize().equals(lk.ijse.dep10.app.util.Size.SMALL)) {

                  Button button = new Button(name);
                    button.setMinSize(170,70);
                    button.setOnAction((ActionEvent event)-> {
                        tglLarge.setDisable(false);
                        tglSmall.setDisable(false);
                        itemName=name;
                        System.out.println(name);
                    });
                    FlowPlane1.getChildren().add(button);
                }
            }
            resultSet.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database connection lost").showAndWait();
            e.printStackTrace();
        }

    }
    @FXML
    void tglLargeOnAction(ActionEvent event) {
        for (Item item : itemsArrayList) {
            System.out.println(item.getItemName()+" : "+item.getCategory()+" : "+item.getSize());
        }
        size= lk.ijse.dep10.app.util.Size.LARGE;
        List<Item> filteredItems = itemsArrayList.stream()
                .filter(itemsArrayList -> itemsArrayList.getSize()== lk.ijse.dep10.app.util.Size.LARGE).filter(itemsArrayList -> itemsArrayList.getItemName().equals(itemName))
                .collect(Collectors.toList());
        Item item = filteredItems.get(0);
        itemPrize = item.getPrize();
        System.out.println(itemPrize);
        Stage stage = (Stage) tglLarge.getScene().getWindow();
        stage.close();
    }

    @FXML
    void tglSmallOnAction(ActionEvent event) {
        size= lk.ijse.dep10.app.util.Size.SMALL;
        List<Item> filteredItems = itemsArrayList.stream()
                .filter(itemsArrayList -> itemsArrayList.getSize()== lk.ijse.dep10.app.util.Size.SMALL).filter(itemsArrayList -> itemsArrayList.getItemName().equals(itemName))
                .collect(Collectors.toList());
        Item item = filteredItems.get(0);
        itemPrize  = item.getPrize();
        System.out.println(itemPrize);
        Stage stage = (Stage) tglSmall.getScene().getWindow();
        stage.close();

    }


}
