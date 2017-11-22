package entities;

import java.util.ArrayList;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class User {
    private String username;
    private String accountLevel;
    private String email;
    private int currentVideos;

    public User(String username, String accountLevel, String email){
        this.username = username;
        this.accountLevel = accountLevel;
        currentVideos = 0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCurrentVideos() {
        return currentVideos;
    }

    public void setCurrentVideos(int list){
        this.currentVideos = list;
    }

}
