package com.arr.bancamovil.model.convert;

public class Moneda {

    private int mIcon;
    private String mMoneda;

    public Moneda(int icon, String moneda) {
        this.mIcon = icon;
        this.mMoneda = moneda;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getMoneda() {
        return mMoneda;
    }
}
