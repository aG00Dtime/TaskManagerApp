package taskmanager.taskmanagerapp;

public class Task {

    private Integer id;
    private Integer projectId;

    private String title;
    private String description;
    private String created;
    private String updated;
    private String deadline;
    private String assigned;
    private String status;

    public Task(Integer id, Integer projectId,
                String title, String description,
                String created, String updated,
                String deadline, String assigned, String status) {

        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.created = created;
        this.updated = updated;
        this.deadline = deadline;
        this.assigned = assigned;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProjectId() {
        return projectId;
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

    public String getUpdated() {
        return updated;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAssigned() {
        return assigned;
    }

    public String getStatus() {
        return status;
    }
}
