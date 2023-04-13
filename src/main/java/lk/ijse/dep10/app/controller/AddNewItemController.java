package lk.ijse.dep10.app.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Item;
import lk.ijse.dep10.app.util.Size;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;


public class AddNewItemController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCashier;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPrize;

    @FXML
    private Label lblSize;

    @FXML
    private GridPane leftAnchor;

    @FXML
    private RadioButton rbtLarge;

    @FXML
    private RadioButton rbtSmall;

    @FXML
    private AnchorPane rootRightStage21;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private ToggleGroup tglSize;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrize;

    @FXML
    private TextField txtSearch;
    private int idDigit = 1;
    private String generatedID = "A-";
    boolean isTableSelected=false;

    public void initialize() {
        btnNewCashier.fire();
        txtId.setEditable(false);

        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("category"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("size"));
        tblItem.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("prize"));


        ObservableList<String> items = cmbCategory.getItems();
        items.add("RICE");
        items.add("KOTTU");
        items.add("BAKERY ITEMS");
        items.add("DRINKS");
        items.add("DESSERTS");
        cmbCategory.setValue("Select Category");
        loadData();

        cmbCategory.getSelectionModel().selectedItemProperty().addListener(((observableValue, previous, current) -> {
            if (current == null) {
                cmbCategory.setPromptText("FOOD ITEMS");
                return;
            }
            switch (current) {
                case "RICE":
                    generatedID = "R-";
                    if (!isTableSelected)  {
                        rbtLarge.setDisable(false);
                        rbtSmall.setDisable(false);
                    }
                    break;
                case "KOTTU":
                    generatedID = "K-";
                    if (!isTableSelected)  {
                        rbtLarge.setDisable(false);
                        rbtSmall.setDisable(false);
                    }
                    break;
                case "BAKERY ITEMS":
                    generatedID = "B-";
                    tglSize.selectToggle(rbtSmall);
                    if (!isTableSelected)  {
                        rbtLarge.setDisable(true);
                        rbtSmall.setDisable(true);
                    }
                    break;
                case "DRINKS":
                    tglSize.selectToggle(rbtSmall);
                    if (!isTableSelected)  {
                        rbtLarge.setDisable(true);
                        rbtSmall.setDisable(true);
                    }
                    generatedID = "D-";
                    break;
                case "DESSERTS":
                    tglSize.selectToggle(rbtSmall);
                    if (!isTableSelected)  {
                        rbtLarge.setDisable(true);
                        rbtSmall.setDisable(true);
                    }
                    generatedID = "E-";
                    break;
            }
            generatedID = String.format(generatedID + "%03d", idDigit);
            if (!isTableSelected) {txtId.setText(generatedID);
            System.out.println(generatedID+" combo");}
        }));

        tblItem.getSelectionModel().selectedItemProperty().addListener(((observableValue, previous, current) -> {
            isTableSelected=false;
            btnDelete.setDisable(current == null);
            if (current == null) return;
            isTableSelected=true;
            Connection connection = DBConnection.getInstance().getConnection();
            // Retrieve the blob data from the database
            txtName.setText(current.getItemName());
            txtId.setText(current.getId());
            System.out.println(current.getId()+" tbl select");
            txtPrize.setText(current.getPrize() + "");
            cmbCategory.setValue(current.getCategory());
            if (current.getCategory().equals("DRINKS") ||current.getCategory().equals("DESSERTS")||current.getCategory().equals("BAKERY ITEMS")){
                rbtLarge.setDisable(true);
                rbtSmall.setDisable(true);
            }else {
                rbtLarge.setDisable(false);
                rbtSmall.setDisable(false);
            }
            if (current.getSize() == Size.LARGE) {
                rbtLarge.setSelected(true);
                rbtSmall.setSelected(false);
            } else {
                rbtSmall.setSelected(true);
                rbtLarge.setSelected(false);
            }

        }));

        txtSearch.textProperty().addListener((ov,current,previous)->{
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM Item WHERE name LIKE '%s'";
                sql= String.format(sql,"%"+txtSearch.getText()+"%"); // txtSearch.getText()=current
                ResultSet resultSet = statement.executeQuery(sql);
                tblItem.getItems().clear();
                while (resultSet.next()) {
                    String id = resultSet.getString("item_id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    double prize = Double.parseDouble(resultSet.getString("prize"));
                    Size size = Size.valueOf(resultSet.getString("size"));
                    Item foodItem = new Item(id, category, name, size, prize);
                    tblItem.getItems().add(foodItem);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadData() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Item");
            while (resultSet.next()) {

                String id = resultSet.getString("item_id");
                arrayList.add(Integer.parseInt(id.substring(2, id.length())));
                generatedID = id.substring(0, 2);
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double prize = Double.parseDouble(resultSet.getString("prize"));
                Size size = Size.valueOf(resultSet.getString("size"));

                Item foodItem = new Item(id, category, name, size, prize);
                tblItem.getItems().add(foodItem);
            }
            if (arrayList.isEmpty()) return;
            idDigit = Collections.max(arrayList)+1;
            generatedID = String.format(generatedID + "%03d",idDigit);
            System.out.println(generatedID+" load");
            resultSet.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database connection lost").showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        deleteFromDatabase();
        tblItem.getItems().remove(tblItem.getSelectionModel().getSelectedItem());
        tblItem.getSelectionModel().clearSelection();
    }

    private void deleteFromDatabase() {
        Connection connection = DBConnection.getInstance().getConnection();
        String selectedItem = tblItem.getSelectionModel().getSelectedItem().getId();
        String sql1 = "DELETE FROM Item WHERE item_id='%s'";
        sql1 = String.format(sql1, selectedItem);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql1);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to delete from database").showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void btnNewCashierOnAction(ActionEvent event) {
        txtName.clear();
        txtPrize.clear();
        tglSize.selectToggle(null);
        rbtLarge.setDisable(false);
        rbtSmall.setDisable(false);
        tblItem.getSelectionModel().clearSelection();
        txtName.requestFocus();
        cmbCategory.getSelectionModel().clearSelection();
        cmbCategory.setValue("Select Category");
        txtId.clear();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isValid()) return;
        try {
            Item item = new Item(txtId.getText().toUpperCase(), cmbCategory.getSelectionModel().getSelectedItem(),
                    txtName.getText().toUpperCase().strip(), tglSize.getSelectedToggle() == rbtSmall ? Size.SMALL : Size.LARGE, Double.parseDouble(txtPrize.getText()));

            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            Item selectedItem = tblItem.getSelectionModel().getSelectedItem();

            Statement statement2 = connection.createStatement();
            String sql2 = "SELECT * FROM Item WHERE name='%s' AND size='%s' AND category='%s'AND prize='%s'";
            sql2=String.format(sql2,item.getItemName(),item.getSize(),cmbCategory.getSelectionModel().getSelectedItem(),Double.parseDouble(txtPrize.getText()));
            ResultSet resultSet = statement2.executeQuery(sql2);

            if (resultSet.next()) {
                new Alert(Alert.AlertType.ERROR, "Item Already Exist").showAndWait();
                return;
            }

            if (selectedItem == null) {
                String sql = "INSERT INTO Item VALUES ('%s','%s', '%s', '%s', '%s')";
                sql = String.format(sql, item.getId(), item.getCategory(), item.getItemName(), item.getSize(), item.getPrize());
                stm.executeUpdate(sql);

                tblItem.getItems().add(item);
                idDigit++;
            } else {
                String sql = "UPDATE Item SET category='%s', name='%s', size='%s' ,prize='%s' WHERE item_id='%s'";
                sql = String.format(sql, item.getCategory(), item.getItemName(), item.getSize(), item.getPrize(), item.getId());
                stm.executeUpdate(sql);
                ObservableList<Item> itemList = tblItem.getItems();
                int selectedItemIndex = itemList.indexOf(selectedItem);
                itemList.set(selectedItemIndex, item);
                tblItem.refresh();
            }
            btnNewCashier.fire();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the Item, try again!").showAndWait();
        }
    }



    private boolean isValid() {
        boolean isDataValid = true;

        for (Node node : new Node[]{txtId, txtName, txtPrize}) {
            node.getStyleClass().remove("invalid");
        }
        rbtLarge.getStyleClass().remove("invalid");
        rbtSmall.getStyleClass().remove("invalid");

        String name = txtName.getText();
        String prize = txtPrize.getText();
        Toggle toggleSize = tglSize.getSelectedToggle();

        if (!prize.matches("[0-9.]{2,}")) {
            isDataValid = false;
            txtPrize.requestFocus();
            txtPrize.selectAll();
            txtPrize.getStyleClass().add("invalid");
        }
        if (toggleSize == null) {
            isDataValid = false;
            rbtSmall.requestFocus();
            rbtSmall.getStyleClass().add("invalid");
            rbtLarge.getStyleClass().add("invalid");
        }

        if (name.isEmpty() || !name.matches("[A-z0-9 ]+")) {
            isDataValid = false;
            txtName.requestFocus();
            txtName.selectAll();
            txtName.getStyleClass().add("invalid");
        }
        if (cmbCategory.getSelectionModel().getSelectedItem() == null) {
            isDataValid = false;
            cmbCategory.requestFocus();
            cmbCategory.getStyleClass().add("invalid");
        }

        return isDataValid;
    }

    @FXML
    void imgCloseMouseReleased(MouseEvent event) {
        close();
    }

    @FXML
    void imgCloseTouchReleased(TouchEvent event) {
        close();
    }
    public void close() {
        Stage stage = (Stage) btnDelete.getScene().getWindow();
        stage.setMaximized(false);
        stage.sizeToScene();
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
            stage.centerOnScreen();
            stage.setTitle("Main Logging");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Loading Error").showAndWait();
        }
        stage.show();
        stage.centerOnScreen();
    }


    @FXML
    void rbtLargeOnAction(ActionEvent event) {

    }

    @FXML
    void rbtSmallOnAction(ActionEvent event) {

    }

    @FXML
    void tblMemberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) btnDelete.fire();
    }

}
