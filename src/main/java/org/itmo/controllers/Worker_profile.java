package org.itmo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.itmo.App;
import org.itmo.entities.PaidPassengers;
import org.itmo.entities.Staff;
import org.itmo.entities.Voyage;
import org.itmo.manager.StaffEntityManager;
import org.itmo.manager.VoyageEntityManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.itmo.App.*;

public class Worker_profile implements Initializable
{

    private static Staff staffUser;

    static {
        try {
            staffUser = new Staff(userEntityManager.getByLogin(authedUser.getUsername()));
        } catch (SQLException throwables) {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Ошибка: " + throwables.getErrorCode());
            warning.setHeaderText("Такой пользователь не существует.");
            warning.setContentText("Введите другой ник.");
            warning.showAndWait();
        }
    }

    @FXML
    public void toVoyages() throws IOException {
        App.setRoot("work_fxmls/table_voyages");
    }

    @FXML
    public void toRoutes() throws IOException {
        App.setRoot("work_fxmls/table_routes");
    }

    @FXML
    public void toBuses() throws IOException {
        App.setRoot("work_fxmls/table_buses");
    }



    @FXML
    public TabPane backgroundPane;

    @FXML
    public Label userName;

    @FXML
    public Label accountType;

    @FXML
    public FlowPane listOfVoyages;

    @FXML
    public ScrollPane scrollVoyages;

    ////////////////////////////////

    @FXML
    TableView<PaidPassengers> tablePassengers;

    @FXML
    TableColumn<PaidPassengers, Integer> idPassenger;
    @FXML
    TableColumn<PaidPassengers, String> nickPassenger;
    @FXML
    TableColumn<PaidPassengers, Integer> idWay;
    @FXML
    TableColumn<PaidPassengers, String> stuffTableNumber;
    @FXML
    TableColumn<PaidPassengers, String> namePassenger;
    @FXML
    TableColumn<PaidPassengers, Boolean> isPayed;
    @FXML
    TableColumn<PaidPassengers, String> passportColumn;

    ////////////////////////////

    @FXML
    TextField clientName;
    @FXML
    ComboBox<Integer> routeId;
    @FXML
    TextField passportId;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seeAllVoyagesTab();
        try {
            seeUserOffers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void refreshButton() throws SQLException {
        ObservableList<Integer> routeList = FXCollections.observableArrayList(voyageEntityManager.getIds());
        routeId.setItems(routeList);
        ObservableList<PaidPassengers> list = FXCollections.observableArrayList(paidPassengers.getAll());
        tablePassengers.refresh();
        tablePassengers.setItems(list);
    }

    @FXML
    public void addPassenger() throws SQLException {
        if (clientName.getText() != null && routeId.getValue() != null && passportId.getText() != null)
        {
            paidPassengers.addLocal(new PaidPassengers(null, routeId.getValue(), false, staffUser.getTableNumber(), clientName.getText(), passportId.getText()));

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
        clientName.clear();
        passportId.clear();
        refreshButton();
    }

    public void seeUserOffers() throws SQLException {
        idPassenger.setCellValueFactory(new PropertyValueFactory<PaidPassengers, Integer>("id"));
        nickPassenger.setCellValueFactory(new PropertyValueFactory<PaidPassengers, String>("passengerUser"));
        idWay.setCellValueFactory(new PropertyValueFactory<PaidPassengers, Integer>("idWay"));
        stuffTableNumber.setCellValueFactory(new PropertyValueFactory<PaidPassengers, String>("tableNumber"));
        namePassenger.setCellValueFactory(new PropertyValueFactory<PaidPassengers, String>("customerName"));
        isPayed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PaidPassengers, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PaidPassengers, Boolean> voyageBooleanCellDataFeatures) {
                PaidPassengers voyage = voyageBooleanCellDataFeatures.getValue();

                SimpleBooleanProperty prop = new SimpleBooleanProperty(voyage.getPayed());

                prop.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        voyage.setPayed(t1);
                        try {
                            paidPassengers.updatePassengersByEntity(voyage);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                return prop;
            }
        });
        isPayed.setCellFactory(new Callback<TableColumn<PaidPassengers, Boolean>, TableCell<PaidPassengers, Boolean>>() {
            @Override
            public TableCell<PaidPassengers, Boolean> call(TableColumn<PaidPassengers, Boolean> voyageBooleanTableColumn) {
                CheckBoxTableCell<PaidPassengers, Boolean> cell = new CheckBoxTableCell<PaidPassengers, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        passportColumn.setCellValueFactory(new PropertyValueFactory<PaidPassengers, String>("passport"));
        tablePassengers.setEditable(true);

        refreshButton();
    }

    private void seeAllVoyagesTab() {
        backgroundPane.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(10), new Insets(-5))));
        userName.setText(staffUser.getUsername());
        accountType.setText("Администратор");
        accountType.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(10), new Insets(-5))));

        scrollVoyages.setStyle("-fx-background-color:transparent;");
        scrollVoyages.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollVoyages.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollVoyages.setPrefHeight(530);

        listOfVoyages.setOrientation(Orientation.HORIZONTAL);
        listOfVoyages.setPrefWidth(scrollVoyages.getPrefWidth());
        listOfVoyages.setPrefHeight(scrollVoyages.getPrefHeight());
        listOfVoyages.setVgap(20);
        listOfVoyages.setPadding(new Insets(20));

        List<Voyage> voyage = new ArrayList<>();
        try {
            voyage.addAll(voyageEntityManager.getAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (int i = 0; i < voyage.size(); i++) {
            Label startDate = new Label("Дата старта: " + voyage.get(i).getStartDate().toString());
            startDate.setFont(new Font("Arial", 16));

            Label finishDate = new Label("Дата прибытия: " + voyage.get(i).getFinishDate().toString());
            finishDate.setFont(new Font("Arial", 16));

            Label passengers = new Label("Колличество пассажиров: " + String.valueOf(voyage.get(i).getPassengers()));
            passengers.setFont(new Font("Arial", 16));

            Label ticketCost = new Label("Стоимость билета: " + String.valueOf(voyage.get(i).getTicketCost()) + " рублей");
            ticketCost.setFont(new Font("Arial", 16));

            Label routesName = new Label("Название маршрута: " + voyage.get(i).getRouteName());
            routesName.setFont(new Font("Arial", 16));

            Label busesNumber = new Label("Номер автобуса: " + String.valueOf(voyage.get(i).getBusNumber()));
            busesNumber.setFont(new Font("Arial", 16));

            CheckBox isDone = new CheckBox("Завершенность");
            isDone.setFont(new Font("Arial", 16));
            isDone.setSelected(voyage.get(i).isDoneFlag());
            isDone.setDisable(true);

            Label idWay = new Label("Идентификатор маршрута: " + String.valueOf(voyage.get(i).getIdWay()));
            idWay.setFont(new Font("Arial", 16));

            Label finishDateFact = new Label();
            if (voyage.get(i).getFactFinish() != null) {
                finishDateFact.setText("Дата фактического прибытия: " + voyage.get(i).getFactFinish().toString());
            } else {
                finishDateFact.setText("Не определено фактическое прибытие");
            }
            finishDateFact.setFont(new Font("Arial", 16));

            VBox box = new VBox(startDate, finishDate, passengers, ticketCost, routesName, busesNumber, isDone, idWay, finishDateFact);
            box.setPrefWidth(675);
            box.setBackground(new Background(new BackgroundFill(Color.rgb(253, 245, 230), new CornerRadii(10), new Insets(0))));
            box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
            box.setSpacing(10);
            box.setPadding(new Insets(10));
            listOfVoyages.getChildren().add(box);
        }

    }
}
