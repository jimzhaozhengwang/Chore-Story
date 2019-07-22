package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public Boolean hasResponse() {
        return (data != null);
    }

    public class Data {
        List<Child> children;
        String email;
        Integer id;
        String name;
        @SerializedName("clan_name")
        String clanName;
        String type;
        Integer picture;

        public Integer getId() {
            return id;
        }

        public List<Child> getChildren() {
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

        public Integer getPicture() { return picture; }

        public class Child {
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
