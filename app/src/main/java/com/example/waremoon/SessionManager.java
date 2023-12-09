package com.example.waremoon;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    // Dodaj dodatkowe klucze dla innych informacji o użytkowniku

    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setUserData(int userId, String userName) {
        sharedPreferences.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, userName)
                // Dodaj pozostałe dane użytkownika tutaj
                .apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
    // Dodaj metody do pobierania innych danych użytkownika
}
