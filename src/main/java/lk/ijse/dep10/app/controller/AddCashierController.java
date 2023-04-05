package lk.ijse.dep10.app.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.StaffMember;
import lk.ijse.dep10.app.util.Gender;
import lk.ijse.dep10.app.util.Status;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;

public class AddCashierController {

    public ToggleGroup tglGender;
    public ToggleGroup tglDesignation;
    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCashier;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView imgClose;

    @FXML
    private ImageView imgProfilePic;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblContact;

    @FXML
    private Label lblDesignation;

    @FXML
    private Label lblGender;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblRePassword;

    @FXML
    private Label lblUsername;

    @FXML
    private GridPane leftAnchor;

    @FXML
    private RadioButton rbtCashier;

    @FXML
    private RadioButton rbtFemale;

    @FXML
    private RadioButton rbtMale;

    @FXML
    private RadioButton rbtOwner;

    @FXML
    private AnchorPane rootRightStage2;

    @FXML
    private TableView<StaffMember> tblMember;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRePassowrd;
    private Blob picture1 = null;
    @FXML
    private TextField txtUsername;
    private String generatedID = "A000";
    private int idDigit = 0;

    public void initialize() {
        btnNewCashier.fire();
        tblMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("image"));
        tblMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblMember.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblMember.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("gender"));
        tblMember.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));
        loadData();

        tblMember.getSelectionModel().selectedItemProperty().addListener(((observableValue, previous, current) -> {
            btnDelete.setDisable(current == null);
            if (current == null) return;
//            tglDesignation.getSelectedToggle() == rbtOwner ? rbtOwner.setSelected(true) : rbtCashier.setSelected(true);
            Connection connection = DBConnection.getInstance().getConnection();
            // Retrieve the blob data from the database
            try {
                String sql2 ="SELECT * FROM Staff WHERE staff_id='%s'";
                sql2 = String.format(sql2, current.getId());
                ResultSet resultSet = connection.createStatement().executeQuery(sql2);
                resultSet.next();
                String userName = resultSet.getString("user_name");
                String password = resultSet.getString("password");
                txtPassword.setEditable(false);
                txtUsername.setEditable(false);
                txtRePassowrd.setEditable(false);
                txtPassword.setText(password);
                txtRePassowrd.setText(password);
                txtUsername.setText(userName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String sql = "SELECT picture FROM Picture WHERE id = '%s'";
            sql = String.format(sql, current.getId());
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    byte[] blobData = rs.getBytes("picture");
                    ByteArrayInputStream bis = new ByteArrayInputStream(blobData);
                    BufferedImage bufferedImage = ImageIO.read(bis);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imgProfilePic.setImage(image);
                    imageLoad();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            txtName.setText(current.getName());
            txtId.setText(current.getId());
            txtAddress.setText(current.getAddress());
            txtContact.setText(current.getContact());
            if(current.getStatus() == Status.OWNER) {
                rbtOwner.setSelected(true) ;
                rbtCashier.setSelected(false);
            } else {
                rbtCashier.setSelected(true);
                rbtOwner.setSelected(false) ;
            }
            if(current.getGender() == Gender.MALE) {
                rbtMale.setSelected(true) ;
                rbtFemale.setSelected(false);
            } else {
                rbtFemale.setSelected(true);
                rbtMale.setSelected(false) ;
            }

        }));
    }

    private void loadData() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Staff");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Picture WHERE id=?");
            while (resultSet.next()) {

                String id = resultSet.getString("staff_id");
                idDigit = Integer.parseInt(id.substring(2, id.length()));
                String name = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String contact = resultSet.getString("contact");
                Gender gender = Gender.valueOf(resultSet.getString("gender"));
                Status designation = Status.valueOf(resultSet.getString("status"));


                Blob picture = null;

                preparedStatement.setString(1, id);
                ResultSet resultPicture = preparedStatement.executeQuery();

                if (resultPicture.next()) {
                    picture = resultPicture.getBlob("picture");
                }
                ImageView imageView = new ImageView(new Image(picture.getBinaryStream(), 50, 50, true, true));
                StaffMember staffMember = new StaffMember(id, name, address, contact, gender, designation, imageView);
                tblMember.getItems().add(staffMember);
            }
            generatedID = String.format("A-%03d", ++idDigit);
            resultSet.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database connection lost").showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void btnBrowseOnAction(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the Student picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toURL().toString());
            imgProfilePic.setImage(image);

        }
        imageLoad();
    }

    private void imageLoad() {
        Image image = imgProfilePic.getImage();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            picture1 = new SerialBlob(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        deleteFromDatabase();
        tblMember.getItems().remove(tblMember.getSelectionModel().getSelectedItem());
        tblMember.getSelectionModel().clearSelection();
    }

    private void deleteFromDatabase() {
        Connection connection = DBConnection.getInstance().getConnection();
        String selectedStudentName = tblMember.getSelectionModel().getSelectedItem().getId();
        String sql = "DELETE FROM Picture WHERE id='%s'";
        sql = String.format(sql, selectedStudentName);
        String sql1 = "DELETE FROM Staff WHERE staff_id='%s'";
        sql1 = String.format(sql1, selectedStudentName);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.executeUpdate(sql1);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to delete from database").showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void btnNewCashierOnAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtPassword.clear();
        txtRePassowrd.clear();
        txtUsername.clear();
        tglGender.selectToggle(null);
        tglDesignation.selectToggle(null);
        tblMember.getSelectionModel().clearSelection();
        txtId.requestFocus();
        Image image = new Image("images/background/AddNewMember/No-Image.jpg");
        imgProfilePic.setImage(image);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isValid()) return;
        try {
            Image image = imgProfilePic.getImage();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            picture1 = new SerialBlob(bytes);
            ImageView imageView = new ImageView(new Image(picture1.getBinaryStream(), 50, 50, true, true));

            StaffMember staffMember = new StaffMember((txtId.getText()), txtName.getText(), txtAddress.getText(), txtContact.getText(),
                    tglGender.getSelectedToggle() == rbtMale ? Gender.MALE : Gender.FEMALE, tglDesignation.getSelectedToggle() == rbtOwner ? Status.OWNER : Status.CASHIER, imageView);

            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            StaffMember selectedStudent = tblMember.getSelectionModel().getSelectedItem();

            if (selectedStudent == null) {
                String sql = "INSERT INTO Staff VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s', '%s')";
                sql = String.format(sql, staffMember.getId(), staffMember.getName(), staffMember.getAddress(), staffMember.getContact(), txtUsername.getText(),
                        txtPassword.getText(), staffMember.getGender(), staffMember.getStatus());
                stm.executeUpdate(sql);

                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Picture (picture,id ) VALUES (?,?)");
                preparedStatement1.setBlob(1, picture1);
                preparedStatement1.setString(2, generatedID);

                preparedStatement1.executeUpdate();
                tblMember.getItems().add(staffMember);
                idDigit++;
            } else {
                StaffMember selectedItem = tblMember.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String sql = "UPDATE Staff SET full_name='%s', address='%s', contact='%s', gender='%s' ,status='%s' WHERE staff_id='%s'";
                sql = String.format(sql, staffMember.getName(), staffMember.getAddress(), staffMember.getContact(), staffMember.getGender(), staffMember.getStatus(),staffMember.getId());
                stm.executeUpdate(sql);
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Picture SET picture=? WHERE id=?");
                preparedStatement.setBlob(1, picture1);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                ObservableList<StaffMember> studentList = tblMember.getItems();
                int selectedStudentIndex = studentList.indexOf(selectedStudent);
                studentList.set(selectedStudentIndex, staffMember);
                tblMember.refresh();
            }

            btnNewCashier.fire();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the student, try again!").show();
        }
    }

    private boolean isValid() {
        boolean isDataValid = true;

        for (Node node : new Node[]{txtId, txtName, txtAddress, txtContact, txtUsername, txtPassword, txtRePassowrd}) {
            node.getStyleClass().remove("invalid");
        }
        rbtCashier.getStyleClass().remove("invalid");
        rbtOwner.getStyleClass().remove("invalid");
        rbtFemale.getStyleClass().remove("invalid");
        rbtMale.getStyleClass().remove("invalid");

        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String rePassword = txtRePassowrd.getText();
        Toggle toggleGender = tglGender.getSelectedToggle();
        Toggle toggleDesignation = tglDesignation.getSelectedToggle();

        if (toggleGender == null) {
            isDataValid = false;
            rbtMale.requestFocus();
            rbtFemale.getStyleClass().add("invalid");
            rbtMale.getStyleClass().add("invalid");
        }
        if (toggleDesignation == null) {
            isDataValid = false;
            rbtOwner.requestFocus();
            rbtOwner.getStyleClass().add("invalid");
            rbtCashier.getStyleClass().add("invalid");
        }

        if (name.isEmpty() || !name.matches("[A-Za-z ]+")) {
            isDataValid = false;
            txtName.requestFocus();
            txtName.selectAll();
            txtName.getStyleClass().add("invalid");
        }
        if (address.strip().length() < 3) {
            isDataValid = false;
            txtAddress.requestFocus();
            txtAddress.selectAll();
            txtAddress.getStyleClass().add("invalid");
        }
        if (!contact.matches("^0[1-9]{2}-[0-9]{7}")) {
            isDataValid = false;
            txtContact.requestFocus();
            txtContact.selectAll();
            txtContact.getStyleClass().add("invalid");
        }

        if (username.isEmpty()) {
            isDataValid = false;
            txtUsername.requestFocus();
            txtUsername.selectAll();
            txtUsername.getStyleClass().add("invalid");
        }

        if (!password.matches("([A-Z]+)") && !password.matches("[0-9]+") && !password.matches("[@#!%&*]+") && password.length() < 6) {
            isDataValid = false;
            txtPassword.requestFocus();
            txtPassword.selectAll();
            txtPassword.getStyleClass().add("invalid");
        }
        if (!rePassword.equals(password)) {
            System.out.println(rePassword);
            isDataValid = false;
            txtRePassowrd.requestFocus();
            txtRePassowrd.selectAll();
            txtRePassowrd.getStyleClass().add("invalid");
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

    private void close() {
        Stage stage = (Stage) btnBrowse.getScene().getWindow();
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
    void rbtOwnerOnAction(ActionEvent event) {
        generatedID = String.format("A-%03d", idDigit);
        txtId.setText(generatedID);
    }

    @FXML
    void rbtCashierOnAction(ActionEvent event) {
        generatedID = String.format("C-%03d", idDigit);
        txtId.setText(generatedID);
    }

    @FXML
    void rbtFemaleOnAction(ActionEvent event) {

    }

    @FXML
    void rbtMaleOnAction(ActionEvent event) {

    }

    public void tblMemberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) btnDelete.fire();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        Image image = new Image("/image/profile-user.png");
        imgProfilePic.setImage(image);
    }
}
