package com.chorestory.helpers;

public class TokenHandler {

    private String authToken = null;

    // This will eventually be setup to pull from some storage system
    // Undecided ActivityManager/keystore
    public void setToken(String token) {
        this.authToken = token + ":";
    }

    public String getToken() {
        return this.authToken;
    }

}
