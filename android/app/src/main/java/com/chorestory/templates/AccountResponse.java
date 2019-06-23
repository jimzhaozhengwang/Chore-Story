package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountResponse {
    @SerializedName("data")
    @Expose
    private String data;

    public String getAccountData() {
        return data;
    }
}
