package org.itmo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.itmo.App;
import org.itmo.entities.Routes;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.itmo.App.citiesRoutesManager;

public class AddRoute implements Initializable {

    @FXML
    TextField routeName;
    @FXML
    TextField duration;
    @FXML
    ComboBox<String> startCity;
    @FXML
    ComboBox<String> finishCity;
    @FXML
    Button addButton;

    @FXML
    public void addData() throws SQLException, IOException {

        if (duration.getText() != null && routeName.getText() != null && !startCity.getSelectionModel().isEmpty() && !finishCity.getSelectionModel().isEmpty() && !citiesRoutesManager.getRoutesNames().contains(routeName.getText()))
        {
            citiesRoutesManager.addRoute(new Routes(routeName.getText(), startCity.getValue(), finishCity.getValue(), Integer.parseInt(duration.getText())));

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
            startCity.setItems(cityList);
            finishCity.setItems(cityList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
