package com.simulator.common.model;
//below class defines properties of order object and getter/setters
import com.simulator.common.enums.OrderType;
import com.simulator.common.enums.StockSymbol;

import java.time.Instant;
import java.util.UUID;

public class Order {

    private String orderId;
    private StockSymbol stock;
    private OrderType type;
    private double price;
    private int quantity;
    private Instant timestamp;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.quantity = 1; // keep simple
    }

    // getters & setters

    public String getOrderId() {
        return orderId;
    }

    public StockSymbol getStock() {
        return stock;
    }

    public void setStock(StockSymbol stock) {
        this.stock = stock;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
