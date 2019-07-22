package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

public class CompleteQuestResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public boolean hasResponse() {
        return data != null;
    }

    public class Data {
        @SerializedName("completed_now")
        private boolean completedNow;
        @SerializedName("lvled_up")
        private boolean lvledUp;
        @SerializedName("qst")
        private Quest quest;

        public boolean isCompletedNow() {
            return completedNow;
        }

        public boolean isLvledUp() {
            return lvledUp;
        }

        public Quest getQuest() {
            return quest;
        }

        public class Quest {
            @SerializedName("completed_on")
            private String completedOn;
            private String description;
            private double due;
            private int id;
            @SerializedName("needs_verification")
            private boolean needsVerification;
            private boolean recurring;
            private int reward;
            private String title;
            @SerializedName("verified_on")
            private String verifiedOn;

            public String getCompletedOn() {
                return completedOn;
            }

            public String getDescription() {
                return description;
            }

            public double getDue() {
                return due;
            }

            public int getId() {
                return id;
            }

            public boolean getNeedsVerification() {
                return needsVerification;
            }

            public boolean getRecurring() {
                return recurring;
            }

            public int getReward() {
                return reward;
            }

            public String getTitle() {
                return title;
            }

            public String getVerifiedOn() {
                return verifiedOn;
            }
        }
    }
}
