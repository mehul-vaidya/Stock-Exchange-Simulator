package com.simulator.common.model;
//when trade happens. i.e. when stock is sold and bought, we maintain
//buy order id, sell order id , price , timestamp for that
import com.simulator.common.enums.StockSymbol;

import java.time.Instant;

public class Trade {

    private String buyOrderId;
    private String sellOrderId;
    private StockSymbol stock;
    private double price;
    private Instant timestamp;

    public Trade(String buyOrderId, String sellOrderId,
                 StockSymbol stock, double price) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.stock = stock;
        this.price = price;
        this.timestamp = Instant.now();
    }

    // getters

    public StockSymbol getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }
}
