package com.simulator.exchange.engine;
//we maintain buy order queue and sell order queue for each stock
//if buyer is willing to pay more than what seller is asking then trade happens
//lets say seller is willing to sale at 10. buyer is ready to buy at 12 then trade happen
//new stock price will be 10
import com.simulator.common.model.Order;
import com.simulator.common.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;


public class MatchingEngine {


    public Trade match(OrderBook book) {

        if (book.buyOrders.isEmpty() || book.sellOrders.isEmpty()) {
            return null;
        }

        Order bestBuy = book.buyOrders.peek();
        Order bestSell = book.sellOrders.peek();

        //get buy and sell order and check buyer price is greater than seller price.
        //seller price will be new stock price
        if (bestBuy.getPrice() >= bestSell.getPrice()) {

            book.buyOrders.poll();
            book.sellOrders.poll();

            double tradePrice = bestSell.getPrice();

            return new Trade(
                    bestBuy.getOrderId(),
                    bestSell.getOrderId(),
                    bestBuy.getStock(),
                    tradePrice
            );
        }

        return null;
    }
}
