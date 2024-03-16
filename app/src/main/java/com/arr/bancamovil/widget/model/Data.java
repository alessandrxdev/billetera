package com.arr.bancamovil.widget.model;

public class Data {

    private int icon;
    private String moneda, venta;

    public Data(int icon, String moneda, String venta) {
        this.icon = icon;
        this.moneda = moneda;
        this.venta = venta;
    }

    public int getIcon() {
        return icon;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getVenta() {
        return venta;
    }
}
