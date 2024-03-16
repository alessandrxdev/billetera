package com.arr.bancamovil.model;

public class Premium {

    private int icon;
    private String title, subtitle;

    public Premium(int icon, String title, String subtitle) {
        this.icon = icon;
        this.subtitle = subtitle;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
