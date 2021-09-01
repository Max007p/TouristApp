package org.itmo.manager;

import org.itmo.database.MySqlDatabase;
import org.itmo.entities.Bus;
import org.itmo.entities.ModuleInfo;
import org.itmo.entities.PaidPassengers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaidPassengersManager {

    private MySqlDatabase database;

    public PaidPassengersManager(MySqlDatabase database)
    {
        this.database = database;
    }

    public void addOnline (PaidPassengers passenger) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Ways_passenger(Passenger_User, Ways_ID_way, Is_payed, Passport) values(?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setString(1, passenger.getPassengerUser());
            s.setInt(2, passenger.getIdWay());
            s.setBoolean(3, passenger.getPayed());
            s.setString(4, passenger.getPassport());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                passenger.setId(keys.getInt(1));
            }


        }
    }

    public List<PaidPassengers> getAll() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Ways_passenger";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<PaidPassengers> voyage = new ArrayList<>();
            while(resultSet.next()) {
                voyage.add(new PaidPassengers(
                        resultSet.getInt("Id_passenger"),
                        resultSet.getString("Passenger_User"),
                        resultSet.getInt("Ways_ID_way"),
                        resultSet.getBoolean("Is_payed"),
                        resultSet.getInt("Staff_Table_number"),
                        resultSet.getString("Customer_name"),
                        resultSet.getString("Passport")
                ));
            }
            return voyage;
        }
    }

    public void addLocal (PaidPassengers passenger) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Ways_passenger(Is_payed, Staff_Table_number, Customer_name, Passport, Ways_ID_way) values(?,?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setBoolean(1, passenger.getPayed());
            s.setInt(2, passenger.getTableNumber());
            s.setString(3, passenger.getCustomerName());
            s.setString(4, passenger.getPassport());
            s.setInt(5, passenger.getIdWay());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                passenger.setId(keys.getInt(1));
            }


        }
    }

    public List<PaidPassengers> getByUsername (String username) throws SQLException
    {

        try (Connection c = database.getConnection())
        {
            List<PaidPassengers> resultList = new ArrayList<>();
            String sql = "SELECT * FROM Ways_passenger WHERE Passenger_User=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, username);

            ResultSet result = s.executeQuery();
            while (result.next())
            {
                resultList.add(new PaidPassengers(
                        result.getInt("Id_passenger"),
                        result.getString("Passenger_User"),
                        result.getInt("Ways_ID_Way"),
                        result.getBoolean("Is_payed"),
                        result.getInt("Staff_table_number"),
                        result.getString("Customer_name"),
                        result.getString("Passport")
                ));
            }

            if (!resultList.isEmpty())
            {
                return resultList;
            }

            return null;
        }

    }

    public int updatePassengersByEntity(PaidPassengers bus) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Ways_passenger SET Passenger_User=?, Ways_ID_way=?, Is_payed=?, Staff_Table_number=?, Customer_name=?, Passport=? WHERE Id_passenger=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, bus.getPassengerUser());
            s.setInt(2, bus.getIdWay());
            s.setBoolean(3, bus.getPayed());
            s.setInt(4, bus.getTableNumber());
            s.setString(5, bus.getCustomerName());
            s.setString(6, bus.getPassport());
            s.setInt(7, bus.getId());

            return s.executeUpdate();
        }
    }

}
