package com.arr.bancamovil.broadcast;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import com.arr.bancamovil.widget.DataProvider;

public class UpdateWidget extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // update widget
        DataProvider data = new DataProvider(context, intent);
        data.updateData();
        
        Log.e("Widget Tasas ", " Se ha llamado al broadcast update widget");
    }
}
