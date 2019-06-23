package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountResponse {
    @SerializedName("data")
    @Expose
    private String data;

    public String getAccountData() {

        // TODO: Work on writing a general de-serializer for these data responses

        return data;
    }
}
