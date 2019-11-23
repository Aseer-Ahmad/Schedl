package com.app.schedl;

import java.util.Date;

public class DataModel {
    private String itemname;
    private Date itemdate;

    public DataModel(String itemname, Date itemdate) {
        this.itemname = itemname;
        this.itemdate = itemdate;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Date getItemdate() {
        return itemdate;
    }

    public void setItemdate(Date itemdate) {
        this.itemdate = itemdate;
    }
}
