package taskmanager.taskmanagerapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreateNewProjectController {


    @FXML
    private TextField projectTitle;

    @FXML
    private TextArea projectDesc;

    @FXML
    private DatePicker projectDeadline;

    public void addProject() throws SQLException {

        String title = projectTitle.getText();
        String desc = projectDesc.getText();
        String date = String.valueOf(projectDeadline.getValue());


        // check text fields
        if (title.isEmpty() || desc.isEmpty() || date.isEmpty()) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Failed");
            a.setContentText("Ensure all fields are filled in!");
            a.show();

        } else {

            ProjectModel addNewProject = new ProjectModel();
            addNewProject.createProject(title,desc,date);

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Success!");
            a.setContentText("New Project Added");
            a.show();

            // clear fields

            projectDesc.clear();
            projectTitle.clear();
            projectDeadline.setValue(null);


        }


    }
}
