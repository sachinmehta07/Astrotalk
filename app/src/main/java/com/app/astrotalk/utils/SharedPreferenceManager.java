package com.app.astrotalk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.astrotalk.model.PoojaBookModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedPreferenceManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_USER_LOGGED_IN = "user_logged_in";
    private static final String KEY_USER_NAME = "user_name"; // New key for user name
    private static SharedPreferenceManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setChatMessages(String userId, List<String> chatMessages) {

        Gson gson = new Gson();

        String messagesJson = gson.toJson(chatMessages);

        String key = "chat_" + userId;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, messagesJson);
        editor.apply();
    }

    // Updated method to get chat messages as a list
    public List<String> getChatMessages(String key) {

        String messagesJson = sharedPreferences.getString(key, "");

        if (!messagesJson.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            return gson.fromJson(messagesJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    public void setPoojaBooked(int poojaId, PoojaBookModel poojaBookModel) {

        String key = "pooja_" + poojaId;

        Gson gson = new Gson();

        String poojaJson = gson.toJson(poojaBookModel);

        // Check if the Pooja with the same ID already exists in SharedPreferences
        if (!sharedPreferences.contains(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, poojaJson);
            editor.apply();
        }
    }

    public PoojaBookModel getPoojaBooked(int poojaId) {
        String key = "pooja_" + poojaId;
        String poojaJson = sharedPreferences.getString(key, "");

        if (!poojaJson.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(poojaJson, PoojaBookModel.class);
        } else {
            return null;
        }
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
        editor.clear();
        editor.apply();
    }

    // New method to save user name
    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public void removePoojaBooked(int poojaId) {
        String key = "pooja_" + poojaId;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // New method to get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
}
