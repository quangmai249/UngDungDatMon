package com.da.qlnhahang.model;

import java.io.Serializable;

public class Notify implements Serializable {
    private String waiter;
    private String message;
    private String date;

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
