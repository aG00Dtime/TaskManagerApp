package taskmanager.taskmanagerapp;

import javafx.scene.control.Button;

public class Project {

    private String title;
    private String description;

    private String created;
    private String deadline;

    private String status;

    private Integer id;

    private Button editButton;
    private Button deleteButton;




    public Project(Integer id, String title, String description, String created, String deadline, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
        this.deadline = deadline;
        this.status = status;
        this.editButton = new Button("Edit");
        this.deleteButton = new Button("Delete");
    }

    public Integer getId(){

        return id;
    }
    public String getTitle() {

        return title;
    }

    public String getDescription() {

        return description;
    }

    public String getCreated() {

        return created;
    }

    public String getDeadline() {

        return deadline;
    }

    public String getStatus() {

        return status;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}
