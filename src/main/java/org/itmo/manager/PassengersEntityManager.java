package org.itmo.manager;

import javafx.scene.control.Alert;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.Passenger;
import org.itmo.entities.Staff;
import org.itmo.entities.Voyage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PassengersEntityManager {
    private MySqlDatabase database;

    public PassengersEntityManager(MySqlDatabase database) {
        this.database = database;
    }

    public void add (Passenger voyage) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Passenger(Phone_number, E-mail, Name, Username, Passport) values(?,?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, voyage.getPhoneNumber());
            s.setString(2, voyage.getEmail());
            s.setString(3, voyage.getName());
            s.setString(4, voyage.getUsername());
            s.setString(5, voyage.getPassport());
            s.executeUpdate();

        }
    }

    public Passenger getByLogin(String login) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Passenger WHERE Username=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, login);

            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()) {
                return new Passenger(
                        resultSet.getString("Phone_number"),
                        resultSet.getString("E-mail"),
                        resultSet.getString("Name"),
                        resultSet.getString("Username"),
                        resultSet.getString("Passport")
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
