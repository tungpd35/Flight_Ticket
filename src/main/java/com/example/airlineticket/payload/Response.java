package com.example.airlineticket.payload;

public class Response {
    private String token;
    private final String type = "Bearer";
    private String email;
    public Response(String token,String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }
}
