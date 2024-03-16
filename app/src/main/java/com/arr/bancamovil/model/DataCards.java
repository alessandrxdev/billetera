package com.arr.bancamovil.model;

public class DataCards implements Data {

    private String tarjeta, moneda, usuario;

    public DataCards(String tarjeta, String moneda, String usuario) {
        this.tarjeta = tarjeta;
        this.moneda = moneda;
        this.usuario = usuario;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getUsuario() {
        return usuario;
    }

    @Override
    public int itemViewType() {
        return DataCards.VIEW_CARD;
    }
}
