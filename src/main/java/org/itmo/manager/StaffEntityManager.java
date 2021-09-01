package org.itmo.manager;

import javafx.scene.control.Alert;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.Staff;
import org.itmo.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffEntityManager {
    private MySqlDatabase database;

    public StaffEntityManager(MySqlDatabase database) {
        this.database = database;
    }

    public Staff getByLogin(String login) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Staff WHERE Username=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, login);

            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()) {
                return new Staff(
                        resultSet.getInt("Table_number"),
                        resultSet.getInt("Experience"),
                        resultSet.getString("Category"),
                        resultSet.getString("Mobile_number"),
                        resultSet.getInt("Birth_year"),
                        resultSet.getString("Fullname"),
                        resultSet.getString("Role"),
                        resultSet.getString("Username")
                );
            }

            return null;
        } catch (SQLException throwables) {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Ошибка: " + throwables.getErrorCode());
            warning.setHeaderText("Такой пользователь не существует.");
            warning.setContentText("Введите другой ник.");
            warning.showAndWait();
            return null;
        }
    }
}
