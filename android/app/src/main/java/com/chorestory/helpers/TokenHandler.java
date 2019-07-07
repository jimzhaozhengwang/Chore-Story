package com.chorestory.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenHandler  {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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
        return this.preferences.getString("token", null);
    }

    public boolean isParentToken(String token) {
        return !(token.charAt(0) == ':');
    }

    private void storeToken(String token, Context context) {
        createPreferencesObject(context);

        this.editor.putString("token", token);

        this.editor.commit();
    }

    private void createPreferencesObject(Context context) {
        this.preferences = context.getSharedPreferences(
                "com.chorestory.app",
                Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }
}
