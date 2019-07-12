package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        List<Integer> children;
        String email;
        Integer id;
        String name;
        @SerializedName("clan_name")
        String clanName;
        String type;

        public Integer getId() {
            return id;
        }

        public List<Integer> getChildren() {
            return children;
        }

        public String getClanName() {
            return clanName;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
