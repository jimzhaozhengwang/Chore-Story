package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleStringResponse {

    @SerializedName("data")
    @Expose
    private String response;

    public Boolean hasResponse() {
        return (response != null && !response.isEmpty());
    }

    public String getResponse() {
        return response;
    }

}
