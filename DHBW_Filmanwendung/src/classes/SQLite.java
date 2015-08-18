/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arth
 */
public class SQLite {

    private String dbname = "movieDB.db";

    private static SQLite instance = null;

    public SQLite() {
    }

    public static SQLite getInstance() {
        if (instance == null) {
            instance = new SQLite();
        }
        return instance;
    }

    public void createTable() {
        String sql;
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbname);

            //Create MovieList Table
            Statement stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Movielist("
                    + "ID int UNIQUE,"
                    + "UserID int,"
                    + "MovieID char(20),"
                    + "MerkList bit,"
                    + "FavList bit,"     
                    + "UserRate char(3)"
                    + ");";
            stmt.executeUpdate(sql);

            //Create Movie Table
            sql = "CREATE TABLE IF NOT EXISTS Movie("
                    + "imdbID char(20) PRIMARY KEY,"
                    + "Title char(20),"
                    + "Year char(20),"
                    + "Runtime char(20),"
                    + "Genre char(200),"  
                    + "Poster text,"
                    + "Director char(200),"
                    + "Released char(20),"
                    + "Plot text,"
                    + "imdbRating char(3)"
                    + ");";
            stmt.executeUpdate(sql);

            //Create User Table
            sql = "CREATE TABLE IF NOT EXISTS User("
                    + "UserID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
                    + "Username char(200) NOT NULL UNIQUE"
                    + ");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void insert(String table, Map<String, String> map) {
        try {
            String col = "";
            String values = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String value = entry.getValue().replaceAll("'", "");
                if (value != null) {
                    if (col != "") {
                        col += "," + entry.getKey();
                        values += "," + "'" + value + "'";
                    } else {
                        col += entry.getKey();
                        values += "'" + value + "'";
                    }
                }
            }

            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            Statement stmt = c.createStatement();

            String sql = "INSERT INTO " + table + " (" + col + ") VALUES (" + values + ");";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public List select(String table, String field, String statement) {

        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;

        if (field.isEmpty()) {
            field = "*";
        }

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();

            rs = stmt.executeQuery("SELECT " + field + " FROM " + table + " WHERE " + statement + ";");

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while (rs.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return resultList;
    }
}
