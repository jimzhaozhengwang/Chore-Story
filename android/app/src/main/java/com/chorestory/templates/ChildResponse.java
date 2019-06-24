package com.chorestory.templates;

public class ChildResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        private String clan_name;
        private int id;
        private int level;
        private String name;
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
