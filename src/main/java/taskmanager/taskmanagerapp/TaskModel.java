package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class TaskModel {

    public void addTask(Integer projectId,String title,String desc,String date) throws SQLException {

        // get curr date
        LocalDate today = LocalDate.now(ZoneId.of("America/Guyana"));

        String statement = " INSERT INTO Tasks (projectId,title,description,created,deadline) VALUES ( ? , ? , ? , ?, ?)";
        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = connection.prepareStatement(statement);
        pStatement.setString(1, String.valueOf(projectId));
        pStatement.setString(2, title);
        pStatement.setString(3, desc);
        pStatement.setString(4, String.valueOf(today));
        pStatement.setString(5, date);

        pStatement.executeUpdate();

        connection.close();
    }

    public ObservableList<Task> getTasks(Integer projectId) {

        Connection connection = DBConnection.Connector();

        // create list of projects for table insertion
        ObservableList<Task> list = FXCollections.observableArrayList();


        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM TASKS WHERE projectId =" +projectId.toString() );
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                list.add(new Task(
                        Integer.parseInt(results.getString("id")),
                        Integer.parseInt(results.getString("projectId")),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("created"),
                        results.getString("updated"),
                        results.getString("deadline"),
                        results.getString("assignedTo"),
                        results.getString("status")));
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }

    public  ObservableList<Task> SearchTasks(Integer projectId,String taskTitle) {

        Connection connection = DBConnection.Connector();

        // create list of projects for table insertion
        ObservableList<Task> list = FXCollections.observableArrayList();


        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM TASKS WHERE projectId = " +projectId.toString()
                    + " AND TITLE LIKE '%"+taskTitle+ "%'" );
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                list.add(new Task(
                        Integer.parseInt(results.getString("id")),
                        Integer.parseInt(results.getString("projectId")),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("created"),
                        results.getString("updated"),
                        results.getString("deadline"),
                        results.getString("assignedTo"),
                        results.getString("status")));
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }



    public void deleteTask(Integer taskID) throws SQLException {

        Connection connection = DBConnection.Connector();

        // enable foreign keys to enable cascade
        String statement = "PRAGMA foreign_keys = ON" ;
        PreparedStatement pStatement = connection.prepareStatement(statement);
        pStatement.execute();

        statement = "DELETE FROM TASKS WHERE ID =" +taskID;
        pStatement = connection.prepareStatement(statement);
        pStatement.executeUpdate();

        connection.close();


    }

    public void updateTask(Integer taskID,String taskTitle,String taskDescription
    ,String taskDeadline,String taskStatusBox,String taskAssignMemberList) throws SQLException {
        String statement = "UPDATE TASKS SET title = ?,description = ?,deadline = ?,status= ?,assignedTo =? ,updated=?  WHERE ID = " + taskID.toString();

        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = connection.prepareStatement(statement);

        LocalDate today = LocalDate.now(ZoneId.of("America/Guyana"));


        pStatement.setString(1, taskTitle);
        pStatement.setString(2, taskDescription);
        pStatement.setString(3, taskDeadline);
        pStatement.setString(4,  taskStatusBox);
        pStatement.setString(5, taskAssignMemberList);
        pStatement.setString(6, String.valueOf(today));


        pStatement.executeUpdate();
        connection.close();
    }


    public ObservableList<String> getMemberList(Integer pId) {

        Connection connection = DBConnection.Connector();

        // create list of projects for table insertion
        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT users.username FROM users " +
                    "INNER JOIN projectMembers " +
                    "ON users.username = projectMembers.username " +
                    "WHERE projectMembers.projectId = " + pId.toString());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                list.add((results.getString("username")));
//                System.out.println(results.getString("username"));
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(pId + "from edit task page member observer list");
        return list;
    }



}
