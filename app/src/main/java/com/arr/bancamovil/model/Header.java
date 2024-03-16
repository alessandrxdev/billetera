package com.arr.bancamovil.model;

public class Header implements Items {

    private String header;

    public Header(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public int itemViewType() {
        return Header.VIEW_HEADER;
    }
}
