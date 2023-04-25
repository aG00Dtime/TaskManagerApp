package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditTaskPageController implements Initializable  {
    public Button taskUpdateButton;
    public TextField taskTitle;
    public TextArea taskDescription;
    public DatePicker taskDeadline;
    public ComboBox taskAssignMemberList;
    public ComboBox taskStatusBox;

    public Integer taskID;
    private String oldTitle,oldDescription,oldProjectDeadline,oldStatus,oldAssigned;

    private ObservableList<String> newMemList;

    public void fillFields(Integer id, String title, String description, String deadline,String assigned, String status) throws Exception {


        taskID = id;

        oldTitle = title;
        taskTitle.setText(title);

        taskDescription.setText(description);
        oldDescription = description;

        taskDeadline.setValue(LocalDate.parse(deadline));
        oldProjectDeadline = String.valueOf(LocalDate.parse(deadline));

        taskStatusBox.setValue(status);
        oldStatus = status;

        taskAssignMemberList.setValue(assigned);
        oldAssigned=assigned;

    }
    public void updateProject() {

        // check if anything changes
        if (taskTitle.getText().equals(oldTitle)
                && taskDescription.getText().equals(oldDescription)
                && taskDeadline.getValue().toString().equals(oldProjectDeadline)
                && taskStatusBox.getValue().equals(oldStatus) && taskAssignMemberList.getValue().equals(oldAssigned)
        ) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Failed");
            a.setContentText("No changes made! Make a change and try again.");
            a.show();

        } else {

            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Confirm Changes");
            a.setContentText("Are you sure you want to update the task?");


            Optional<ButtonType> result = a.showAndWait();

            if (result.get() == ButtonType.OK) {

                try {

                    String statement = "UPDATE TASKS SET title = ?,description = ?,deadline = ?,status= ?,assignedTo =? ,updated=?  WHERE ID = " + taskID.toString();

                    Connection connection = DBConnection.Connector();
                    PreparedStatement pStatement = connection.prepareStatement(statement);

                    LocalDate today = LocalDate.now(ZoneId.of("America/Guyana"));


                    pStatement.setString(1, taskTitle.getText());
                    pStatement.setString(2, taskDescription.getText());
                    pStatement.setString(3, String.valueOf(taskDeadline.getValue()));
                    pStatement.setString(4, (String) taskStatusBox.getValue());
                    pStatement.setString(5, (String) taskAssignMemberList.getValue());
                    pStatement.setString(6, String.valueOf(today));


                    pStatement.executeUpdate();
                    connection.close();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setHeaderText("Success!");
                    successAlert.setContentText("Task details were updated!");
                    successAlert.show();

                    oldTitle = taskTitle.getText();
                    oldDescription = taskDescription.getText();
                    oldProjectDeadline = String.valueOf(taskDeadline.getValue());
                    oldStatus = (String) taskStatusBox.getValue();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private final ObservableList<String> projectStatusList = FXCollections.observableArrayList(
            "In Progress",
            "On hold",
            "Cancelled",
            "Completed");


    public static ObservableList<String> getMemberList(Integer pId) {

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

    public void setMemberList(Integer pId){
        newMemList=(getMemberList(pId));
        taskAssignMemberList.setItems(newMemList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taskStatusBox.setItems(projectStatusList);


    }
}
