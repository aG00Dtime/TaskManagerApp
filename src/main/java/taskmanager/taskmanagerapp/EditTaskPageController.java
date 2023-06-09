package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditTaskPageController implements Initializable {
    private final ObservableList<String> projectStatusList = FXCollections.observableArrayList(
            "In Progress",
            "On hold",
            "Cancelled",
            "Completed");
    public Button taskUpdateButton;
    public TextField taskTitle;
    public TextArea taskDescription;
    public DatePicker taskDeadline;
    public ComboBox<String> taskAssignMemberList;
    public ComboBox<String> taskStatusBox;
    public Integer taskID;
    private String oldTitle, oldDescription, oldProjectDeadline, oldStatus, oldAssigned;
    private ObservableList<String> newMemList;

    public void fillFields(Integer id, String title, String description, String deadline, String assigned, String status) throws Exception {


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
        oldAssigned = assigned;

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

                    TaskModel updateTask = new TaskModel();
                    updateTask.updateTask(
                            taskID,
                            taskTitle.getText(),
                            taskDescription.getText(),
                            taskDeadline.getValue().toString(),
                            taskStatusBox.getValue(),
                            taskAssignMemberList.getValue()
                    );


                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setHeaderText("Success!");
                    successAlert.setContentText("Task details were updated!");
                    successAlert.show();

                    oldTitle = taskTitle.getText();
                    oldDescription = taskDescription.getText();
                    oldProjectDeadline = String.valueOf(taskDeadline.getValue());
                    oldStatus = taskStatusBox.getValue();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setMemberList(Integer pId) {

        TaskModel newMemberList = new TaskModel();

        newMemList = newMemberList.getMemberList(pId);
        taskAssignMemberList.setItems(newMemList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taskStatusBox.setItems(projectStatusList);


    }
}
