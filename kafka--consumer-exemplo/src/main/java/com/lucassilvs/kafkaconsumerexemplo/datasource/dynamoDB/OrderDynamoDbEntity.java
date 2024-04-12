package com.lucassilvs.kafkaconsumerexemplo.datasource.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "OrderInfo")
public class OrderDynamoDbEntity {

    @DynamoDBHashKey
    private int orderId;
    private long orderTime;
    private String itemId;

    public OrderDynamoDbEntity(int orderId, long orderTime, String itemId) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.itemId = itemId;
    }

    public OrderDynamoDbEntity() {
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
