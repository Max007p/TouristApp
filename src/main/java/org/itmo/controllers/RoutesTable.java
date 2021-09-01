package org.itmo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.itmo.App;
import org.itmo.entities.Cities;
import org.itmo.entities.CitiesRoutes;
import org.itmo.entities.Routes;
import org.itmo.entities.Voyage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static org.itmo.App.citiesRoutesManager;
import static org.itmo.App.voyageEntityManager;

public class RoutesTable implements Initializable {
    private ObservableList<CitiesRoutes> dataCitiesRotes = FXCollections.observableArrayList(getAllCitiesRoutes());
    private ObservableList<Routes> dataRoutes = FXCollections.observableArrayList(getAllRoutes());
    private ObservableList<Cities> dataCities = FXCollections.observableArrayList(getAllCities());


    @FXML
    TableView<Routes> routeTable;

    @FXML
    TableColumn<Routes, String> columnName;
    @FXML
    TableColumn<Routes, String> columnStart;
    @FXML
    TableColumn<Routes, String> columnFinish;
    @FXML
    TableColumn<Routes, Integer> columnDuration;

    @FXML
    TableView<CitiesRoutes> routeCityTable;

    @FXML
    TableColumn<CitiesRoutes, String> columnTown;
    @FXML
    TableColumn<CitiesRoutes, String> columnRouteName;
    @FXML
    TableColumn<CitiesRoutes, Integer> columnSequence;

    @FXML
    TableView<Cities> cityTable;

    @FXML
    TableColumn<Cities, String> columnCity;


    @FXML
    Button addButtonRight;
    @FXML
    Button addButtonLeft;

    @FXML
    public void addRowRight() throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_routecity"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Ввод новой привязки");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void addRowLeft() throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_route"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Ввод нового маршрута");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void addRowCity() throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_city"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Ввод нового города");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void backToMain() throws IOException {
        App.setRoot("work_fxmls/profile_worker");
    }

    public RoutesTable() throws SQLException {
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

    private List<CitiesRoutes> getAllCitiesRoutes() throws SQLException {
        return citiesRoutesManager.getAllCitiesRoutes();
    }

    private List<Routes> getAllRoutes() throws SQLException {
        return citiesRoutesManager.getAllRoutes();
    }

    private List<Cities> getAllCities() throws SQLException {
        return citiesRoutesManager.getAllCities();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void makeAllEditable() throws SQLException {
        columnName.setCellValueFactory(new PropertyValueFactory<Routes, String>("name"));
        columnStart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Routes, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Routes, String> voyageStringCellDataFeatures) {
                Routes voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getStart();
                return new SimpleObjectProperty<String>(route);
            }
        });
        columnFinish.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Routes, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Routes, String> voyageStringCellDataFeatures) {
                Routes voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getFinish();
                return new SimpleObjectProperty<String>(route);
            }
        });
        columnDuration.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("duration"));
        ////////////////////////////////////////////////////////////

        columnTown.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitiesRoutes, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CitiesRoutes, String> voyageStringCellDataFeatures) {
                CitiesRoutes voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getTown();
                return new SimpleObjectProperty<String>(route);
            }
        });

        columnRouteName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitiesRoutes, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CitiesRoutes, String> voyageStringCellDataFeatures) {
                CitiesRoutes voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getRouteName();
                return new SimpleObjectProperty<String>(route);
            }
        });

        columnSequence.setCellValueFactory(new PropertyValueFactory<CitiesRoutes, Integer>("sequence"));

        //////////////////////////////////////////

        ObservableList<String> routeList = FXCollections.observableArrayList(citiesRoutesManager.getRoutesNames());
        ObservableList<String> cityList = FXCollections.observableArrayList(citiesRoutesManager.getCities());

        columnName.setCellFactory(new Callback<TableColumn<Routes, String>, TableCell<Routes, String>>() {
            @Override
            public TableCell<Routes, String> call(TableColumn<Routes, String> voyageDateTableColumn) {
                TableCell<Routes, String> cell = new TableCell<Routes, String>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(String date, boolean b) {

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
                                        commitEdit(textField.getText());
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

        columnDuration.setCellFactory(new Callback<TableColumn<Routes, Integer>, TableCell<Routes, Integer>>() {
            @Override
            public TableCell<Routes, Integer> call(TableColumn<Routes, Integer> voyageDateTableColumn) {
                TableCell<Routes, Integer> cell = new TableCell<Routes, Integer>(){
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

        columnDuration.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Routes, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Routes, Integer> voyageDateCellEditEvent) {
                TablePosition<Routes, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Routes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setDuration(stroka);
                try {
                    citiesRoutesManager.updateRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnStart.setCellFactory(ComboBoxTableCell.forTableColumn(cityList));
        columnStart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Routes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Routes, String> voyageStringCellEditEvent) {
                TablePosition<Routes, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                Routes voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setStart(stroka);
                try {
                    citiesRoutesManager.updateRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnFinish.setCellFactory(ComboBoxTableCell.forTableColumn(cityList));
        columnFinish.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Routes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Routes, String> voyageDateCellEditEvent) {
                TablePosition<Routes, String> pos = voyageDateCellEditEvent.getTablePosition();

                String stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Routes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setFinish(stroka);
                try {
                    citiesRoutesManager.updateRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Routes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Routes, String> voyageDateCellEditEvent) {
                TablePosition<Routes, String> pos = voyageDateCellEditEvent.getTablePosition();

                String stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Routes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setName(stroka);
                try {
                    citiesRoutesManager.updateRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        //2 таблица

        columnTown.setCellFactory(ComboBoxTableCell.forTableColumn(cityList));
        columnTown.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CitiesRoutes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CitiesRoutes, String> voyageDateCellEditEvent) {
                TablePosition<CitiesRoutes, String> pos = voyageDateCellEditEvent.getTablePosition();

                String stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                CitiesRoutes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setTown(stroka);
                try {
                    citiesRoutesManager.updateCitiesRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnRouteName.setCellFactory(ComboBoxTableCell.forTableColumn(routeList));
        columnRouteName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CitiesRoutes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CitiesRoutes, String> voyageDateCellEditEvent) {
                TablePosition<CitiesRoutes, String> pos = voyageDateCellEditEvent.getTablePosition();

                String stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                CitiesRoutes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setTown(stroka);
                try {
                    citiesRoutesManager.updateCitiesRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnSequence.setCellFactory(new Callback<TableColumn<CitiesRoutes, Integer>, TableCell<CitiesRoutes, Integer>>() {
            @Override
            public TableCell<CitiesRoutes, Integer> call(TableColumn<CitiesRoutes, Integer> voyageDateTableColumn) {
                TableCell<CitiesRoutes, Integer> cell = new TableCell<CitiesRoutes, Integer>(){
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

        columnSequence.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CitiesRoutes, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CitiesRoutes, Integer> voyageDateCellEditEvent) {
                TablePosition<CitiesRoutes, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                CitiesRoutes voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setSequence(stroka);
                try {
                    citiesRoutesManager.updateCitiesRoutesByEntity(voyage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        //3 таблица

        columnCity.setCellValueFactory(new PropertyValueFactory<Cities, String>("town"));
        columnCity.setCellFactory(new Callback<TableColumn<Cities, String>, TableCell<Cities, String>>() {
            @Override
            public TableCell<Cities, String> call(TableColumn<Cities, String> voyageDateTableColumn) {
                TableCell<Cities, String> cell = new TableCell<Cities, String>(){
                    private TextField textField;
                    @Override
                    protected void updateItem(String date, boolean b) {

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
                                        commitEdit(textField.getText());
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
        columnCity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cities, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Cities, String> voyageDateCellEditEvent) {
                TablePosition<Cities, String> pos = voyageDateCellEditEvent.getTablePosition();

                String stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Cities voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);
                String oldValue = voyage.getTown();

                voyage.setTown(stroka);
                try {
                    citiesRoutesManager.updateCitiesByEntity(voyage, oldValue);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });



        columnDuration.setSortType(TableColumn.SortType.ASCENDING);
        columnTown.setSortType(TableColumn.SortType.ASCENDING);
        columnCity.setSortType(TableColumn.SortType.ASCENDING);

        routeTable.setEditable(true);
        routeTable.setItems(dataRoutes);
        routeTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                {
                    Routes selectedItem = routeTable.getSelectionModel().getSelectedItem();
                    routeTable.getItems().remove(selectedItem);
                    try {
                        citiesRoutesManager.deleteRoute(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    routeTable.getSelectionModel().clearSelection();
                }
            }
        });

        cityTable.setEditable(true);
        cityTable.setItems(dataCities);
        cityTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                {
                    Cities selectedItem = cityTable.getSelectionModel().getSelectedItem();
                    cityTable.getItems().remove(selectedItem);
                    try {
                        citiesRoutesManager.deleteCity(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    cityTable.getSelectionModel().clearSelection();
                }
            }
        });

        routeCityTable.setEditable(true);
        routeCityTable.setItems(dataCitiesRotes);
        routeCityTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                {
                    CitiesRoutes selectedItem = routeCityTable.getSelectionModel().getSelectedItem();
                    routeCityTable.getItems().remove(selectedItem);
                    try {
                        citiesRoutesManager.deleteCityRoute(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    routeCityTable.getSelectionModel().clearSelection();
                }
            }
        });
    }
}
