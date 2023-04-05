package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                if (DBConnection.getInstance().getConnection() != null && !DBConnection.getInstance().getConnection().isClosed()) {
                    System.out.println("Database connection is about to close");
                    DBConnection.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateDatabase();
        ResultSet resultSet=null;
        try {
            resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Staff WHERE status ='OWNER'");
            if (resultSet.next()) {
                System.out.println(resultSet.next());
//                primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainLogging.fxml")).load()));
//                primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/OwnerScene.fxml")).load()));
                primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddCashier.fxml")).load()));
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Main Logging");
                primaryStage.setResizable(false);
                primaryStage.show();
            } else {
                System.out.println(resultSet.next());
                primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/AddOwner.fxml")).load()));
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Create Admin");
                primaryStage.setResizable(false);
                primaryStage.show();
            }
        } catch(SQLException e){
                new Alert(Alert.AlertType.ERROR, "Database Connection Error").showAndWait();
                e.printStackTrace();
            }

        }



    public void generateDatabase() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            InputStream resourceAsStream = getClass().getResourceAsStream("/schema.sql");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            statement.execute(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to generate database tables");
        }
    }
}
