package com.simulator.exchange.service;

// This class first initializes Exchange service by putting stocks and their initial price in last price Map
// second it has receiveOrder methods which listen to kafka order topic "orders-topic"
// it note down that order in Book
// it then pass book to Matching Engine. which then tries to perform trade.
// for trade to happen there should be atleast one buyer which is willing to buy stock who is ready sell at lowest price among all seller
// if trade actually happened then update lastprice and publish new price on price-topic

import com.simulator.common.enums.StockSymbol;
import com.simulator.common.model.Order;
import com.simulator.common.model.PriceUpdate;
import com.simulator.common.model.Trade;
import com.simulator.exchange.engine.MatchingEngine;
import com.simulator.exchange.engine.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class ExchangeService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);

    private final Map<StockSymbol, OrderBook> books = new EnumMap<>(StockSymbol.class);
    private final Map<StockSymbol, Double> lastPrice = new EnumMap<>(StockSymbol.class);

    private final MatchingEngine engine = new MatchingEngine();
    private final KafkaTemplate<String, PriceUpdate> kafkaTemplate;

    public ExchangeService(KafkaTemplate<String, PriceUpdate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

        // where stocks start trading for first time. set their initial price
        lastPrice.put(StockSymbol.AA, 100.0);
        lastPrice.put(StockSymbol.BB, 150.0);
        lastPrice.put(StockSymbol.CC, 200.0);
        lastPrice.put(StockSymbol.DD, 25.0);

        log.info("Exchange initialized with starting prices: {}", lastPrice);

        // initialize order books for each stock
        for (StockSymbol s : StockSymbol.values()) {
            books.put(s, new OrderBook());
            log.info("Order book created for stock {}", s);
        }
    }

    @KafkaListener(topics = "orders-topic")
    public void receiveOrder(Order order) {

        log.info("ORDER RECEIVED → {} {} @ {}",
                order.getType(),
                order.getStock(),
                order.getPrice());

        // get book for this stock and add this order
        OrderBook book = books.get(order.getStock());
        book.add(order);

        log.debug("Order added to book for stock {}", order.getStock());

        // attempt to match orders
        Trade trade = engine.match(book);

        if (trade != null) {
            log.info("TRADE EXECUTED → {} bought/sold at price {}",
                    trade.getStock(),
                    trade.getPrice());

            // update last traded price
            lastPrice.put(trade.getStock(), trade.getPrice());

            log.info("UPDATED LAST PRICE → {} = {}",
                    trade.getStock(),
                    trade.getPrice());

            // publish new price
            kafkaTemplate.send(
                    "price-topic",
                    new PriceUpdate(trade.getStock(), trade.getPrice())
            );

            log.info("PRICE PUBLISHED TO KAFKA → {} @ {}",
                    trade.getStock(),
                    trade.getPrice());

        } else {
            log.debug("NO MATCH FOUND for stock {}", order.getStock());
        }
    }
}
