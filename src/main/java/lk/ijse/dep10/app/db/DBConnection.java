package lk.ijse.dep10.app.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection(){
        try {
            File file = new File("application.properties");
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Configuration does not exist").showAndWait();
                throw new RuntimeException(e);
            }
            Properties properties = new Properties();
            try {
                properties.load(fileReader);
                fileReader.close();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to read configurations").showAndWait();
                throw new RuntimeException(e);
            }

            String password = properties.getProperty("mysql.password","asdf123");
            String username = properties.getProperty("mysql.username","root");
            String database = properties.getProperty("mysql.database","dep10_restaurant");
            String host = properties.getProperty("mysql.host","dep10.lk");
            String port = properties.getProperty("mysql.port","3306");

            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?createDatabaseIfNotExist=true&allowMultiQueries=true", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to establish the database connection, try again. If the problem persist please contact the Technical Team").showAndWait();
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance() {
        return (dbConnection==null)? dbConnection =new DBConnection():dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
