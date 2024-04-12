package com.lucassilvs.kafkaconsumerexemplo.entrypoint.model;


public class OrderServiceModel {

    private long orderTime;

    private int orderId;

    private String itemId;

    public OrderServiceModel(long orderTime, int orderId, String itemId) {
        this.orderTime = orderTime;
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public OrderServiceModel() {
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
