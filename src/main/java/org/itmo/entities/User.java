package org.itmo.entities;

import com.mysql.cj.util.StringUtils;

public class User {
    private String username;
    private String password;
    private Boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public User(String username, String password, Boolean isAdmin) {
        this(username, password);
        this.isAdmin = isAdmin;
    }

    public User(User user)
    {
        this.username = user.username;
        this.password = user.password;
        this.isAdmin = user.isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
