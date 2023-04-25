package taskmanager.taskmanagerapp;

public class Users {

    private final String username;
    private final String email;


    public Users(String username, String email) {
        this.username = username;
        this.email = email;

    }
    public String getUsername() {

        return username;
    }

    public String getEmail() {
        return email;
    }
}
