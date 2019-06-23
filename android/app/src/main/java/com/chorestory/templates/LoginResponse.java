package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    private String token;

    public Boolean hasToken() {
        return (token != null && !token.isEmpty());
    }

    public String getToken() {
        return token;
    }

}
