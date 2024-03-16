package com.arr.bancamovil.model;

public class Tips {

    private String title, message;
    private boolean expanded;

    public Tips(String title, String message, boolean expanded) {
        this.title = title;
        this.message = message;
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
