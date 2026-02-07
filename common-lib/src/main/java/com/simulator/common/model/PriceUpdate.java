package com.simulator.common.model;

import com.simulator.common.enums.StockSymbol;

import java.time.Instant;

public class PriceUpdate {

    private StockSymbol stock;
    private double price;
    private Instant timestamp;

    public PriceUpdate(StockSymbol stock, double price) {
        this.stock = stock;
        this.price = price;
        this.timestamp = Instant.now();
    }

    public StockSymbol getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
