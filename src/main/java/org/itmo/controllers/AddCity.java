package org.itmo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.itmo.App;
import org.itmo.entities.Cities;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.itmo.App.citiesRoutesManager;

public class AddCity implements Initializable {

    @FXML
    TextField townName;

    @FXML
    Button addButton;

    @FXML
    public void addData() throws SQLException, IOException {

        if (townName.getText() != null && !citiesRoutesManager.getCities().contains(townName.getText()))
        {
            citiesRoutesManager.addCity(new Cities(townName.getText()));

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
            warning.setHeaderText("Некоторые поля являются пустыми или такой город существует");
            warning.setContentText("Введите валидные данные.");
            warning.showAndWait();
        }
        App.setRoot("work_fxmls/table_routes");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}

