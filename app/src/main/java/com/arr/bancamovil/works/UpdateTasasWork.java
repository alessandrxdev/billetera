package com.arr.bancamovil.works;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.arr.bancamovil.utils.notification.NotificationUtil;
import com.arr.bancamovil.utils.tasas.TasasUtils;

import com.arr.bancamovil.widget.DataProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class UpdateTasasWork extends Worker {

    private final Context mContext;
    private static final String CHANNEL_ID = "tasas_channel";
    private static final String CHANNEL_NAME = "Tasas de cambio";

    public UpdateTasasWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(
                    () -> {
                        TasasUtils tasas = new TasasUtils(mContext);
                        JSONObject result = tasas.obtainsTasas();
                        handler.post(
                                () -> {
                                    // UI Thread work here
                                    if (result != null) {
                                        Log.e("ExecuteTask ", String.valueOf(result));
                                        tasas.saveTasas(result);

                                        // mostrar notificacion de tasas actualizadas solo se el
                                        // usuario lo desea
                                        SharedPreferences spNot =
                                                PreferenceManager.getDefaultSharedPreferences(
                                                        mContext);
                                        boolean isShowNotifi = spNot.getBoolean("update", false);
                                        if (!isShowNotifi) {
                                            NotificationUtil notif = new NotificationUtil(mContext);
                                            notif.createNotificationChannel(
                                                    CHANNEL_ID, CHANNEL_NAME);
                                            notif.showNotification(
                                                    CHANNEL_ID,
                                                    "Tasas",
                                                    "Las tasas de cambio se han actualizado");
                                        }
                                    }
                                });
                    });
            Log.e("Work. ", "Work success");

            // update widget
            Intent intent = new Intent("com.arr.bancamovil.ACTION_UPDATE_WIDGET");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            return Result.success();
        } catch (Exception err) {
            err.printStackTrace();
            return Result.failure();
        }
    }
}
