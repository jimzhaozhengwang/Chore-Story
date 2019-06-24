package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountResponse {
    @SerializedName("data")
    @Expose
    private data data;

    public AccountResponse.data getData() {
        return data;
    }

    class data {
        @SerializedName("children")
        List<Integer> children;
        @SerializedName("email")
        String email;
        @SerializedName("id")
        Integer id;
        @SerializedName("name")
        String name;
        @SerializedName("type")
        String type;

        public List<Integer> getChildren() {
            return children;
        }

        public String getEmail() {
            return email;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

}
