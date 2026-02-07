package com.simulator.exchange.engine;

import com.simulator.common.enums.OrderType;
import com.simulator.common.model.Order;

import java.util.PriorityQueue;

public class OrderBook {

    // Highest BUY first
    public PriorityQueue<Order> buyOrders =
            new PriorityQueue<>((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

    // Lowest SELL first
    public PriorityQueue<Order> sellOrders =
            new PriorityQueue<>((a, b) -> Double.compare(a.getPrice(), b.getPrice()));

    //add order in correct queue
    public void add(Order order) {
        if (order.getType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public Order bestBuy() {
        return buyOrders.peek();
    }

    public Order bestSell() {
        return sellOrders.peek();
    }

    public Order popBuy() {
        return buyOrders.poll();
    }

    public Order popSell() {
        return sellOrders.poll();
    }

}
