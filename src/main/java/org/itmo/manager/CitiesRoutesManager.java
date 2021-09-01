package org.itmo.manager;

import javafx.scene.control.Alert;
import org.itmo.database.MySqlDatabase;
import org.itmo.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitiesRoutesManager {
    private MySqlDatabase database;

    public CitiesRoutesManager(MySqlDatabase database) {
        this.database = database;
    }

    public void addCity (Cities city) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Cities(Town) values(?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, city.getTown());
            s.executeUpdate();
        }
    }

    public void addRoute (Routes routes) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Routes(Name, Start, Finish, Duration) values(?,?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, routes.getName());
            s.setString(2, routes.getStart());
            s.setString(3, routes.getFinish());
            s.setInt(4, routes.getDuration());
            s.executeUpdate();
        }
    }

    public void addCityRouteConnection (CitiesRoutes citiesRoutes) throws SQLException
    {
        try (Connection c = database.getConnection())
        {
            String sql = "INSERT INTO Cities_routes(Cities_town, Routes_Name, Sequence) values(?,?,?)";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, citiesRoutes.getTown());
            s.setString(2, citiesRoutes.getRouteName());
            s.setInt(3, citiesRoutes.getSequence());
            s.executeUpdate();

        }
    }

    public List<String> getCities() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT DISTINCT Town FROM Cities";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<String> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(result.getString("Town"));
            }
            return routes;
        }
    }

    public List<String> getRoutesNames() throws SQLException
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

    public List<Cities> getAllCities() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Cities";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<Cities> voyage = new ArrayList<>();
            while(resultSet.next()) {
                voyage.add(new Cities(
                        resultSet.getString("Town")
                ));
            }
            return voyage;
        }
    }

    public List<Routes> getAllRoutes() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Routes";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<Routes> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(new Routes(
                        result.getString("Name"),
                        result.getString("Start"),
                        result.getString("Finish"),
                        result.getInt("Duration")
                ));
            }
            return routes;
        }
    }

    public List<CitiesRoutes> getAllCitiesRoutes() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM Cities_routes";
            Statement s = c.createStatement();
            ResultSet result = s.executeQuery(sql);

            List<CitiesRoutes> routes = new ArrayList<>();
            while(result.next())
            {
                routes.add(new CitiesRoutes(
                        result.getString("Cities_town"),
                        result.getString("Routes_name"),
                        result.getInt("Sequence")
                ));
            }
            return routes;
        }
    }

    public int deleteByNameCity(String city) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Cities WHERE Town=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, city);

            return s.executeUpdate();
        }
    }

    public int deleteByNameRoute(String name) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Routes WHERE Name=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, name);

            return s.executeUpdate();
        }
    }

    public int deleteByCityAndRoute(String city, String route) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "DELETE FROM Cities_routes WHERE Cities_town=? AND Routes_Name=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, city);
            s.setString(2, route);

            return s.executeUpdate();
        }
    }

    public int deleteCity(Cities cities) throws SQLException
    {
        return deleteByNameCity(cities.getTown());
    }

    public int deleteRoute(Routes route) throws SQLException
    {
        return deleteByNameRoute(route.getName());
    }

    public int deleteCityRoute(CitiesRoutes citiesRoutes) throws SQLException
    {
        return deleteByCityAndRoute(citiesRoutes.getTown(), citiesRoutes.getRouteName());
    }

    public int updateCitiesByEntity(Cities cities, String oldValue) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Cities SET Town=? WHERE Town=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, cities.getTown());
            s.setString(2, oldValue);

            return s.executeUpdate();
        }
    }

    public int updateRoutesByEntity(Routes route) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Routes SET Name=?, Start=?, Finish=?, Duration=? WHERE Name=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, route.getName());
            s.setString(2, route.getStart());
            s.setString(3, route.getFinish());
            s.setInt(4, route.getDuration());
            s.setString(5, route.getName());


            return s.executeUpdate();
        }
    }

    public int updateCitiesRoutesByEntity(CitiesRoutes citiesRoutes) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE Cities_routes SET Cities_town=?, Routes_name=?, Sequence=? WHERE Cities_town=? AND Routes_name=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, citiesRoutes.getTown());
            s.setString(2, citiesRoutes.getRouteName());
            s.setInt(3, citiesRoutes.getSequence());
            s.setString(4, citiesRoutes.getTown());
            s.setString(5, citiesRoutes.getRouteName());

            return s.executeUpdate();
        }
    }
}
