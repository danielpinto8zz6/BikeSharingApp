package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

public class EmailToken {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    public EmailToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
