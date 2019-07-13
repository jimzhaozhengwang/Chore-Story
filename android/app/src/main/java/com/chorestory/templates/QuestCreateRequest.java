package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestCreateRequest {
    private String title;
    private String description;
    private int reward;
    private List<Long> timestamps;
    private long due;
    @SerializedName("needs_verification")
    private boolean needsVerification;

    public QuestCreateRequest(String title, String description, int reward, long timestamp, long due, boolean needsVerification) {
        this.title = title;
        this.description = description;
        this.reward = reward;
        // Since our recurrences are simple but the server can handle intelligently spaced timestamps,
        //      we only need to send one timestamp here
        this.timestamps = new ArrayList<>();
        if (timestamp != -1) {
            this.timestamps.add(timestamp);
        }
        this.due = due;
        this.needsVerification = needsVerification;
    }
}
