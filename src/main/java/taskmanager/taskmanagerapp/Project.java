package taskmanager.taskmanagerapp;

import javafx.scene.control.Button;

public class Project {

    private final String title;
    private final String description;

    private final String created;
    private final String deadline;

    private final String status;

    private final Integer id;



    public Project(Integer id, String title, String description, String created, String deadline, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
        this.deadline = deadline;
        this.status = status;

    }

    public Integer getId() {

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

}
