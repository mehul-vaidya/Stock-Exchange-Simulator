package com.simulator.bot.service;

import com.simulator.common.enums.OrderType;
import com.simulator.common.enums.StockSymbol;
import com.simulator.common.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BotTrader {

    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final Random random = new Random();

    private final StockSymbol[] stocks = StockSymbol.values();

    // Local rough prices (good enough for bots)
    private final double[] basePrices = {100, 150, 200, 25};

    private long startTime;

    public BotTrader(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    private static final Logger log = LoggerFactory.getLogger(BotTrader.class);

    @PostConstruct
    public void init() {

        startTime = System.currentTimeMillis();
        log.info("Bot started at {}", startTime);
    }

    @Scheduled(fixedRateString = "${bot.interval-ms}")
    public void placeOrder() {

        // Stop after 10 minutes
        if (System.currentTimeMillis() - startTime > 10 * 60 * 1000) {
            log.info("Bot runtime finished. Stopping order generation.");
            return;
        }

        int i = random.nextInt(stocks.length);
        StockSymbol stock = stocks[i];
        OrderType type = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;

        double current = basePrices[i];
        double price;

        if (type == OrderType.BUY) {
            price = randomBetween(0.9 * current, current);
        } else {
            price = randomBetween(current, 1.1 * current);
        }

        Order order = new Order();
        order.setStock(stock);
        order.setType(type);
        order.setPrice(round(price));

        kafkaTemplate.send("orders-topic", order);

        log.info("BOT ORDER SENT → {} {} @ {}", type, stock, order.getPrice());

    }

    private double randomBetween(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
