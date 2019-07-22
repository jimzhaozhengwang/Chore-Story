package com.chorestory.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenHandler {

    private final String PREFERENCES_KEY = "token";

    private String childCreationToken;
    private String parentRegistrationToken;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * TOKEN SPEC:
     * Authorization: <Parent token>:<Child token>
     */

    public void setChildCreationToken(String token) {
        if (token == null) {
            token = "";
        }
        this.childCreationToken = token;
    }

    public String getChildCreationToken() {
        String childCreationToken = this.childCreationToken;
        this.childCreationToken = ""; // only want it being used once
        return childCreationToken;
    }

    public boolean hasChildCreationToken() {
        return this.childCreationToken != null && !this.childCreationToken.isEmpty();
    }

    public void setParentRegistrationToken(String token) {
        if (token == null) {
            token = "";
        }
        this.parentRegistrationToken = token;
    }

    public String getParentRegistrationToken() {
        String parentRegistrationToken = this.parentRegistrationToken;
        this.parentRegistrationToken = "";
        return parentRegistrationToken;
    }

    public boolean hasParentRegistrationToken() {
        return this.parentRegistrationToken != null && !this.parentRegistrationToken.isEmpty();
    }

    public void setParentToken(String token, Context context) {
        if (token == null) {
            token = "";
        }
        storeToken(token + ":", context);
    }

    public void setChildToken(String token, Context context) {
        if (token == null) {
            token = "";
        }
        storeToken(":" + token, context);
    }

    public String getToken(Context context) {
        createPreferencesObject(context);
        return this.preferences.getString(PREFERENCES_KEY, "");
    }

    public boolean isParentToken(String token) {
        return token != null && !token.isEmpty() && !(token.charAt(0) == ':');
    }

    public boolean isChildToken(String token) {
        return token != null && !token.isEmpty() && token.charAt(0) == ':';
    }

    public void deleteStoredToken(Context context) {
        this.childCreationToken = "";

        createPreferencesObject(context);

        this.editor.clear();

        this.editor.commit();
    }

    private void storeToken(String token, Context context) {
        createPreferencesObject(context);

        this.editor.putString(PREFERENCES_KEY, token);

        this.editor.commit();
    }

    private void createPreferencesObject(Context context) {
        this.preferences = context.getSharedPreferences(
                "com.chorestory.app",
                Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }
}
