package entities;

import java.util.ArrayList;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class User {
    private String username;
    private String accountLevel;
    private String email;
    private ArrayList<String> currentVideos;

    public User(String username, String accountLevel, String email){
        this.username = username;
        this.accountLevel = accountLevel;
        currentVideos = new ArrayList<>();
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public ArrayList<String> getCurrentVideos() {
        return currentVideos;
    }

    public void addProcessingVideos(String id) {
        currentVideos.add(id);
    }
}
