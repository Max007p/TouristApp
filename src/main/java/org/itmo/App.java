package org.itmo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.User;
import org.itmo.manager.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static final MySqlDatabase database = new MySqlDatabase("127.0.0.1", "mydb", "root", "1701002mp");
    public static UserEntityManager userEntityManager = new UserEntityManager(database);
    public static StaffEntityManager staffEntityManager = new StaffEntityManager(database);
    public static VoyageEntityManager voyageEntityManager = new VoyageEntityManager(database);
    public static CitiesRoutesManager citiesRoutesManager = new CitiesRoutesManager(database);
    public static BusesEntityManager busesEntityManager = new BusesEntityManager(database);
    public static PaidPassengersManager paidPassengers = new PaidPassengersManager(database);
    public static PassengersEntityManager passengersEntityManager = new PassengersEntityManager(database);
    public static User authedUser;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        scene = new Scene(loadFXML("common_fxmls/entry"), 1000, 720);
        stage.setScene(scene);
        stage.setTitle("Туристическое бюро");
        stage.show();

        if (database.getConnection() == null)
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Отсутствие подключения");
            warning.setHeaderText("Результат:");
            warning.setContentText("Отсутствует подключение к базе данных.");
            warning.showAndWait();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}