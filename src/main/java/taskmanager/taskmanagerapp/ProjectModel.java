package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class ProjectModel {

    public void createProject(String title, String desc, String date) throws SQLException {
        // get curr date
        LocalDate today = LocalDate.now(ZoneId.of("America/Guyana"));


        String statement = " INSERT INTO PROJECT (title,description,created,deadline) VALUES ( ? , ? , ? , ?)";
        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = connection.prepareStatement(statement);

        pStatement.setString(1, title);
        pStatement.setString(2, desc);
        pStatement.setString(3, String.valueOf(today));
        pStatement.setString(4, date);

        pStatement.executeUpdate();

        connection.close();
    }

    public ObservableList<Users> getProjectMembers(Integer pID) {


        // create list of projects for table insertion
        ObservableList<Users> memberList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.Connector();
            // need to do join
            String statementSQL = "SELECT users.username,users.email FROM users " +
                    "INNER JOIN projectMembers " +
                    "ON users.username = projectMembers.username " +
                    "WHERE projectMembers.projectId = " + pID.toString();

            PreparedStatement statement = connection.prepareStatement(statementSQL);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                memberList.add(new Users(
                        results.getString("username"),
                        results.getString("email")
                ));


            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return memberList;

    }


    public ResultSet getProjects() throws SQLException {

        Connection connection = DBConnection.Connector();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM PROJECT");
        ResultSet results = statement.executeQuery();

//        connection.close();

        return results;


    }

    public ResultSet searchProject(String title) throws SQLException {

        Connection connection = DBConnection.Connector();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM PROJECT where title LIKE '%" + title + "%'");
        ResultSet results = statement.executeQuery();

        return results;

    }

    public void deleteProject(Integer Projectid) throws SQLException {

        Connection connection = DBConnection.Connector();

        // enable foreign keys to enable cascade
        String statement = "PRAGMA foreign_keys = ON";
        PreparedStatement pStatement = connection.prepareStatement(statement);
        pStatement.execute();

        statement = "DELETE FROM PROJECT WHERE ID =" + Projectid;
        pStatement = connection.prepareStatement(statement);
        pStatement.executeUpdate();

        connection.close();


    }

    public void deleteProjectMembers(Integer projectID, String username) throws SQLException {

        String statement = "DELETE FROM PROJECTMEMBERS WHERE PROJECTID = ? AND USERNAME = ?";

        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = null;

        pStatement = connection.prepareStatement(statement);

        pStatement.setString(1, projectID.toString());

        pStatement.setString(2, username);

        pStatement.executeUpdate();


        connection.close();


    }

    public void addNewMember(Integer id, String user) throws SQLException {

        String statement = "REPLACE INTO projectMembers (projectId,username) VALUES ( ? , ? )";

        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = connection.prepareStatement(statement);

        pStatement.setString(1, String.valueOf(id));
        pStatement.setString(2, user);

        pStatement.executeUpdate();

        connection.close();
    }
}


