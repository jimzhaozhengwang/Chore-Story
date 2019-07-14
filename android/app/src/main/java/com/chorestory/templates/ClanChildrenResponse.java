package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClanChildrenResponse {

    @SerializedName("data")
    private List<Children> children;

    public List<Children> getChildren() {
        return children;
    }

    public Boolean hasResponse() {
        return (children != null);
    }

    public class Children {
        @SerializedName("clan_name")
        String clanName;
        Integer id;
        Integer level;
        String name;
        String username;
        @SerializedName("xp")
        Integer exp;
        List<Parent> parents;

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

        public String getUsername() {
            return username;
        }

        public Integer getExp() {
            return exp;
        }

        public List<Parent> getParent() {
            return parents;
        }

        public class Parent {
            Integer id;
            String name;

            public Integer getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}
