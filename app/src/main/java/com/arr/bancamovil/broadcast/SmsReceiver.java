package com.arr.bancamovil.broadcast;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import com.arr.bancamovil.utils.notification.NotificationUtil;

public class SmsReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "transfer";
    private static final String CHANNEL_NAME = "Transferencia";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Object[] pdus = (Object[]) extras.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String senderPhoneNumber = smsMessage.getDisplayOriginatingAddress();

                        // Verificar si el número de teléfono del remitente corresponde a "pago por
                        // móvil"
                        if (isPagoPorMovilSender(senderPhoneNumber)) {
                            // Mostrar la notificación
                            showNotification(context, smsMessage.getMessageBody());
                        }
                    }
                }
            }
        }
    }

    private boolean isPagoPorMovilSender(String phoneNumber) {
        return phoneNumber.equals("PAGOxMOVIL");
    }

    private void showNotification(Context context, String message) {
        NotificationUtil notif = new NotificationUtil(context);
        notif.createNotificationChannel(CHANNEL_ID, CHANNEL_NAME);
        notif.showNotification(
                CHANNEL_ID, "Transferencia", "Ha recibido una transferencia desde Tramsfermóvil");
    }
}
