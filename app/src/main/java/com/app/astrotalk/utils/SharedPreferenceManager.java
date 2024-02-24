package com.app.astrotalk.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_USER_LOGGED_IN = "user_logged_in";
    private static final String KEY_USER_NAME = "user_name"; // New key for user name

    private static SharedPreferenceManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    public void clearUserLoggedIn() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_LOGGED_IN);
        editor.remove(KEY_USER_NAME);
        editor.apply();
    }

    // New method to save user name
    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    // New method to get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
}
