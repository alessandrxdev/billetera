package com.arr.bancamovil.model;

public class TicketModel implements Data {

    private String salida, horaSalida, llegada, horaLlegada, asiento, ticket;

    public TicketModel(
            String salida,
            String hFrom,
            String llegada,
            String hTo,
            String asiento,
            String ticket) {
        this.salida = salida;
        this.horaSalida = hFrom;
        this.llegada = llegada;
        this.horaLlegada = hTo;
        this.asiento = asiento;
        this.ticket = ticket;
    }

    public String getSalida() {
        return salida;
    }

    public String getHSalida() {
        return horaSalida;
    }

    public String getLlegada() {
        return llegada;
    }

    public String getHLlegada() {
        return horaLlegada;
    }

    public String getAsiento() {
        return asiento;
    }

    public String getTicket() {
        return ticket;
    }

    @Override
    public int itemViewType() {
        return TicketModel.VIEW_TICKET;
    }
}
