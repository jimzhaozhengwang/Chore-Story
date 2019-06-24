package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendsResponse {
    @SerializedName("data")
    @Expose
    private List<Integer> data;

    public List<Integer> getData() {
        return data;
    }
}
