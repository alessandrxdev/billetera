package com.arr.bancamovil.adapter.convert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arr.bancamovil.R;
import com.arr.bancamovil.model.convert.Moneda;

import java.util.ArrayList;

public class ConvertAdapter extends BaseAdapter {

    private int flags[];
    private String[] monedas;

    private Context mContext;
    private LayoutInflater inflater;

    public ConvertAdapter(Context context, int[] flag, String[] moneda) {
        this.mContext = context;
        this.flags = flag;
        this.monedas = moneda;
        inflater = (LayoutInflater.from(mContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.convert_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView names = (TextView) view.findViewById(R.id.moneda);
        icon.setImageResource(flags[i]);
        names.setText(monedas[i]);
        return view;
    }
}
