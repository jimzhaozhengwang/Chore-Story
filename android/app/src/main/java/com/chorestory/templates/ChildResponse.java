package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChildResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public boolean hasResponse() {
        return (data != null);
    }

    public class Data {
        @SerializedName("clan_name")
        private String clanName;
        private int id;
        private int level;
        private String name;
        @SerializedName("xp")
        private int exp;
        private String username;
        private String type;
        private List<Parent> parents;

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

        public String getUsername() {
            return username;
        }

        public String getType() {
            return type;
        }

        public List<Parent> getParents() {
            return parents;
        }

        public class Parent {
            int id;
            String name;
            int picture;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getPicture() {
                return picture;
            }
        }
    }
}
