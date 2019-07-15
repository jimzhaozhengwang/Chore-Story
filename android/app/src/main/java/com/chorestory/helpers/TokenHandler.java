package com.chorestory.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenHandler {

    private final String PREFERENCES_KEY = "token";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * TOKEN SPEC:
     * Authorization: <Parent token>:<Child token>
     */

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
