package com.arr.bancamovil.utils.exchange.interfaces;

public interface TransferListener {

    void onDataExtracted(String beneficiario, String transaccion, String monto, String tiempo);
}
