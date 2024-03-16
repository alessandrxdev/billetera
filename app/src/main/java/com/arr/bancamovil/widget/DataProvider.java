package com.arr.bancamovil.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.arr.bancamovil.model.Tasas;
import com.arr.bancamovil.utils.tasas.TasasUtils;
import com.arr.bancamovil.widget.model.Data;
import java.util.ArrayList;
import com.arr.bancamovil.R;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private List<Data> list = new ArrayList<>();

    public DataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        updateData();
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view =
                new RemoteViews(mContext.getPackageName(), R.layout.layout_list_item_widget);
        Data data = list.get(position);
        view.setTextViewText(R.id.moneda, data.getMoneda());
        view.setTextViewText(R.id.venta, data.getVenta());
        view.setImageViewResource(R.id.icon, data.getIcon());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void updateData() {
        initData();
    }

    private void initData() {
        list.clear();
        try {
            JSONObject informal =
                    new JSONObject(new TasasUtils(mContext).getFileJson().getString("informal"));
            if (informal.length() > 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("USD", "USD");
                map.put("EUR", "EUR");
                map.put("MLC", "MLC");

                HashMap<String, Integer> icon = new HashMap<>();
                icon.put("USD", R.drawable.ic_flag_usa_56dp);
                icon.put("EUR", R.drawable.ic_flag_europe_56dp);
                icon.put("MLC", R.drawable.ic_credit_card_mlc_56dp);
                for (String currency : map.keySet()) {
                    if (informal.has(currency)) {
                        JSONObject object = informal.getJSONObject(currency);
                        double sell = object.getDouble("sell");
                        int iconResource =
                                icon.containsKey(currency)
                                        ? icon.get(currency)
                                        : R.drawable.ic_moneda_24px;
                        list.add(new Data(iconResource, map.get(currency), Double.toString(sell)));
                    }
                }
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
