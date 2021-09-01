package org.itmo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.itmo.entities.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static org.itmo.App.*;

public class BusesTable implements Initializable {

    private ObservableList<String> statusList = FXCollections.observableArrayList();
    private ObservableList<String> codeList = FXCollections.observableArrayList(busesEntityManager.getCodeModels());
    private ObservableList<Bus> dataBuses = FXCollections.observableArrayList(getAllBuses());
    private ObservableList<ModuleInfo> dataBusesInfo = FXCollections.observableArrayList(getAllBusesInfo());

    @FXML
    TableView<ModuleInfo> busInfoTable;

    @FXML
    TableColumn<ModuleInfo, String> columnName;
    @FXML
    TableColumn<ModuleInfo, Boolean> columnToilets;
    @FXML
    TableColumn<ModuleInfo, String> columnManufacturer;
    @FXML
    TableColumn<ModuleInfo, Integer> columnFloors;
    @FXML
    TableColumn<ModuleInfo, Integer> columnSeats;
    @FXML
    TableColumn<ModuleInfo, Date> columnDate;
    @FXML
    TableColumn<ModuleInfo, String> columnCode;

    @FXML
    TableView<Bus> busTable;

    @FXML
    TableColumn<Bus, Integer> columnNumber;
    @FXML
    TableColumn<Bus, Integer> columnTotalRun;
    @FXML
    TableColumn<Bus, String> columnStatus;
    @FXML
    TableColumn<Bus, String> columnCode2;


    @FXML
    Button addButtonRight;
    @FXML
    Button addButtonLeft;

    @FXML
    public void addRowRight () throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_buses"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Регистрация автобуса");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void addRowLeft () throws SQLException, IOException {
        Scene sceneAdd = new Scene(loadFXML("work_fxmls/add_busesinfo"), 640, 480);
        Stage stage = new Stage();
        stage.setTitle("Ввод информации об автобусе");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(sceneAdd);
        stage.show();
    }

    @FXML
    public void backToMain () throws IOException {
        App.setRoot("work_fxmls/profile_worker");
    }

    public BusesTable() throws SQLException {
    }


    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        try {
            statusList.add("Готов");
            statusList.add("Не готов");
            makeAllEditable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<Bus> getAllBuses () throws SQLException {
        return busesEntityManager.getAllBuses();
    }

    private List<ModuleInfo> getAllBusesInfo () throws SQLException {
        return busesEntityManager.getAllBusesInfo();
    }

    private static Parent loadFXML (String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void makeAllEditable () throws SQLException {
        columnName.setCellValueFactory(new PropertyValueFactory<ModuleInfo, String>("name"));
        columnToilets.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ModuleInfo, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ModuleInfo, Boolean> voyageBooleanCellDataFeatures) {
                ModuleInfo voyage = voyageBooleanCellDataFeatures.getValue();

                SimpleBooleanProperty prop = new SimpleBooleanProperty(voyage.getToilet());

                prop.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        voyage.setToilet(t1);
                        try {
                            busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                return prop;
            }
        });
        columnManufacturer.setCellValueFactory(new PropertyValueFactory<ModuleInfo, String>("manufacturer"));
        columnFloors.setCellValueFactory(new PropertyValueFactory<ModuleInfo, Integer>("floors"));
        columnSeats.setCellValueFactory(new PropertyValueFactory<ModuleInfo, Integer>("seats"));
        columnDate.setCellValueFactory(new PropertyValueFactory<ModuleInfo, Date>("constructDate"));
        columnCode.setCellValueFactory(new PropertyValueFactory<ModuleInfo, String>("codeModel"));

        columnName.setCellFactory(new Callback<TableColumn<ModuleInfo, String>, TableCell<ModuleInfo, String>>() {
            @Override
            public TableCell<ModuleInfo, String> call(TableColumn<ModuleInfo, String> voyageDateTableColumn) {
                TableCell<ModuleInfo, String> cell = new TableCell<ModuleInfo, String>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(String date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date);
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
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
                        } else {
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

        columnToilets.setCellFactory(new Callback<TableColumn<ModuleInfo, Boolean>, TableCell<ModuleInfo, Boolean>>() {
            @Override
            public TableCell<ModuleInfo, Boolean> call(TableColumn<ModuleInfo, Boolean> voyageBooleanTableColumn) {
                CheckBoxTableCell<ModuleInfo, Boolean> cell = new CheckBoxTableCell<ModuleInfo, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        columnManufacturer.setCellFactory(new Callback<TableColumn<ModuleInfo, String>, TableCell<ModuleInfo, String>>() {
            @Override
            public TableCell<ModuleInfo, String> call(TableColumn<ModuleInfo, String> voyageDateTableColumn) {
                TableCell<ModuleInfo, String> cell = new TableCell<ModuleInfo, String>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(String date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
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
                        } else {
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

        columnFloors.setCellFactory(new Callback<TableColumn<ModuleInfo, Integer>, TableCell<ModuleInfo, Integer>>() {
            @Override
            public TableCell<ModuleInfo, Integer> call(TableColumn<ModuleInfo, Integer> voyageDateTableColumn) {
                TableCell<ModuleInfo, Integer> cell = new TableCell<ModuleInfo, Integer>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.parseInt(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        } else {
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

        columnSeats.setCellFactory(new Callback<TableColumn<ModuleInfo, Integer>, TableCell<ModuleInfo, Integer>>() {
            @Override
            public TableCell<ModuleInfo, Integer> call(TableColumn<ModuleInfo, Integer> voyageDateTableColumn) {
                TableCell<ModuleInfo, Integer> cell = new TableCell<ModuleInfo, Integer>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.parseInt(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        } else {
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

        columnDate.setCellFactory(new Callback<TableColumn<ModuleInfo, Date>, TableCell<ModuleInfo, Date>>() {
            @Override
            public TableCell<ModuleInfo, Date> call(TableColumn<ModuleInfo, Date> voyageDateTableColumn) {
                TableCell<ModuleInfo, Date> cell = new TableCell<ModuleInfo, Date>(){
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

        columnCode.setCellFactory(new Callback<TableColumn<ModuleInfo, String>, TableCell<ModuleInfo, String>>() {
            @Override
            public TableCell<ModuleInfo, String> call(TableColumn<ModuleInfo, String> voyageDateTableColumn) {
                TableCell<ModuleInfo, String> cell = new TableCell<ModuleInfo, String>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(String date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
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
                        } else {
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

        columnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, String> voyageStringCellEditEvent) {
                TablePosition<ModuleInfo, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setName(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnManufacturer.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, String> voyageStringCellEditEvent) {
                TablePosition<ModuleInfo, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setManufacturer(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnFloors.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, Integer> voyageDateCellEditEvent) {
                TablePosition<ModuleInfo, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setFloors(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnSeats.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, Integer> voyageDateCellEditEvent) {
                TablePosition<ModuleInfo, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setSeats(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, Date> voyageDateCellEditEvent) {
                TablePosition<ModuleInfo, Date> pos = voyageDateCellEditEvent.getTablePosition();

                Date stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setConstructDate(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, voyage.getCodeModel());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnCode.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ModuleInfo, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ModuleInfo, String> voyageStringCellEditEvent) {
                TablePosition<ModuleInfo, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                ModuleInfo voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);
                String old = voyage.getCodeModel();

                voyage.setManufacturer(stroka);
                try {
                    busesEntityManager.updateBusesInfoByEntity(voyage, old);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });





        //2 таблица

        columnNumber.setCellValueFactory(new PropertyValueFactory<Bus, Integer>("number"));
        columnTotalRun.setCellValueFactory(new PropertyValueFactory<Bus, Integer>("totalRun"));
        columnStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bus, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bus, String> voyageStringCellDataFeatures) {
                Bus voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getStatus();
                return new SimpleObjectProperty<String>(route);
            }
        });
        columnCode2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bus, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bus, String> voyageStringCellDataFeatures) {
                Bus voyage = voyageStringCellDataFeatures.getValue();

                String route = voyage.getCodeModel();
                return new SimpleObjectProperty<String>(route);
            }
        });

        columnNumber.setCellFactory(new Callback<TableColumn<Bus, Integer>, TableCell<Bus, Integer>>() {
            @Override
            public TableCell<Bus, Integer> call(TableColumn<Bus, Integer> voyageDateTableColumn) {
                TableCell<Bus, Integer> cell = new TableCell<Bus, Integer>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.parseInt(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        } else {
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

        columnTotalRun.setCellFactory(new Callback<TableColumn<Bus, Integer>, TableCell<Bus, Integer>>() {
            @Override
            public TableCell<Bus, Integer> call(TableColumn<Bus, Integer> voyageDateTableColumn) {
                TableCell<Bus, Integer> cell = new TableCell<Bus, Integer>() {
                    private TextField textField;

                    @Override
                    protected void updateItem(Integer date, boolean b) {

                        super.updateItem(date, b);
                        if (b) {
                            this.setText(null);
                            setGraphic(null);
                        } else {
                            if (isEditing()) {
                                if (textField != null) {
                                    textField.setText(date.toString());
                                }
                                setGraphic(textField);
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            } else {
                                setText(date.toString());
                                setContentDisplay(ContentDisplay.TEXT_ONLY);
                            }
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();

                        if (textField == null) {
                            textField = new TextField(getText());
                            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent t) {
                                    if (t.getCode() == KeyCode.ENTER) {
                                        commitEdit(Integer.parseInt(textField.getText()));
                                    } else if (t.getCode() == KeyCode.ESCAPE) {
                                        cancelEdit();

                                    }
                                }
                            });
                        } else {
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

        columnStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusList));
        columnStatus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bus, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Bus, String> voyageStringCellEditEvent) {
                TablePosition<Bus, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                Bus voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setStatus(stroka);
                try {
                    busesEntityManager.updateBusesByEntity(voyage, voyage.getNumber());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnCode2.setCellFactory(ComboBoxTableCell.forTableColumn(codeList));
        columnCode2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bus, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Bus, String> voyageStringCellEditEvent) {
                TablePosition<Bus, String> pos = voyageStringCellEditEvent.getTablePosition();

                String stroka = voyageStringCellEditEvent.getNewValue();
                int row = pos.getRow();
                Bus voyage = voyageStringCellEditEvent.getTableView().getItems().get(row);

                voyage.setStatus(stroka);
                try {
                    busesEntityManager.updateBusesByEntity(voyage, voyage.getNumber());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnNumber.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bus, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Bus, Integer> voyageDateCellEditEvent) {
                TablePosition<Bus, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Bus voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);
                int old = voyage.getNumber();

                voyage.setNumber(stroka);
                try {
                    busesEntityManager.updateBusesByEntity(voyage, old);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        columnTotalRun.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bus, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Bus, Integer> voyageDateCellEditEvent) {
                TablePosition<Bus, Integer> pos = voyageDateCellEditEvent.getTablePosition();

                Integer stroka = voyageDateCellEditEvent.getNewValue();
                int row = pos.getRow();
                Bus voyage = voyageDateCellEditEvent.getTableView().getItems().get(row);

                voyage.setTotalRun(stroka);
                try {
                    busesEntityManager.updateBusesByEntity(voyage, voyage.getNumber());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });


        columnName.setSortType(TableColumn.SortType.ASCENDING);
        columnNumber.setSortType(TableColumn.SortType.ASCENDING);

        busTable.setEditable(true);
        busTable.setItems(dataBuses);
        busTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    Bus selectedItem = busTable.getSelectionModel().getSelectedItem();
                    busTable.getItems().remove(selectedItem);
                    try {
                        busesEntityManager.deleteBus(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    busTable.getSelectionModel().clearSelection();
                }
            }
        });

        busInfoTable.setEditable(true);
        busInfoTable.setItems(dataBusesInfo);
        busInfoTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    ModuleInfo selectedItem = busInfoTable.getSelectionModel().getSelectedItem();
                    busInfoTable.getItems().remove(selectedItem);
                    try {
                        busesEntityManager.deleteBusesInfo(selectedItem);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    busInfoTable.getSelectionModel().clearSelection();
                }
            }
        });
    }
}
