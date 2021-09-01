package org.itmo.manager;

import javafx.scene.control.Alert;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.User;
import org.itmo.entities.Voyage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoyageEntityManager {
    private MySqlDatabase database;

    public VoyageEntityManager(MySqlDatabase database) {
        this.database = database;
    }

    public void add (Voyage voyage) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Voyage(Start_date, Finish_date, Passengers, Ticket_cost, Routes_Name, Buses_Number, Done_flag, Finish_date_fact, ID_way) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setDate(1, voyage.getStartDate());
            s.setDate(2, voyage.getFinishDate());
            s.setInt(3, voyage.getPassengers());
            s.setInt(4, voyage.getTicketCost());
            s.setString(5, voyage.getRouteName());
            s.setInt(6, voyage.getBusNumber());
            s.setBoolean(7, voyage.isDoneFlag());
            s.setDate(8, voyage.getFactFinish());
            s.setInt(9, 0);
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                voyage.setIdWay(keys.getInt(1));
            }

        }
    }

    public List<Voyage> getAll() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Voyage";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<Voyage> voyage = new ArrayList<>();
            while(resultSet.next()) {
                voyage.add(new Voyage(
                        resultSet.getDate("Start_date"),
                        resultSet.getDate("Finish_date"),
                        resultSet.getInt("Passengers"),
                        resultSet.getInt("Ticket_cost"),
                        resultSet.getString("Routes_Name"),
                        resultSet.getInt("Buses_Number"),
                        resultSet.getBoolean("Done_flag"),
                        resultSet.getDate("Finish_date_fact"),
                        resultSet.getInt("ID_way")
                ));
            }
            return voyage;
        }
    }

    public List<String> getRoutes() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT DISTINCT Name FROM Routes";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<String> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(result.getString("Name"));
            }
            return routes;
        }
    }

    public List<Integer> getIds() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT DISTINCT ID_way FROM Voyage";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<Integer> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(result.getInt("ID_way"));
            }
            return routes;
        }
    }

    public List<Integer> getBuses() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT DISTINCT Number FROM Buses";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<Integer> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(result.getInt("Number"));
            }
            return routes;
        }
    }

    public int deleteById(int id) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Voyage WHERE ID_way=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }

    public int delete(Voyage voyage) throws SQLException
    {
        return deleteById(voyage.getIdWay());
    }

    public int updateByEntity(Voyage voyage) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Voyage SET Start_date=?, Finish_date=?, Passengers=?, Ticket_cost=?, Routes_name=?, Buses_number=?, Done_flag=?, Finish_date_fact=? WHERE ID_way=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setDate(1, voyage.getStartDate());
            s.setDate(2, voyage.getFinishDate());
            s.setInt(3, voyage.getPassengers());
            s.setInt(4, voyage.getTicketCost());
            s.setString(5, voyage.getRouteName());
            s.setInt(6, voyage.getBusNumber());
            s.setBoolean(7, voyage.isDoneFlag());
            s.setInt(9, voyage.getIdWay());
            s.setDate(8, voyage.getFactFinish());


            return s.executeUpdate();
        }
    }
}
