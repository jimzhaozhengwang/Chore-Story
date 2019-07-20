package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetQuestsResponse {
    @SerializedName("data")
    List<Quest> quests;

    public Boolean hasResponse() {
        return (quests != null);
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public class Quest {
        @SerializedName("completed_on")
        String completedOn;
        String description;
        Float due;
        Integer id;
        @SerializedName("needs_verification")
        Boolean needsVerification;
        @SerializedName("next_occurrence")
        Float nextOccurrence;
        Owner owner;
        Boolean recurring;
        Integer reward;
        String title;
        @SerializedName("verified_on")
        String verifiedOn;

        public String getCompletedOn() {
            return completedOn;
        }

        public String getDescription() {
            return description;
        }

        public Integer getDue() {
            return Math.round(due);
        }

        public Integer getId() {
            return id;
        }

        public Boolean getNeedsVerification() {
            return needsVerification;
        }

        public Integer getNextOccurrence() {
            return Math.round(nextOccurrence);
        }

        public Owner getOwner() {
            return owner;
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

        public class Owner {
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
