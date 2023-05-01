package lk.ijse.dep10.app.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.StaffMember;
import lk.ijse.dep10.app.util.Gender;
import lk.ijse.dep10.app.util.Status;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;

public class AddOwnerController extends LoggedUserDetails {


    public Label lblName;
    public Label lblAddress;
    public TextField txtAddress;
    public Label lblContact;
    public TextField txtContact;
    public Label lblUsername;
    public Label lblPassword;
    public Label lblRePassword;
    public Label lblGender;
    public RadioButton rbtMale;
    public ToggleGroup tglGender;
    public ImageView imgProfilePic;
    public RadioButton rbtFemale;
    public Button btnBrowse;
    public Button btnClear;
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
        stage.setMaximized(true);
        stage.sizeToScene();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
        stage.setTitle("Shop Owner Mode");
        stage.centerOnScreen();
        stage.show();


    }

    private boolean isValid(){
        boolean isDataValid = true;

        for (Node node : new Node[]{ txtAdminName, txtAddress, txtContact, txtAdminUsername, txtAdminPassword, txtRePassword}) {
            node.getStyleClass().remove("invalid");
        }
        rbtFemale.getStyleClass().remove("invalid");
        rbtMale.getStyleClass().remove("invalid");

        String name = txtAdminName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String username = txtAdminUsername.getText();
        String password = txtAdminPassword.getText();
        String rePassword = txtRePassword.getText();
        Toggle toggleGender = tglGender.getSelectedToggle();


        if (toggleGender == null) {
            isDataValid = false;
            rbtMale.requestFocus();
            rbtFemale.getStyleClass().add("invalid");
            rbtMale.getStyleClass().add("invalid");
        }
        if (!rePassword.equals(password)) {
            System.out.println(rePassword);
            isDataValid = false;
            txtRePassword.requestFocus();
            txtRePassword.selectAll();
            txtRePassword.getStyleClass().add("invalid");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$") ) {
            isDataValid = false;
            txtAdminPassword.requestFocus();
            txtAdminPassword.selectAll();
            txtAdminPassword.getStyleClass().add("invalid");
        }

        if (username.isEmpty()) {
            isDataValid = false;
            txtAdminUsername.requestFocus();
            txtAdminUsername.selectAll();
            txtAdminUsername.getStyleClass().add("invalid");
        }
        if (!contact.matches("^0[1-9]{2}-[0-9]{7}")) {
            isDataValid = false;
            txtContact.requestFocus();
            txtContact.selectAll();
            txtContact.getStyleClass().add("invalid");
        }
        if (address.strip().length() < 3) {
            isDataValid = false;
            txtAddress.requestFocus();
            txtAddress.selectAll();
            txtAddress.getStyleClass().add("invalid");
        }
        if (name.isEmpty() || !name.matches("[A-Za-z ]+")) {
            isDataValid = false;
            txtAdminName.requestFocus();
            txtAdminName.selectAll();
            txtAdminName.getStyleClass().add("invalid");
        }
        return isDataValid;
    }

    private void addOwnerDatabase() {

        try {
            Image image = imgProfilePic.getImage();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
           Blob picture1 = new SerialBlob(bytes);
            ImageView imageView = new ImageView(new Image(picture1.getBinaryStream(), 50, 50, true, true));
            StaffMember staffMember = new StaffMember("A-001",  txtAdminName.getText(), txtAddress.getText(), txtContact.getText(),
                    tglGender.getSelectedToggle() == rbtMale ? Gender.MALE : Gender.FEMALE, Status.OWNER, imageView);

            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String sql = "INSERT INTO Staff VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s', '%s')";
            sql = String.format(sql, staffMember.getId(), staffMember.getName(), staffMember.getAddress(), staffMember.getContact(), txtAdminUsername.getText(),
                    txtAdminPassword.getText(), staffMember.getGender(), staffMember.getStatus());
            stm.executeUpdate(sql);
            loggedName=staffMember.getName();
            loggedId="A-001";
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Picture (picture,id ) VALUES (?,?)");
            preparedStatement1.setBlob(1, picture1);
            preparedStatement1.setString(2, "A-001");

            preparedStatement1.executeUpdate();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Unable to Add Owner to Database").showAndWait();
            e.printStackTrace();
        }
    }


    public void rbtMaleOnAction(ActionEvent actionEvent) {
    }

    public void rbtFemaleOnAction(ActionEvent actionEvent) {
    }

    public void btnBrowseOnAction(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the Student picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toURL().toString());
            imgProfilePic.setImage(image);
        }

    }


    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    public void btnComfirmKeyRelease(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) btnConfirm.fire();
    }

}
