package org.itmo.controllers;

import com.mysql.cj.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.itmo.App;
import org.itmo.entities.User;

import java.io.IOException;
import java.sql.SQLException;

import static org.itmo.App.authedUser;
import static org.itmo.App.userEntityManager;

public class Register {

    @FXML
    public TextField nicknameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField passwordField2;



    @FXML
    public void back() throws IOException {
        App.setRoot("common_fxmls/entry");
    }

    @FXML
    public void register() throws IOException, SQLException {
        if (StringUtils.isNullOrEmpty(nicknameField.getText()) || StringUtils.isNullOrEmpty(passwordField.getText()) || StringUtils.isNullOrEmpty(passwordField2.getText()))
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Введены пустые поля");
            warning.setHeaderText("Результат:");
            warning.setContentText("Введите данные в поля.");
            warning.showAndWait();
        }
        else if (!(passwordField.getText().equals(passwordField2.getText())))
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Введенные пароли не совпадают");
            warning.setHeaderText("Решение:");
            warning.setContentText("Введите пароль корректно.");
            warning.showAndWait();
        }

        if (passwordField.getText().equals(passwordField2.getText()) && nicknameField.getLength() > 3)
        {
            userEntityManager.add(new User(nicknameField.getText(), passwordField.getText()));
            authedUser = new User(nicknameField.getText(), passwordField.getText());
            App.setRoot("user_fxmls/profile_user");
        }


    }
}
