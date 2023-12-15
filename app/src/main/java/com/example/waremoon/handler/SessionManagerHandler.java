package com.example.waremoon.handler;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    private SharedPreferences sharedPreferences;

    public SessionManagerHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setUserData(int userId, String userName) {
        sharedPreferences.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, userName)
                .apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID);
    }

    public void logoutUser() {
        sharedPreferences.edit().clear().apply();
    }
}
