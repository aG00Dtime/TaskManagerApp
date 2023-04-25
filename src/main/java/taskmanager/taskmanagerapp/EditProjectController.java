package taskmanager.taskmanagerapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditProjectController implements Initializable {

    private final ObservableList<String> projectStatusList = FXCollections.observableArrayList(
            "In Progress",
            "On hold",
            "Cancelled",
            "Completed");
    @FXML
    private TableColumn<Users, String> actions;
    @FXML
    private Button addMemberButton;
    @FXML
    private TableColumn<Users, String> memberEmail;
    @FXML
    private TableColumn<Users, String> memberName;
    @FXML
    private DatePicker projectDeadline;
    @FXML
    private TextArea projectDescription;
    @FXML
    private ComboBox<String> projectStatusBox;
    @FXML
    private TextField projectTitle;
    @FXML
    private ComboBox<String> projectUserList;
    @FXML
    private TableView<Users> teamTable;
    private Integer projectID;
    private String oldTitle, oldDescription, oldProjectDeadline, oldStatus;

    // user list
    public static ObservableList<String> getUsers() {

        Connection connection = DBConnection.Connector();

        // create list of projects for table insertion
        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT USERNAME FROM USERS");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                list.add((results.getString("username")));
//                System.out.println(results.getString("username"));
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void fillFields(Integer id, String title, String description, String deadline, String status) throws Exception {

        projectID = id;

        oldTitle = title;
        projectTitle.setText(title);

        projectDescription.setText(description);
        oldDescription = description;

        projectDeadline.setValue(LocalDate.parse(deadline));
        oldProjectDeadline = String.valueOf(LocalDate.parse(deadline));

        projectStatusBox.setValue(status);
        oldStatus = status;

        // get the member list
        refreshMemberList(projectID);
    }

    //DONE: add update method
    //DONE: add member method
    //DONE: add combo box options using lists

    public void updateProject() {

        // check if anything changes
        if (projectTitle.getText().equals(oldTitle)
                && projectDescription.getText().equals(oldDescription)
                && projectDeadline.getValue().toString().equals(oldProjectDeadline)
                && projectStatusBox.getValue().equals(oldStatus)
        ) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Failed");
            a.setContentText("No changes made! Make a change and try again.");
            a.show();

        } else {

            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Confirm Changes");
            a.setContentText("Are you sure you want to update the project?");


            Optional<ButtonType> result = a.showAndWait();

            if (result.get() == ButtonType.OK) {

                try {

                    String statement = "UPDATE PROJECT SET title = ?,description = ?,deadline = ?,status= ? WHERE ID = " + projectID.toString();

                    Connection connection = DBConnection.Connector();
                    PreparedStatement pStatement = connection.prepareStatement(statement);

                    pStatement.setString(1, projectTitle.getText());
                    pStatement.setString(2, projectDescription.getText());
                    pStatement.setString(3, String.valueOf(projectDeadline.getValue()));
                    pStatement.setString(4, projectStatusBox.getValue());

                    pStatement.executeUpdate();
                    connection.close();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setHeaderText("Success!");
                    successAlert.setContentText("Project details were updated!");
                    successAlert.show();

                    oldTitle = projectTitle.getText();
                    oldDescription = projectDescription.getText();
                    oldProjectDeadline = String.valueOf(projectDeadline.getValue());
                    oldStatus = projectStatusBox.getValue();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void addTeamMember() throws SQLException {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.getDialogPane().setHeaderText("Add New Member?");
        alert.getDialogPane().setContentText("Are you sure you want to add " + projectUserList.getValue().toUpperCase() + " to the list of members?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            ProjectModel addMemberNew = new ProjectModel();
            addMemberNew.addNewMember(projectID,projectUserList.getValue());

            projectUserList.getSelectionModel().clearSelection();

            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.initModality(Modality.APPLICATION_MODAL);
            alertSuccess.setHeaderText("Success!");
            alertSuccess.setContentText("User added!");
            alertSuccess.show();

        } else {

            Alert alertCancelled = new Alert(Alert.AlertType.INFORMATION);
            alertCancelled.initModality(Modality.APPLICATION_MODAL);
            alertCancelled.setHeaderText("Cancelled!");
            alertCancelled.setContentText("User not added!");
            alertCancelled.show();
        }
        refreshMemberList(projectID);
    }

    public void refreshMemberList(Integer PID) {

        ProjectModel modelMemberList = new ProjectModel();
        ObservableList<Users> memberList = modelMemberList.getProjectMembers(PID);
        teamTable.setItems(memberList);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        ObservableList<Users> teamMemberList = getProjectMembers();
        ObservableList<String> userList = getUsers();

        memberName.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));
        memberEmail.setCellValueFactory(new PropertyValueFactory<Users, String>("email"));

        // populate combo box options
        projectStatusBox.setItems(projectStatusList);
        projectUserList.setItems(userList);

        // custom Actions buttons
        Callback<TableColumn<Users, String>, TableCell<Users, String>> cellFactory = (param) -> {

            final TableCell<Users, String> cell = new TableCell<Users, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        final Button deleteButton = new Button("Delete");


                        //trigger delete
                        deleteButton.setOnAction(event -> {

                            Users user = getTableView().getItems().get(getIndex());
                            //Done: DELETE MEMBER BUTTON easy fix

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.initModality(Modality.APPLICATION_MODAL);

                            alert.getDialogPane().setHeaderText("Remove Team Member?");
                            alert.getDialogPane().setContentText("Are you sure you want to remove" + user.getUsername() + " from the list of members?");

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == ButtonType.OK) {


                                try{

                                    ProjectModel deleteMember = new ProjectModel();
                                    deleteMember.deleteProjectMembers(projectID,user.getUsername());

                                    projectUserList.getSelectionModel().clearSelection();

                                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                                    alertSuccess.initModality(Modality.APPLICATION_MODAL);
                                    alertSuccess.setHeaderText("Success!");
                                    alertSuccess.setContentText("Removed Member!");
                                    alertSuccess.show();}

                                catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }


                            } else {

                                Alert alertCancelled = new Alert(Alert.AlertType.INFORMATION);
                                alertCancelled.initModality(Modality.APPLICATION_MODAL);
                                alertCancelled.setHeaderText("Cancelled!");
                                alertCancelled.setContentText("Not Removed!");
                                alertCancelled.show();
                            }
                            refreshMemberList(projectID);
                        });

                        //add new buttons to cell
                        HBox actionButtons = new HBox(deleteButton);
                        actionButtons.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteButton, new Insets(2, 2, 0, 3));

                        setGraphic(actionButtons);
                        setText(null);
                    }

                }

            };

            return cell;
        };

        actions.setCellFactory(cellFactory);

    }
}




