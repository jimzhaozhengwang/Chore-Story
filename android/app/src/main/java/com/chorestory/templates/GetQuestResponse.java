package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;
import com.chorestory.templates.GetQuestsResponse.Quest;

public class GetQuestResponse {
    @SerializedName("data")
    Quest quest;

    public Boolean hasResponse() {
        return (quest != null);
    }

    public Quest getQuest() {
        return quest;
    }
}
