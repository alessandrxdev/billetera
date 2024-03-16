package com.arr.bancamovil;

import android.app.Application;

import com.arr.bancamovil.utils.theme.ThemeApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeApp.apply(this);
    }
}
