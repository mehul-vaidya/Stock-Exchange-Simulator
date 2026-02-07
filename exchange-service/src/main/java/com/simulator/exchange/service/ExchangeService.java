package com.simulator.exchange.service;

import com.simulator.common.enums.StockSymbol;
import com.simulator.common.model.Order;
import com.simulator.common.model.PriceUpdate;
import com.simulator.common.model.Trade;
import com.simulator.exchange.engine.MatchingEngine;
import com.simulator.exchange.engine.OrderBook;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class ExchangeService {
    private final Map<StockSymbol, OrderBook> books = new EnumMap<>(StockSymbol.class);
    private final Map<StockSymbol, Double> lastPrice = new EnumMap<>(StockSymbol.class);

    private final MatchingEngine engine = new MatchingEngine();
    private final KafkaTemplate<String, PriceUpdate> kafkaTemplate;

    public ExchangeService(KafkaTemplate<String, PriceUpdate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

        //where stocks start trading for first time. set their initial price
        lastPrice.put(StockSymbol.AA, 100.0);
        lastPrice.put(StockSymbol.BB, 150.0);
        lastPrice.put(StockSymbol.CC, 200.0);
        lastPrice.put(StockSymbol.DD, 25.0);

        //for each stock symbol(AA, BB,CC , DD) we will maintain seperate order book
        //order book contains buy and sell PQ for
        //matching engine does matching work
        for (StockSymbol s : StockSymbol.values()) {
            books.put(s, new OrderBook());
        }
    }

    @KafkaListener(topics = "orders-topic")
    public void receiveOrder(Order order) {
        //get book for this stock and add this order in that stock
        OrderBook book = books.get(order.getStock());
        book.add(order);

        //match engine will perform match
        Trade trade = engine.match(book);

        //if exchange happen (stock sold or bought)
        if (trade != null) {
            //store new stock price in
            lastPrice.put(trade.getStock(), trade.getPrice());
            //publish new price
            kafkaTemplate.send("price-topic",new PriceUpdate(trade.getStock(), trade.getPrice()));
        }
    }
}
