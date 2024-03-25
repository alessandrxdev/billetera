package com.arr.bancamovil;

import android.app.Application;

import com.arr.bancamovil.utils.theme.ThemeApp;
import com.arr.bugsend.utils.HandlerUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeApp.apply(this);

        Thread.setDefaultUncaughtExceptionHandler(new HandlerUtil(this));
    }
}
