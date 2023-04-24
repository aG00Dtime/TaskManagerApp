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
import java.util.ResourceBundle;

public class EditTaskPageController implements Initializable  {
    public Button taskUpdateButton;
    public TextField taskTitle;
    public TextArea taskDescription;
    public DatePicker taskDeadline;
    public ComboBox taskAssignMemberList;
    public ComboBox taskStatusBox;

    public Integer projectId;
    private String oldTitle,oldDescription,oldProjectDeadline,oldStatus,oldAssigned;

    private ObservableList<String> newMemList;

    public void fillFields(Integer id, String title, String description, String deadline,String assigned, String status) throws Exception {

        projectId = id;

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
