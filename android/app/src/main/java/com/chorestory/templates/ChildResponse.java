package com.chorestory.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildResponse {

    @SerializedName("data")
    @Expose
    private data data;

    public ChildResponse.data getData() {
        return data;
    }

    class data {
        @SerializedName("clan_name")
        private String clan_name;
        @SerializedName("id")
        private int id;
        @SerializedName("level")
        private int level;
        @SerializedName("name")
        private String name;
        @SerializedName("xp")
        private int xp;

        public String getClan_name() {
            return clan_name;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }

        public int getXp() {
            return xp;
        }
    }
}
