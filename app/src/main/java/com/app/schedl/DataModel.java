package com.app.schedl;

import java.util.Date;

public class DataModel {
    private String itemname;
    private Date itemtime_begin;
    private int itemtime_tocomplete;

    public DataModel(String itemname, Date itemtime_begin, int itemtime_tocomplete) {
        this.itemname = itemname;
        this.itemtime_begin = itemtime_begin;
        this.itemtime_tocomplete = itemtime_tocomplete;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Date getItemtime_begin() {
        return itemtime_begin;
    }

    public void setItemtime_begin(Date itemtime_begin) {
        this.itemtime_begin = itemtime_begin;
    }

    public int getItemtime_tocomplete() {
        return itemtime_tocomplete;
    }

    public void setItemtime_tocomplete(int itemtime_tocomplete) {
        this.itemtime_tocomplete = itemtime_tocomplete;
    }
}
