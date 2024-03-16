package com.arr.bancamovil.utils.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class ThemeApp {

    public ThemeApp() {

    }

    public enum Mode {
        system, light, dark
    }

    public static void apply(Mode mode) {
        switch (mode) {
            case dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            case light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            default -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
            }
        }
    }

    public static void apply(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString("tema", Mode.system.name());
        apply(Mode.valueOf(value));
    }
}
