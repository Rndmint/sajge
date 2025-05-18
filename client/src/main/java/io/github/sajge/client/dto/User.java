package io.github.sajge.client.dto;

public class User {
    private String username;
    private String displayName;
    private String password;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String d) {
        this.displayName = d;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String t) {
        this.token = t;
    }
}
