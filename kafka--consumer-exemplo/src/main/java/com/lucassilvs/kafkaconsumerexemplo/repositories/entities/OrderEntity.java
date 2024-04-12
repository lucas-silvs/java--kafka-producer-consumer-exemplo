package com.lucassilvs.kafkaconsumerexemplo.repositories.entities;

public class OrderEntity {

    private int orderId;
    private long orderTime;
    private String itemId;


    public OrderEntity(int orderId, long orderTime, String itemId) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
