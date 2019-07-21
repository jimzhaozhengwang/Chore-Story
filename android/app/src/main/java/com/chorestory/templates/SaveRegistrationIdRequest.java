package com.chorestory.templates;

import com.google.gson.annotations.SerializedName;

public class SaveRegistrationIdRequest {
    @SerializedName("registration_id")
    private String registrationId;

    public SaveRegistrationIdRequest(String registrationId) {
        this.registrationId = registrationId;
    }
}
