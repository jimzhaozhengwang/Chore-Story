package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestModifyRequest {
    private int cid;
    private String title;
    private String description;
    private int reward;
    @SerializedName("needs_verification")
    private boolean needsVerification;
    private List<Long> timestamps;
    private long due;

    public QuestModifyRequest(int cid, String title, String description, int reward, boolean needsVerification, long timestamp, long due) {
        this.cid = cid;
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
