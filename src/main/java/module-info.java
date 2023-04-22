module taskmanager.taskmanagerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens taskmanager.taskmanagerapp to javafx.fxml;
    exports taskmanager.taskmanagerapp;
}