package com.arr.bancamovil.model;

public class Transfer implements Items {

    String tarjeta, hora, monto;

    public Transfer(String tarjeta, String hora, String monto) {
        this.tarjeta = tarjeta;
        this.hora = hora;
        this.monto = monto;
    }

    public String getCard() {
        return tarjeta;
    }

    public String getHora() {
        return hora;
    }

    public String getMonto() {
        return monto;
    }

    @Override
    public int itemViewType() {
        return Transfer.VIEW_TASAS;
    }
}
