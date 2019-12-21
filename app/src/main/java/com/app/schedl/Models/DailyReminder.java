package com.app.schedl.Models;

public class DailyReminder {
    private String item_name;
    private Boolean expanded;

    public DailyReminder(String item_name) {
        this.item_name = item_name;
        this.expanded = false;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
