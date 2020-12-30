package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    private String szToken;

    public Token(String szToken) {
        this.szToken = szToken;
    }

    public String getSzToken() {
        return szToken;
    }

    public void setSzToken(String szToken) {
        this.szToken = szToken;
    }
}
