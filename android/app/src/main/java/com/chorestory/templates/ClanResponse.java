package com.chorestory.templates;

import java.util.List;

public class ClanResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public Boolean hasResponse() {
        return (data != null);
    }

    public class Data {
        Integer id;
        String name;
        List<Parent> parents;
        List<Children> children;

        public Integer getId() {
            return id;
        }

        public String getClanName() {
            return name;
        }

        public List<Parent> getParents() {
            return parents;
        }

        public List<Children> getChildren() {
            return children;
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

        public class Children {
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
