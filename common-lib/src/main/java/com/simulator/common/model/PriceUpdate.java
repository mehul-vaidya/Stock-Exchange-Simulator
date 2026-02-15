package com.simulator.common.model;
//this class is used to maintain current price of stock along with timestamp
import com.simulator.common.enums.StockSymbol;

import java.time.Instant;

public class PriceUpdate {

    private StockSymbol stock;
    private double price;
    private Instant timestamp;

    public PriceUpdate() {
    }

    public PriceUpdate(StockSymbol stock, double price) {
        this.stock = stock;
        this.price = price;
        this.timestamp = Instant.now();
    }


    public StockSymbol getStock() {
        return stock;
    }

    public void setStock(StockSymbol stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
