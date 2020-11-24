package com.example.admin.r_mart.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 2/4/2019.
 */

public class AppPreference {

    private SharedPreferences preferences;


    public AppPreference(Context context) {
        preferences = context.getSharedPreferences("app_shopping_name", Context.MODE_PRIVATE);
    }

    public void SetInteger(String key, int val) {
        preferences.edit().putInt(key, val).apply();
    }
    public int GetInteger(String key) {
        return preferences.getInt(key, 0);
    }
}
