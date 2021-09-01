package org.itmo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.itmo.App;
import org.itmo.entities.Voyage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static org.itmo.App.voyageEntityManager;


public class VoyageTable implements Initializable {
    private ObservableList<Voyage> data = FXCollections.observableArrayList(getAll());
    @FXML
    TableColumn<Voyage, Integer> columnIdVoyage;
    @FXML
    TableColumn<Voyage, Date> columnStartVoyage;
    @FXML
    TableColumn<Voyage, Date> columnFinishVoyage;
    @FXML
    TableColumn<Voyage, Integer> columnPassengersVoyage;
    @FXML
    TableColumn<Voyage, Integer> columnTicketVoyage;
    @FXML
    TableColumn<Voyage, String> columnRouteVoyage;
    @FXML
    TableColumn<Voyage, Integer> columnBusVoyage;
    @FXML
    TableColumn<Voyage, Boolean> columnDoneVoyage;
    @FXML
    TableColumn<Voyage, Date> columnFactVoyage;
    @FXML
    TableView<Voyage> voyageTable;
    @FXML
    Button backButton;
    @FXML
    Button addButton;

    @FXML
    public void addRow() throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_voyage"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Ввод новой поездки");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void backToMain() throws IOException {
        App.setRoot("work_fxmls/profile_worker");
    }

    public VoyageTable() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            makeAllEditable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<Voyage> getAll() throws SQLException {
        return voyageEntityManager.getAll();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void makeAllEditable() throws SQLException {
        columnIdVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Integer>("idWay"));
        columnStartVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("startDate"));
        columnFinishVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("finishDate"));
        columnPassengersVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Integer>("passengers"));
        columnTicketVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Integer>("ticketCost"));

        ObservableList<String> routeList = FXCollections.observableArrayList(voyageEntityManager.getRoutes());

        columnRouteVoyage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Voyage, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Voyage, String> voyageStringCellDataFeatures) {
                Voyage voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getRouteName();
                return new SimpleObjectProperty<String>(route);
            }
        });

        ObservableList<Integer> busList = FXCollections.observableArrayList(voyageEntityManager.getBuses());

        columnBusVoyage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Voyage, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Voyage, Integer> voyageIntegerCellDataFeatures) {
                Voyage voyage = voyageIntegerCellDataFeatures.getValue();

                Integer busNumber = voyage.getBusNumber();
                return new SimpleObjectProperty<Integer>(busNumber);
            }
        });

        columnDoneVoyage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Voyage, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Voyage, Boolean> voyageBooleanCellDataFeatures) {
                Voyage voyage = voyageBooleanCellDataFeatures.getValue();

                SimpleBooleanProperty prop = new SimpleBooleanProperty(voyage.isDoneFlag());

                prop.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        voyage.setDoneFlag(t1);
                        try {
                            voyageEntityManager.updateByEntity(voyage);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                return prop;
            }
        });
        columnFactVoyage.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("factFinish"));

        columnStartVoyage.setCellFactory(new Callback<TableColumn<Voyage, Date>, TableCell<Voyage, Date>>() {
            @Override
            public TableCell<Voyage, Date> call(TableColumn<Voyage, Date> voyageDateTableColumn) {
                TableCell<Voyage, Date> cell = new TableCell<Voyage, Date>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(Date date, boolean b) {

                        super.updateItem(date, b);
                        if (b)
                        {
                            this.setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            if(isEditing())
                            {
                                if (textField != null)
                                {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                            else
                            {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null)
                        {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Date.valueOf(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        }
                        else
                        {
                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();

                        setText(String.valueOf(getItem()));
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                };
                return cell;
            }
        });
        columnStartVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Date> voyageDateCellEditEvent) {
                TablePosition<Voyage, Date> pos = voyageDateCellEditEvent.getTablePosition();

                Date stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setStartDate(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnFinishVoyage.setCellFactory(new Callback<TableColumn<Voyage, Date>, TableCell<Voyage, Date>>() {
            @Override
            public TableCell<Voyage, Date> call(TableColumn<Voyage, Date> voyageDateTableColumn) {
                TableCell<Voyage, Date> cell = new TableCell<Voyage, Date>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(Date date, boolean b) {

                        super.updateItem(date, b);
                        if (b)
                        {
                            this.setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            if(isEditing())
                            {
                                if (textField != null)
                                {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                            else
                            {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null)
                        {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Date.valueOf(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        }
                        else
                        {
                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();

                        setText(String.valueOf(getItem()));
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                };
                return cell;
            }
        });
        columnFinishVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Date> voyageDateCellEditEvent) {
                TablePosition<Voyage, Date> pos = voyageDateCellEditEvent.getTablePosition();

                Date stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setFinishDate(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnPassengersVoyage.setCellFactory(new Callback<TableColumn<Voyage, Integer>, TableCell<Voyage, Integer>>() {
            @Override
            public TableCell<Voyage, Integer> call(TableColumn<Voyage, Integer> voyageDateTableColumn) {
                TableCell<Voyage, Integer> cell = new TableCell<Voyage, Integer>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b)
                        {
                            this.setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            if(isEditing())
                            {
                                if (textField != null)
                                {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                            else
                            {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null)
                        {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.valueOf(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();
                                    }
                                }
                            });
                        }
                        else
                        {
                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();

                        setText(String.valueOf(getItem()));
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                };
                return cell;
            }
        });
        columnPassengersVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Integer> voyageDateCellEditEvent) {
                TablePosition<Voyage, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setPassengers(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnTicketVoyage.setCellFactory(new Callback<TableColumn<Voyage, Integer>, TableCell<Voyage, Integer>>() {
            @Override
            public TableCell<Voyage, Integer> call(TableColumn<Voyage, Integer> voyageDateTableColumn) {
                TableCell<Voyage, Integer> cell = new TableCell<Voyage, Integer>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b)
                        {
                            this.setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            if(isEditing())
                            {
                                if (textField != null)
                                {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                            else
                            {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null)
                        {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.valueOf(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();
                                    }
                                }
                            });
                        }
                        else
                        {
                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();

                        setText(String.valueOf(getItem()));
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                };
                return cell;
            }
        });
        columnTicketVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Integer> voyageDateCellEditEvent) {
                TablePosition<Voyage, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setTicketCost(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnRouteVoyage.setCellFactory(ComboBoxTableCell.forTableColumn(routeList));
        columnRouteVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, String> voyageStringCellEditEvent) {
                TablePosition<Voyage, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setRouteName(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnBusVoyage.setCellFactory(ComboBoxTableCell.forTableColumn(busList));
        columnBusVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Integer> voyageDateCellEditEvent) {
                TablePosition<Voyage, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setBusNumber(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnDoneVoyage.setCellFactory(new Callback<TableColumn<Voyage, Boolean>, TableCell<Voyage, Boolean>>() {
            @Override
            public TableCell<Voyage, Boolean> call(TableColumn<Voyage, Boolean> voyageBooleanTableColumn) {
                CheckBoxTableCell<Voyage, Boolean> cell = new CheckBoxTableCell<Voyage, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        // Для чекбоксов прописывать setOnEditCommit не нужно;

        columnFactVoyage.setCellFactory(new Callback<TableColumn<Voyage, Date>, TableCell<Voyage, Date>>() {
            @Override
            public TableCell<Voyage, Date> call(TableColumn<Voyage, Date> voyageDateTableColumn) {
                TableCell<Voyage, Date> cell = new TableCell<Voyage, Date>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(Date date, boolean b) {

                        super.updateItem(date, b);
                        if (b)
                        {
                            this.setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            if(isEditing())
                            {
                                if (textField != null)
                                {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                            else
                            {
                                if (date != null)
                                {
                                    setText(date.toString());
                                }
                                else
                                {
                                    setText("Не назначено");
                                }
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null)
                        {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Date.valueOf(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        }
                        else
                        {
                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();

                        setText(String.valueOf(getItem()));
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                };
                return cell;
            }
        });
        columnFactVoyage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Voyage, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Voyage, Date> voyageDateCellEditEvent) {
                TablePosition<Voyage, Date> pos = voyageDateCellEditEvent.getTablePosition();

                Date stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Voyage voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setFactFinish(stroka);
                try {
                    voyageEntityManager.updateByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnIdVoyage.setSortType(TableColumn.SortType.ASCENDING);
        voyageTable.setEditable(true);
        voyageTable.setItems(data);
        voyageTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                {
                    Voyage selectedItem = voyageTable.getSelectionModel().getSelectedItem();
                    voyageTable.getItems().remove(selectedItem);
                    try {
                        voyageEntityManager.delete(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    voyageTable.getSelectionModel().clearSelection();
                }
            }
        });
    }
}
