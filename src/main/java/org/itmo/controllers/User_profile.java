package org.itmo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.itmo.entities.PaidPassengers;
import org.itmo.entities.Passenger;
import org.itmo.entities.Voyage;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static org.itmo.App.*;

public class User_profile implements Initializable {

    private static Passenger passengerUser;

    static {
        try {
            passengerUser = passengersEntityManager.getByLogin(authedUser.getUsername());
            if (passengerUser == null)
            {
                passengersEntityManager.add(new Passenger(authedUser));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    TabPane backgroundPane;

    @FXML
    public Label userName;

    @FXML
    public Label accountType;

    @FXML
    public TableView<PaidPassengers> allWays;

    @FXML
    public TableColumn<PaidPassengers, Integer> idUser;

    @FXML
    public TableColumn<PaidPassengers, String> nick;

    @FXML
    public TableColumn<PaidPassengers, Integer> idWay;

    @FXML
    public TableColumn<PaidPassengers, Boolean> isPayed;

    @FXML
    public ScrollPane scrollVoyages;

    @FXML
    public FlowPane listOfVoyages;

    public User_profile() throws SQLException {
    }

    public void constructFirstTab() throws SQLException {
        idUser.setCellValueFactory(new PropertyValueFactory<PaidPassengers, Integer>("id"));
        nick.setCellValueFactory(new PropertyValueFactory<PaidPassengers, String>("passengerUser"));
        idWay.setCellValueFactory(new PropertyValueFactory<PaidPassengers, Integer>("idWay"));
        isPayed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PaidPassengers, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PaidPassengers, Boolean> voyageBooleanCellDataFeatures) {
                PaidPassengers voyage = voyageBooleanCellDataFeatures.getValue();

                SimpleBooleanProperty prop = new SimpleBooleanProperty(voyage.getPayed());
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

        fillTheTable();
        allWays.setEditable(false);
    }

    public void fillTheTable() throws SQLException {
        if (paidPassengers.getByUsername(passengerUser.getUsername()) != null)
        {
            ObservableList<PaidPassengers> passengers = FXCollections.observableArrayList(paidPassengers.getByUsername(passengerUser.getUsername()));
            allWays.refresh();
            allWays.setItems(passengers);
        }
    }

    public void constructSecondTab()
    {
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
            box.setPrefWidth(500);
            box.setBackground(new Background(new BackgroundFill(Color.rgb(253, 200, 230), new CornerRadii(10), new Insets(0))));
            box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
            box.setSpacing(10);
            box.setPadding(new Insets(10));
            box.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY)
                    {
                        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                        warning.setTitle("Подтверждение");
                        warning.setHeaderText("Вы хотите забронировать место на данный тур?");
                        var buttonType = warning.showAndWait();

                        if (buttonType.isPresent() || buttonType.get() == ButtonType.OK)
                        {
                            ObservableList<Node> nodes = box.getChildren();
                            Node label = nodes.get(7);
                            String id = label.toString().substring(57, label.toString().length() - 1);
                            try {
                                paidPassengers.addOnline(new PaidPassengers(passengerUser.getUsername(), Integer.parseInt(id), false, 0, passengerUser.getName(), passengerUser.getPassport()));
                                fillTheTable();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                }
            });
            listOfVoyages.getChildren().add(box);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundPane.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(10), new Insets(-5))));
        userName.setText(passengerUser.getUsername());
        accountType.setText("Пользователь");
        accountType.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), new Insets(-5))));
        try {
            constructFirstTab();
            constructSecondTab();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
