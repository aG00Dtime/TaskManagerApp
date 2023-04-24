package taskmanager.taskmanagerapp;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // dummy stage to facilitate scene changing
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 480, 350));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //change the primary stage's scene
    public void changeScene(String fxml, String title, Integer width, Integer height) throws IOException {

        //load fxml of new scene to change view
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);
        stage.setTitle(title);
        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();

    }
}