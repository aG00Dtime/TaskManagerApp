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


            TaskModel addNewTask = new TaskModel();
            addNewTask.addTask(projectId,title,desc,date);

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Success!");
            a.setContentText("New Task Added");
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
