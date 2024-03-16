package com.arr.bancamovil.model;

public class Market {

    private String tipo, saldo;

    public Market(String tipo, String saldo) {
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSaldo() {
        return saldo;
    }
}
