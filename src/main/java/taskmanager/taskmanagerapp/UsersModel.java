package taskmanager.taskmanagerapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersModel {

    public ResultSet checkUserLogin(String FormUsername,String FormPassword) throws SQLException {
        Connection connection = DBConnection.Connector();

        PreparedStatement getUsers = connection.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?");
        getUsers.setString(1, FormUsername);
        getUsers.setString(2, FormPassword);


        ResultSet results =getUsers.executeQuery();

        connection.close();
        // grab results from db

        return results;
    }
}
