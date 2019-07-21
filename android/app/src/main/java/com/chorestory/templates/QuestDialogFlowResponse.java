package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

public class QuestDialogFlowResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public Boolean hasResponse() {
        return (data != null);
    }

    public class Data {
        private String child;
        @SerializedName("experience_point")
        private Object exp;
        private String quest;
        private Time time;


        public String getChild() {
            return child == null ? "" : child;
        }

        public boolean hasChild() {
            return child != null && !child.equals("");
        }

        public Object getExp() {
            return exp == null ? 0 : exp;
        }

        public boolean hasExp() {
            if (exp instanceof String) {
                return false;
            }
            return exp != null && !exp.equals("");
        }

        public String getQuest() {
            return quest == null ? "" : quest;
        }

        public boolean hasQuest() {
            return quest != null && !quest.equals("");
        }

        public Time getTime() {
            return time;
        }

        public class Time {
            private String endDateTime;
            private String startDateTime;

            private TimeTemplate formatTime(String time) {
                if (time == null || time.equals("")) {
                    return null;
                }

                return new TimeTemplate(time, "yyyy-MM-dd'T'HH:mm:ss'Z'");
            }

            public TimeTemplate getEndDateTime() {
                return formatTime(endDateTime);
            }

            public TimeTemplate getStartDateTime() {
                return formatTime(startDateTime);
            }
        }
    }
}
