package taskmanager.taskmanagerapp;

public class Task {

    private final Integer id;
    private final Integer projectId;

    private final String title;
    private final String description;
    private final String created;
    private final String updated;
    private final String deadline;
    private final String assigned;
    private final String status;

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
