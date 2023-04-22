package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProjectPageController implements Initializable {

    @FXML
    private Button prNewProjectButton;

    @FXML
    private Button prSearchButton;

    @FXML
    private TextField prSearchTextField;

    @FXML
    private TableColumn<Project, String> projectActions;

    @FXML
    private  TableColumn<Project, String> projectCreated;

    @FXML
    private  TableColumn<Project, String> projectDeadline;

    @FXML
    private  TableColumn<Project, String> projectDescription;

    @FXML
    private  TableColumn<Project, Integer> projectId;

    @FXML
    private  TableColumn<Project, String> projectName;

    @FXML
    private  TableColumn<Project, String> projectStatus;

    @FXML
    private  TableView<Project> projectsListTable;

    private AnchorPane myAnchorPane;


    public void userLogout(ActionEvent event) throws IOException{

        Main main = new Main();
        main.changeScene("loginPage.fxml","Login",480,395);

    }
    // add a new project
    public void createProject(ActionEvent event) throws IOException{

        // throw up a new window to add project details
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewProjectPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Project");
            stage.setScene(new Scene(root1,600,500));
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static ObservableList<Project> getProject() {

        Connection connection = DBConnection.Connector();

        // create list of projects for table insertion
        ObservableList<Project> list = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PROJECT");
            ResultSet results = statement.executeQuery();

            while (results.next()){
                list.add(new Project (
                        Integer.parseInt(results.getString("id")),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("created"),
                        results.getString("deadline"),
                        results.getString("status") ));
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }

    public void refreshProjects(){

        ObservableList<Project> projectList = getProject();
        projectsListTable.setItems(projectList);

    }

    @Override
    public void initialize (URL ulr,ResourceBundle rb){

        // load project list into table
        projectId.setCellValueFactory(new PropertyValueFactory<Project,Integer>("id"));
        projectName.setCellValueFactory(new PropertyValueFactory<Project,String>("title"));
        projectDescription.setCellValueFactory(new PropertyValueFactory<Project,String>("description"));
        projectCreated.setCellValueFactory(new PropertyValueFactory<Project,String>("created"));
        projectDeadline.setCellValueFactory(new PropertyValueFactory<Project,String>("deadline"));
        projectStatus.setCellValueFactory(new PropertyValueFactory<Project,String>("status"));

        // custom Actions buttons
       Callback<TableColumn<Project,String>,TableCell<Project,String>> cellFactory =(param) ->{

           final TableCell<Project,String> cell = new TableCell<Project,String>(){

               @Override
               public void updateItem(String item,boolean empty){
                   super.updateItem(item,empty);

                   if (empty){
                       setGraphic(null);
                       setText(null);
                   }

                   else{

                       final Button editButton = new Button("Edit");
                       final Button deleteButton = new Button("Delete");

                       // trigger edit
                       editButton.setOnAction(event->{

                           Project project=getTableView().getItems().get(getIndex());


                           //testing
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setContentText("You clicked \n "+project.getTitle());
                            a.show();

                       });
                                //trigger delete
                               deleteButton.setOnAction(event->{

                                   Project project=getTableView().getItems().get(getIndex());

                                   // confirmation dialogue
                                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                   alert.initModality(Modality.APPLICATION_MODAL);

                                   alert.getDialogPane().setHeaderText("Delete?");
                                   alert.getDialogPane().setContentText("Are you sure you want to delete this project?\nTitle : " +project.getTitle());

                                   Optional<ButtonType> result = alert.showAndWait();

                                   if (result.get() == ButtonType.OK){

                                       try {

                                           String statement = "DELETE FROM PROJECT WHERE ID =" + project.getId();

                                           Connection connection= DBConnection.Connector();
                                           PreparedStatement pStatement = connection.prepareStatement(statement);
                                           pStatement.executeUpdate();
                                           // refresh project list after operation
                                           refreshProjects();

                                       } catch (SQLException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                               }
                       );

                       //add new buttons to cell
                       HBox actionButtons = new HBox(editButton, deleteButton);
                       actionButtons.setStyle("-fx-alignment:center");
                       HBox.setMargin(deleteButton, new Insets(2, 2, 0, 3));
                       HBox.setMargin(editButton, new Insets(2, 3, 0, 2));

                       setGraphic(actionButtons);
                       setText(null);
                   }


               }

           };

           return cell;
       };

       // add custom buttons to action cell
        projectActions.setCellFactory(cellFactory);

        refreshProjects();

    }
}