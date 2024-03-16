package com.arr.bancamovil.utils.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.arr.bancamovil.R;

public class NotificationUtil {

    private final Context mContext;
    
    public NotificationUtil(Context context) {
        this.mContext = context;
    }

    public void createNotificationChannel(String id, String name) {
        // Verificar si se está ejecutando en Android Oreo (API 26) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Crear el canal de notificación
            NotificationChannel channel =
                    new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(mContext.getString(R.string.message_notification));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.setAllowBubbles(true);
            }
            // Obtener el administrador de notificaciones
            NotificationManager notificationManager =
                    mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String id, String title, String message) {
        // Crear la notificación
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext, id)
                        .setSmallIcon(R.drawable.ic_stat_billetera)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Obtener el administrador de notificaciones
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}
