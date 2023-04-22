package taskmanager.taskmanagerapp;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CreateNewProjectController {

    private ObservableList<Project> projectList;
    @FXML
    private TextField projectTitle;

    @FXML
    private TextArea projectDesc;

    @FXML
    private DatePicker projectDeadline;

    public void SetTableData(ObservableList<Project> projectList){
        this.projectList = projectList;
    }
    public void addProject() throws SQLException {

        String title = projectTitle.getText();
        String desc = projectDesc.getText();
        String date = String.valueOf(projectDeadline.getValue());

        // get curr date
        LocalDate today = LocalDate.now( ZoneId.of( "America/Guyana" ) ) ;

        String statement = " INSERT INTO PROJECT (title,description,created,deadline) VALUES ( ? , ? , ? , ?)";
        Connection connection = DBConnection.Connector();
        PreparedStatement pStatement = connection.prepareStatement(statement);

        pStatement.setString(1,title);
        pStatement.setString(2,desc);
        pStatement.setString(3, String.valueOf(today));
        pStatement.setString(4, date);

        pStatement.executeUpdate();

        connection.close();

        System.out.println("Done");


        // clear fields

        projectDesc.clear();
        projectTitle.clear();
        projectDeadline.setValue(null);









    }
}
