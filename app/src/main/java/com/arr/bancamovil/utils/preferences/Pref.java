package com.arr.bancamovil.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class Pref {

    private Context mContext;

    public Pref(Context context) {
        this.mContext = context;
    }

    public void prefString(String key, String data) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public String loadPrefString(String key, String dataDef) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sp.getString(key, dataDef);
    }

    public void setIntPreference(String key, int data) {
        SharedPreferences.Editor editor = preference().edit();
        editor.putInt(key, data);
        editor.apply();
    }

    public int getIntPreference(String key) {
        return preference().getInt(key, 0);
    }

    private SharedPreferences preference() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
