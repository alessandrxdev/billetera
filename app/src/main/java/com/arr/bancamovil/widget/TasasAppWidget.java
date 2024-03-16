package com.arr.bancamovil.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.arr.bancamovil.R;
import com.arr.bancamovil.broadcast.UpdateWidget;

/** Implementation of App Widget functionality. */
public class TasasAppWidget extends AppWidgetProvider {

    static void updateAppWidget(
            Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tasas_app_widget);
        setRemoteAdapter(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        IntentFilter filter = new IntentFilter("com.arr.bancamovil.ACTION_UPDATE_WIDGET");
        UpdateWidget update = new UpdateWidget();
        LocalBroadcastManager.getInstance(context).registerReceiver(update, filter);

        Intent intent = new Intent("com.arr.bancamovil.ACTION_UPDATE_WIDGET");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetId = intent.getIntArrayExtra("com.arr.bancamovil.ACTION_UPDATE_WIDGET");
        if (widgetId != null) {
            for (int appWidgetId : widgetId) {
                RemoteViews views =
                        new RemoteViews(context.getPackageName(), R.layout.tasas_app_widget);
                setRemoteAdapter(context, views);
                widgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.list_view, new Intent(context, WidgetService.class));
    }
}
