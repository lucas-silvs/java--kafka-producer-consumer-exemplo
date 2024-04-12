package com.lucassilvs.kafkaconsumerexemplo.transportlayer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderModel {

    @JsonProperty("ordertime")
    private long orderTime;

    @JsonProperty("orderid")
    private int orderId;

    @JsonProperty("itemid")
    private String itemId;

    @JsonProperty("address")
    private AddressModel address;

    public OrderModel(long orderTime, int orderId, String itemId, AddressModel address) {
        this.orderTime = orderTime;
        this.orderId = orderId;
        this.itemId = itemId;
        this.address = address;
    }

    public OrderModel() {

    }


    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public AddressModel getAddress() {
        return address;
    }

    public String getItemId() {
        return itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }
}
