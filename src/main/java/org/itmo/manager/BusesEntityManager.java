package org.itmo.manager;

import org.itmo.database.MySqlDatabase;
import org.itmo.entities.Bus;
import org.itmo.entities.Cities;
import org.itmo.entities.ModuleInfo;
import org.itmo.entities.Routes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusesEntityManager {
    private MySqlDatabase database;

    public BusesEntityManager(MySqlDatabase database) {
        this.database = database;
    }

    public void addBus (Bus bus) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Buses(Number, Total_run, Status, Buses_Code_model) values(?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, bus.getNumber());
            s.setInt(2, bus.getTotalRun());
            s.setString(3, bus.getStatus());
            s.setString(4, bus.getCodeModel());
            s.executeUpdate();
        }
    }

    public void addBusInfo (ModuleInfo info) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Module_info(Name, Toilets, Manufacturer, Floors, Seats, Construct_Date, Code_model) values(?,?,?,?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, info.getName());
            s.setBoolean(2, info.getToilet());
            s.setString(3, info.getManufacturer());
            s.setInt(4, info.getFloors());
            s.setInt(5, info.getSeats());
            s.setDate(6, info.getConstructDate());
            s.setString(7, info.getCodeModel());
            s.executeUpdate();
        }
    }

    public List<String> getCodeModels() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT DISTINCT Code_model FROM Module_info";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<String> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(result.getString("Code_model"));
            }
            return routes;
        }
    }

    public List<Bus> getAllBuses() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Buses";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<Bus> voyage = new ArrayList<>();
            while(resultSet.next()) {
                voyage.add(new Bus(
                        resultSet.getInt("Number"),
                        resultSet.getInt("Total_run"),
                        resultSet.getString("Status"),
                        resultSet.getString("Buses_Code_model")
                ));
            }
            return voyage;
        }
    }

    public List<ModuleInfo> getAllBusesInfo() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Module_info";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<ModuleInfo> voyage = new ArrayList<>();
            while(resultSet.next()) {
                voyage.add(new ModuleInfo(
                        resultSet.getString("Name"),
                        resultSet.getBoolean("Toilets"),
                        resultSet.getString("Manufacturer"),
                        resultSet.getInt("Floors"),
                        resultSet.getInt("Seats"),
                        resultSet.getDate("Construct_date"),
                        resultSet.getString("Code_model")
                ));
            }
            return voyage;
        }
    }

    public int deleteByNumber(Integer city) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Buses WHERE Number=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, city);

            return s.executeUpdate();
        }
    }

    public int deleteByCode(String city) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Module_info WHERE Code_model=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, city);

            return s.executeUpdate();
        }
    }

    public int deleteBus(Bus cities) throws SQLException
    {
        return deleteByNumber(cities.getNumber());
    }

    public int deleteBusesInfo(ModuleInfo cities) throws SQLException
    {
        return deleteByCode(cities.getCodeModel());
    }

    public int updateBusesByEntity(Bus bus, Integer oldValue) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Buses SET Number=?, Total_run=?, Status=?, Buses_Code_model=? WHERE Number=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, bus.getNumber());
            s.setInt(2, bus.getTotalRun());
            s.setString(3, bus.getStatus());
            s.setString(4, bus.getCodeModel());
            s.setInt(5, oldValue);

            return s.executeUpdate();
        }
    }

    public int updateBusesInfoByEntity(ModuleInfo info, String oldValue) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Module_info SET Name=?, Toilets=?, Manufacturer=?, Floors=?, Seats=?, Construct_date=?, Code_model=? WHERE Code_model=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, info.getName());
            s.setBoolean(2, info.getToilet());
            s.setString(3, info.getManufacturer());
            s.setInt(4, info.getFloors());
            s.setInt(5, info.getSeats());
            s.setDate(6, info.getConstructDate());
            s.setString(8, oldValue);
            s.setString(7, info.getCodeModel());
            s.executeUpdate();

            return s.executeUpdate();
        }
    }




}
