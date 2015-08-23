package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLite {

    private String dbname = "movieDB.db";

    //--------------------------------------------------------------------------
    //singelton-pattern
    //--------------------------------------------------------------------------
    private static SQLite instance = null;

    public SQLite() {
    }

    public static SQLite getInstance() {
        if (instance == null) {
            instance = new SQLite();
        }
        return instance;
    }
    //--------------------------------------------------------------------------

    //erstellt falls nötig die benötigen tabelen in der datenbank
    //über den befehl "CREATE TABLE IF NOT EXISTS" wird geprüft ob die tabelle
    //exisiert und falls nicht wird sie erstellt
    public void createTable() {
        String sql;
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbname);

            //Create MovieList Table
            Statement stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Movielist("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
                    + "UserID int,"
                    + "imdbID char(20),"
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

    //einfüge-methode um in die datenbank etwas einzufügen
    //parameter: tabelen-name, die zu speicherenden werte werden als map mit-
    //gegeben
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

    //aktualisiert einen datensatz in der datenbank
    //parameter: tabellen-namen, werte die aktualsiert werden sollen als map, 
    //where statement als string
    public void update(String table, Map<String, String> map, String statement) {
        try {
            String fields = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    String value = entry.getValue().replaceAll("'", "");
                    if (fields != "") {
                        fields += "," + entry.getKey() + "='" + value + "'";
                    } else {
                        fields += entry.getKey() + "='" + value + "'";
                    }
                }
            }

            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            Statement stmt = c.createStatement();

            String sql = "UPDATE " + table + " SET " + fields + " WHERE " + statement + ";";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    //prüft ob element in der datenbank vorhanden ist
    //falls etwas gefunden wurde gibt die methode die anzahl der gefundenen
    //ergebnisse zurück
    public int exsists(String table, String field, String statement) {
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

        return resultList.size();
    }

    //gibt geforderte datensätze zurück
    //rückgabe typ ist eine List
    //--> [{name:value},{name:value},...]
    //zugriff auf diese liste:
    //zurückgegebene list durch iterieren und bei bei jeder row ist kann über 
    //row.getObject("name") auf value zugegriffen werden
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
