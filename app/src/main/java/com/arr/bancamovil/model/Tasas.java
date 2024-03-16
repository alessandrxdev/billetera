package com.arr.bancamovil.model;

public class Tasas implements Items {

    private String moneda, compra, venta;

    public Tasas(String moneda, String compra, String venta) {
        this.moneda = moneda;
        this.compra = compra;
        this.venta = venta;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getCompra() {
        return compra;
    }

    public String getVenta() {
        return venta;
    }

    @Override
    public int itemViewType() {
        return Tasas.VIEW_TASAS;
    }
}
