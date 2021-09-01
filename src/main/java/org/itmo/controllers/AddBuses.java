package org.itmo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.itmo.App;
import org.itmo.entities.Bus;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.itmo.App.busesEntityManager;

public class AddBuses implements Initializable {

    @FXML
    TextField number;
    @FXML
    TextField totalRun;
    @FXML
    ComboBox<String> status;
    @FXML
    ComboBox<String> codeModel;

    @FXML
    public void addData() throws SQLException, IOException {

        if (status.getValue() != null && number.getText() != null && totalRun.getText() != null && !status.getSelectionModel().isEmpty())
        {
            busesEntityManager.addBus(new Bus(Integer.parseInt(number.getText()), Integer.parseInt(totalRun.getText()), status.getValue(), codeModel.getValue()));

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
        App.setRoot("work_fxmls/profile_worker");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> statusList = FXCollections.observableArrayList();
            statusList.add("Готов");
            statusList.add("Не готов");
            status.setItems(statusList);
            ObservableList<String> codeList = FXCollections.observableArrayList(busesEntityManager.getCodeModels());
            codeModel.setItems(codeList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
