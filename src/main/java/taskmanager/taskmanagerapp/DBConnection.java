package taskmanager.taskmanagerapp;

import java.sql.*;
public class DBConnection {


    public static Connection Connector() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            System.out.println("Db Connected");
            return conn;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
