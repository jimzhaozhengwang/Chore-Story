package com.chorestory.templates;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        List<Float> timestamps;

        public Integer getCompletedOn() {
            return stringToTimestamp(completedOn);
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
            if (nextOccurrence != null) {
                return Math.round(nextOccurrence);
            } else {
                return -1;
            }
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

        public Integer getVerifiedOn() {
            return stringToTimestamp(verifiedOn);
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

        public Integer getTimestamp() {
            if (getRecurring() && timestamps.size() >= 1) {
                return Math.round(timestamps.get(0));
            }
            return -1;
        }

        private int stringToTimestamp(String str) {
            if (str == null) return -1;
            String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = new Date();
            try {
                date = format.parse(str);
            } catch (ParseException e) {
                Log.d("BUG", "Parse exception: ");
                Log.d("BUG", e.getMessage());
            }
            return (int)(date.getTime() / 1000);
        }
    }
}
