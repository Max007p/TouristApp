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
import org.itmo.entities.ModuleInfo;
import org.itmo.entities.Voyage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static org.itmo.App.busesEntityManager;
import static org.itmo.App.voyageEntityManager;

public class AddBusesInfo implements Initializable {

    @FXML
    DatePicker constructDate;
    @FXML
    TextField name;
    @FXML
    TextField manufacturer;
    @FXML
    TextField floors;
    @FXML
    TextField seats;
    @FXML
    CheckBox toilet;
    @FXML
    TextField codeModel;
    @FXML
    Button addButton;

    @FXML
    public void addData() throws SQLException, IOException {

        if (manufacturer.getText() != null && name.getText() != null && !codeModel.getText().isEmpty() && seats.getText() != null)
        {
            busesEntityManager.addBusInfo(new ModuleInfo(name.getText(), toilet.isSelected(), manufacturer.getText(), Integer.parseInt(floors.getText()), Integer.parseInt(seats.getText()), Date.valueOf(constructDate.getValue()), codeModel.getText()));

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
        App.setRoot("work_fxmls/table_buses");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        constructDate.setShowWeekNumbers(true);
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
        constructDate.setConverter(converter);
    }


}
