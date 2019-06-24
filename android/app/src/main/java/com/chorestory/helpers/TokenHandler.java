package com.chorestory.helpers;

public class TokenHandler {

    /**
     * TOKEN SPEC:
     * Authorization: <Parent token>:<Child token>
     */
    private String token = ":";

    // This will eventually be setup to pull from some storage system
    // Undecided ActivityManager/keystore
    public void setParentToken(String token) {
        this.token = token + ":";
    }
    public void setChildToken(String token) {
        this.token = ":" + token;
    }

    public String getToken() {
        return this.token;
    }

}
