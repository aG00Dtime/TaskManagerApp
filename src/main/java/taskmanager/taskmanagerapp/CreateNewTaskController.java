package taskmanager.taskmanagerapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreateNewTaskController {

    @FXML
    private Button addTaskButton;

    @FXML
    private DatePicker taskDeadline;

    @FXML
    private TextArea taskDescription;

    @FXML
    private TextField taskTitle;


    public Integer projectId;

    public void getProjectId(Integer id) {

        projectId = id;

    }

    public void addTask() throws SQLException {

        String title = taskTitle.getText();
        String desc = taskDescription.getText();
        String date = String.valueOf(taskDeadline.getValue());


        // check text fields
        if (title.isEmpty() || desc.isEmpty() || date.isEmpty()) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Failed");
            a.setContentText("Ensure all fields are filled in!");
            a.show();

        } else {

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

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Success!");
            a.setContentText("New Project Added");
            a.show();

            // clear fields

            taskTitle.clear();
            taskDescription.clear();
            taskDeadline.setValue(null);


        }


    }

    public void getProjectId(ActionEvent actionEvent) {

        System.out.println("Create task controller " +projectId);
    }
}
