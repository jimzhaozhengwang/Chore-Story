package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

public class QuestCreateResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("completed_now")
        private Boolean completedNow;
        @SerializedName("lvled_up")
        private Boolean lvledUp;
        @SerializedName("qst")
        private Quest quest;

        public Quest getQuest() {
            return quest;
        }

        public class Quest {
            @SerializedName("completed_on")
            private String completedOn;
            private String description;
            private Double due;
            private Integer id;
            @SerializedName("needs_verification")
            private Boolean needsVerification;
            private Boolean recurring;
            private Integer reward;
            private String title;
            @SerializedName("verified_on")
            private String verifiedOn;

            public String getCompletedOn() {
                return completedOn;
            }

            public String getDescription() {
                return description;
            }

            public Double getDue() {
                return due;
            }

            public Integer getId() {
                return id;
            }

            public Boolean getNeedsVerification() {
                return needsVerification;
            }

            public Boolean getRecurring() {
                return recurring;
            }

            public Integer getReward() {
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
