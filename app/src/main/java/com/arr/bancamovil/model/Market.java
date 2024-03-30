package com.arr.bancamovil.model;

public class Market {

    private String tipo, saldo, date;

    public Market(String tipo, String saldo, String date) {
        this.tipo = tipo;
        this.saldo = saldo;
        this.date = date;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getDate() {
        return date;
    }
}
