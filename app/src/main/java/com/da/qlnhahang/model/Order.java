package com.da.qlnhahang.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private long id = System.currentTimeMillis();
    private int status = 0;
    private String waiter;
    private ArrayList<Item> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        if (this.items != null) {
            this.items.addAll(items);
            return;
        }
        this.items = items;
    }
}
