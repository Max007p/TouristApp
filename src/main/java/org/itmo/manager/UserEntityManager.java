package org.itmo.manager;

import javafx.scene.control.Alert;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.Staff;
import org.itmo.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEntityManager {
    private MySqlDatabase database;

    public UserEntityManager(MySqlDatabase database) {
        this.database = database;
    }

    public void add (User user) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO User(Username, Password, isAdmin) values(?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, user.getUsername());
            s.setString(2, user.getPassword());
            s.setBoolean(3, user.isAdmin());
            s.executeUpdate();
        }

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

    public User getByLoginAndPassword(String login, String password) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM User WHERE Username=? AND Password=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, login);
            s.setString(2, password);

            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()) {
                return new User(
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getBoolean("Isadmin")
                );
            }

            return null;
        }
    }

    public List<User> getAll() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM User";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<User> users = new ArrayList<>();
            while(resultSet.next()) {
                users.add(new User(
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getBoolean("Isadmin")
                ));
            }
            return users;
        }
    }

    public int update(User user) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE User SET Username=?, Password=? WHERE Username=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, user.getUsername());
            s.setString(2, user.getPassword());
            s.setString(3, user.getUsername());

            return s.executeUpdate();
        }
    }

    public int deleteByUsername(String username) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM User WHERE Username=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, username);

            return s.executeUpdate();
        }
    }

    public int delete(User user) throws SQLException
    {
        return deleteByUsername(user.getUsername());
    }




}
