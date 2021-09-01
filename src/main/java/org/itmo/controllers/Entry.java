package org.itmo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.itmo.App;

import java.io.IOException;
import java.sql.SQLException;

import static org.itmo.App.*;

public class Entry {

    @FXML
    public Button registerButton;

    @FXML
    public Button loginButton;

    @FXML
    public TextField nicknameField;

    @FXML
    public PasswordField passwordField;

    public Entry() { }

    @FXML
    public void register() throws IOException {
        App.setRoot("common_fxmls/register");
    }

    @FXML
    public void login() throws IOException, SQLException
    {
        if (!(StringUtils.isEmpty(nicknameField.getText()) || StringUtils.isEmpty(passwordField.getText())))
        {
            authedUser = userEntityManager.getByLoginAndPassword(nicknameField.getText(), passwordField.getText());
            if (authedUser != null && !authedUser.isAdmin())
            {
                App.setRoot("user_fxmls/profile_user");
            }
            else if (authedUser != null && authedUser.isAdmin())
            {
                App.setRoot("work_fxmls/profile_worker");
            }
            else
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Введены неверные данные");
                warning.setHeaderText("Результат:");
                warning.setContentText("Неверный логин или пароль.");
                warning.showAndWait();
            }
        }
        else
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Введены пустые поля");
            warning.setHeaderText("Результат:");
            warning.setContentText("Введите данные.");
            warning.showAndWait();
        }
    }




}
