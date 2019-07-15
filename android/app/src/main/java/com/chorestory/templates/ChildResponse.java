package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

public class ChildResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("clan_name")
        private String clanName;
        private int id;
        private int level;
        private String name;
        @SerializedName("xp")
        private int exp;

        public String getClanName() {
            return clanName;
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

        public int getExp() {
            return exp;
        }
    }
}
