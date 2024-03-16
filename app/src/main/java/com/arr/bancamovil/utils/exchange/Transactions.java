package com.arr.bancamovil.utils.exchange;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Telephony;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arr.bancamovil.utils.exchange.interfaces.TransferListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transactions {

    private Context mContext;
    private TransferListener mListener;

    public Transactions(Context context, TransferListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    // cargar mensajes de PAGOxMOVIL
    public void processSms(String card) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) mContext, new String[]{Manifest.permission.READ_SMS}, 30);
        } else {
            try {
                ContentResolver cr = mContext.getContentResolver();
                Cursor c =
                        cr.query(
                                Telephony.Sms.CONTENT_URI,
                                null,
                                Telephony.Sms.ADDRESS + " = ?",
                                new String[]{"PAGOxMOVIL"},
                                "DATE DESC");

                if (c != null) {
                    while (c.moveToNext()) {
                        // contenido del sms
                        String smsBody = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));

                        String smsData = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));

                        // extraer datos del sms
                      /*  String smsBeneficiario = extractBeneficiario(smsBody);
                        String smsTransaccion = extractNumeroTransaccion(smsBody);
                        String smsMonto = extractMonto(smsBody);*/
                        String data = getTime(smsData);

                        String beneficiario = extractValue(smsBody, "Beneficiario: (\\d+)");
                        String ordenante = extractValue(smsBody, "Ordenante: (\\d+)");
                        String monto = extractValue(smsBody, "Monto: ([0-9.]+)");
                        String moneda = extractValue(smsBody, "Monto: [0-9.]+ (\\w+)");
                        String numeroTransaccion = extractValue(smsBody, "Nro. Transaccion: (\\w+)");
                        String saldoRestante = extractValue(smsBody, "Saldo restante: ([A-Z]{2} [0-9.]+)");

                        System.out.println("Beneficiario: " + beneficiario);
                        System.out.println("Ordenante: " + ordenante);
                        System.out.println("Monto: " + monto);
                        System.out.println("Moneda: " + moneda);
                        System.out.println("T" + numeroTransaccion);
                        System.out.println("Saldo Restante: " + saldoRestante);
                        if (beneficiario != null && numeroTransaccion != null
                                && monto != null && moneda != null) {
                            mListener.onDataExtracted(
                                    beneficiario, numeroTransaccion, monto + " " + moneda, data);
                        }
                    }
                    c.close();
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    // comprobar si el beneficiario se encuentra entre los sms
    public boolean hasCreditCardNumber(String lastFourDigits) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) mContext, new String[]{Manifest.permission.READ_SMS}, 30);
        } else {
            try {
                ContentResolver cr = mContext.getContentResolver();
                Cursor cursor =
                        cr.query(
                                Telephony.Sms.CONTENT_URI,
                                null,
                                Telephony.Sms.ADDRESS + " = ?",
                                new String[]{"PAGOxMOVIL"},
                                "DATE DESC");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String smsBody =
                                cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));

                        // Extraer el beneficiario
                        String beneficiario = extractBeneficiario(smsBody);
                        if (beneficiario != null && beneficiario.endsWith(lastFourDigits)) {
                            cursor.close();
                            return true;
                        }
                    }
                    cursor.close();
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return false;
    }

    private String extractBeneficiario(String smsBody) {
        Pattern pattern = Pattern.compile("Beneficiario: ([\\dX]+)");
        Matcher matcher = pattern.matcher(smsBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractValue(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getTime(String time) {
        long timestamp = Long.parseLong(time);

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;
        long minutes = timeDifference / (60 * 1000);
        long hours = timeDifference / (60 * 60 * 1000);
        long days = timeDifference / (24 * 60 * 60 * 1000);
        long month = days / 30;

        String timeAgo;
        if (month > 0) {
            timeAgo = "hace " + month + (month == 1 ? " mes" : " meses");
        } else if (days > 0) {
            timeAgo = "hace " + days + (days == 1 ? " día" : " días");
        } else if (hours > 0) {
            timeAgo = "hace " + hours + (hours == 1 ? " hora" : " horas");
        } else if (minutes > 0) {
            timeAgo = "hace " + minutes + (minutes == 1 ? " minuto" : " minutos");
        } else {
            timeAgo = "ahora";
        }
        return timeAgo;
    }
}
