package com.da.qlnhahang.model;


import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable {
    private String id;
    private String name;
    private String room;
    private ArrayList<Order> orders;
    private boolean isExpanded;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Order> getOrders() {
        return orders == null ? new ArrayList<>() : orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
