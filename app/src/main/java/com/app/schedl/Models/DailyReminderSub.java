package com.app.schedl.Models;

public class DailyReminderSub {
    private String itemsub_name;
    private Boolean itemsub_checked;

    public DailyReminderSub(String itemsub_name) {
        this.itemsub_name = itemsub_name;
        itemsub_checked = false;
    }

    public Boolean getItemsub_checked() {
        return itemsub_checked;
    }

    public void setItemsub_checked(Boolean itemsub_checked) {
        this.itemsub_checked = itemsub_checked;
    }

    public String getItemsub_name() {
        return itemsub_name;
    }

    public void setItemsub_name(String itemsub_name) {
        this.itemsub_name = itemsub_name;
    }
}
