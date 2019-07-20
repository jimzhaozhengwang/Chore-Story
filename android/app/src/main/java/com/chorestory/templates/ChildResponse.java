package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChildResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public Boolean hasResponse() {
        return (data != null);
    }

    public class Data {
        @SerializedName("clan_name")
        private String clanName;
        private Integer id;
        private Integer level;
        private String name;
        @SerializedName("xp")
        private Integer exp;
        private String username;
        private String type;
        private List<Parent> parents;

        public String getClanName() {
            return clanName;
        }

        public Integer getId() {
            return id;
        }

        public Integer getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }

        public Integer getExp() {
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
            Integer id;
            String name;
            Integer picture;

            public Integer getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public Integer getPicture() {
                return picture;
            }
        }
    }
}
