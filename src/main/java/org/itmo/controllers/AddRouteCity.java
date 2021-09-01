package org.itmo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.itmo.App;
import org.itmo.entities.CitiesRoutes;
import org.itmo.entities.Routes;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.itmo.App.citiesRoutesManager;

public class AddRouteCity implements Initializable {

    @FXML
    ComboBox<String> cityName;
    @FXML
    ComboBox<String> routeName;
    @FXML
    TextField sequence;

    @FXML
    public void addData() throws SQLException, IOException {

        if (routeName.getValue() != null && cityName.getValue() != null && sequence.getText() != null && !cityName.getSelectionModel().isEmpty() && !routeName.getSelectionModel().isEmpty())
        {
            CitiesRoutes value = new CitiesRoutes(cityName.getValue(), routeName.getValue(), Integer.parseInt(sequence.getText()));
            if (!citiesRoutesManager.getAllCitiesRoutes().contains(value))
            {
                citiesRoutesManager.addCityRouteConnection(value);
                Alert warning = new Alert(Alert.AlertType.INFORMATION);
                warning.setTitle("Оповещение");
                warning.setHeaderText("Операция была завершена успешно");
                warning.setContentText("Данные были добавлены.");
                warning.showAndWait();
            }
            else
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Ошибка");
                warning.setHeaderText("Такая запись уже существует");
                warning.setContentText("Введите корректные данные.");
                warning.showAndWait();
            }
        }
        else
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Ошибка");
            warning.setHeaderText("Некоторые поля являются пустыми");
            warning.setContentText("Введите данные.");
            warning.showAndWait();
        }
        App.setRoot("work_fxmls/table_routes");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> cityList = FXCollections.observableArrayList(citiesRoutesManager.getCities());
            ObservableList<String> routeList = FXCollections.observableArrayList(citiesRoutesManager.getRoutesNames());
            cityName.setItems(cityList);
            routeName.setItems(routeList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
