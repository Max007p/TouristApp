package org.itmo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.itmo.App;
import org.itmo.entities.Voyage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static org.itmo.App.voyageEntityManager;

public class AddVoyage implements Initializable {

    @FXML
    DatePicker startDate;
    @FXML
    DatePicker finishDate;
    @FXML
    TextField passengerNumber;
    @FXML
    TextField ticketCost;
    @FXML
    ComboBox<String> routeName;
    @FXML
    ComboBox<Integer> busNumber;
    @FXML
    Button addButton;

    @FXML
    public void addData() throws SQLException, IOException {

        if (startDate.getValue() != null && finishDate.getValue() != null && passengerNumber.getText() != null && ticketCost.getText() != null && !routeName.getSelectionModel().isEmpty() && !busNumber.getSelectionModel().isEmpty())
        {
            voyageEntityManager.add(new Voyage(Date.valueOf(startDate.getValue()), Date.valueOf(finishDate.getValue()), Integer.parseInt(passengerNumber.getText()), Integer.parseInt(ticketCost.getText()), routeName.getValue(), busNumber.getValue(), false, null));

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
        App.setRoot("work_fxmls/table_voyages");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDate.setShowWeekNumbers(true);
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null)
                {
                    return dateFormatter.format(localDate);
                }
                else
                {
                    return  "";
                }
            }

            @Override
            public LocalDate fromString(String s) {
                if (s != null && !s.isEmpty())
                {
                    return LocalDate.parse(s, dateFormatter);
                }
                else
                {
                    return null;
                }
            }
        };
        startDate.setConverter(converter);
        finishDate.setConverter(converter);

        try {
            ObservableList<String> routeList = FXCollections.observableArrayList(voyageEntityManager.getRoutes());
            routeName.setItems(routeList);
            ObservableList<Integer> busList = FXCollections.observableArrayList(voyageEntityManager.getBuses());
            busNumber.setItems(busList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
