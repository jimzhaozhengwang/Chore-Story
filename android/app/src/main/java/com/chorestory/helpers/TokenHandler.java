package com.chorestory.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenHandler  {

    private String preferencesKey = "token";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * TOKEN SPEC:
     * Authorization: <Parent token>:<Child token>
     */

    public void setParentToken(String token, Context context) {
        storeToken(token + ":", context);
    }
    public void setChildToken(String token, Context context) {
        storeToken(":" + token, context);
    }

    public String getToken(Context context) {
        createPreferencesObject(context);
        String foundToken = this.preferences.getString(preferencesKey, null);
        if (foundToken == null) {
            foundToken = "";
        }
        return foundToken;
    }

    public boolean isParentToken(String token) {
        return !(token.charAt(0) == ':');
    }

    private void storeToken(String token, Context context) {
        createPreferencesObject(context);

        this.editor.putString(preferencesKey, token);

        this.editor.commit();
    }

    private void createPreferencesObject(Context context) {
        this.preferences = context.getSharedPreferences(
                "com.chorestory.app",
                Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }
}
