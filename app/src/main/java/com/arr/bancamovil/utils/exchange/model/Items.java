package com.arr.bancamovil.utils.exchange.model;

public class Items {

    private int icon;
    private String beneficiario, cantidad, id;

    public Items(int icon, String beneficiario, String id, String cantidad) {
        this.icon = icon;
        this.beneficiario = beneficiario;
        this.id = id;
        this.cantidad = cantidad;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public String getId() {
        return id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public int getIcon() {
        return icon;
    }
}
