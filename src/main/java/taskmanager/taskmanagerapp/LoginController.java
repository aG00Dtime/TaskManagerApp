package taskmanager.taskmanagerapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Label lgStatus;

    @FXML
    private TextField lgUsername;

    @FXML
    private PasswordField lgPassword;

    // trigger the user login func
    public void loginUser(ActionEvent event) throws IOException, SQLException {

        checkLogin();

    }

    @FXML
    // check user login information in the database
    private void checkLogin() throws IOException, SQLException {

        // grab username and password from login form
        String FormUsername = lgUsername.getText().toLowerCase();
        String FormPassword = lgPassword.getText();
        // check user credentials in db // not being encrypted atm
        // TODO: add hash  in db and form

        if (!FormPassword.isEmpty() && !FormUsername.isEmpty()){

            // connect to db
            Connection connection = DBConnection.Connector();

            PreparedStatement getUsers = connection.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?");
            getUsers.setString(1, FormUsername);
            getUsers.setString(2, FormPassword);
            // grab results from db
            ResultSet result = getUsers.executeQuery();


            // print username
            if (result.next()) {

                Main main = new Main();
                main.changeScene("ProjectPage.fxml","Projects",920,710);
                System.out.println(result.getString("username"));
                connection.close();


            }
            else {
                lgStatus.setText("Incorrect Login Details.");
            }

        }
        else {
            lgStatus.setText("Ensure all fields are filled in.");
        }

    }
}
