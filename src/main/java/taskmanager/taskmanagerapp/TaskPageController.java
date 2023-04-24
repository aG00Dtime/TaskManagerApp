package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TaskPageController implements Initializable {


    public Integer projectId;


    @FXML
    public Button addTask;

    @FXML
    public Button addTaskButton;

    @FXML
    private DatePicker taskDeadline;

    @FXML
    private TextArea taskDescription;

    @FXML
    private TextField taskTitle;


    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> taskTableActions;

    @FXML
    private TableColumn<Task, String> taskTableAssigned;

    @FXML
    private TableColumn<Task, String> taskTableDeadline;
    @FXML
    private TableColumn<Task, String> taskTableCreated;

    @FXML
    private TableColumn<Task, String> taskTableDescription;

    @FXML
    private TableColumn<Task, String> taskTableName;

    @FXML
    private TableColumn<Task, String> taskTableStatus;

    @FXML
    private TableColumn<Task, String> taskTableUpdated;

    public void getProjectId(Integer id){

        projectId = id;
        refreshTaskList();


    }

    public void createTask() {

        refreshTaskList();
        // throw up a new window to add project details
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewTaskPage.fxml"));
            Parent root1 = fxmlLoader.load();


            // pass value to window
            CreateNewTaskController passProjectId = fxmlLoader.getController();
            try {
                passProjectId.getProjectId(
                        projectId
                );

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Task");
            stage.setResizable(false);
            stage.setScene(new Scene(root1, 550, 400));
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public  ObservableList<Task> getTasks() {

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



    public void refreshTaskList(){

        ObservableList<Task> projectTasks = getTasks();
        taskTable.setItems(projectTasks);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        taskTableName.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));
        taskTableDescription.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        taskTableCreated.setCellValueFactory(new PropertyValueFactory<Task, String>("created"));
        taskTableUpdated.setCellValueFactory(new PropertyValueFactory<Task, String>("updated"));
        taskTableDeadline.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));
        taskTableAssigned.setCellValueFactory(new PropertyValueFactory<Task, String>("assigned"));
        taskTableStatus.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));





    }

}

