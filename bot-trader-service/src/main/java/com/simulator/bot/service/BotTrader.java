package com.simulator.bot.service;

// This class creates orders by choosing parameters randomly and
// places orders at fixed intervals on Kafka Topic

import com.simulator.common.enums.OrderType;
import com.simulator.common.enums.StockSymbol;
import com.simulator.common.model.Order;
import com.simulator.common.model.PriceUpdate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BotTrader {

    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final Random random = new Random();
    private static final Logger log = LoggerFactory.getLogger(BotTrader.class);

    private final StockSymbol[] stocks = StockSymbol.values();

    // Live prices updated from exchange via Kafka
    private final Map<StockSymbol, Double> livePrices =
            new EnumMap<>(StockSymbol.class);

    private long startTime;

    public BotTrader(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        startTime = System.currentTimeMillis();

        // Initial prices (used until first trade happens)
        livePrices.put(StockSymbol.AA, 100.0);
        livePrices.put(StockSymbol.BB, 150.0);
        livePrices.put(StockSymbol.CC, 200.0);
        livePrices.put(StockSymbol.DD, 25.0);

        log.info("Bot started at {} with initial prices {}", startTime, livePrices);
    }

    // Listen to live price updates from exchange
    @KafkaListener(topics = "price-topic")
    public void onPriceUpdate(PriceUpdate update) {
        livePrices.put(update.getStock(), update.getPrice());
        log.info("Price update Received → {} @ {}", update.getStock(), update.getPrice());
    }

    @Scheduled(fixedRateString = "${bot.interval-ms}")
    public void placeOrder() {

        // Stop after 10 minutes
        if (System.currentTimeMillis() - startTime > 30 * 60 * 1000) {
            log.info("Bot runtime finished. Stopping order generation.");
            return;
        }

        // Select stock, buy/sell randomly
        StockSymbol stock = stocks[random.nextInt(stocks.length)];
        OrderType type = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;

        Double current = livePrices.get(stock);
        if (current == null) {
            // Safety check (should not normally happen)
            return;
        }

        double price;
        if (type == OrderType.BUY) {
            price = randomBetween(0.9 * current, current*1.05);
        } else {
            price = randomBetween(0.95* current, 1.1 * current);
        }

        // Create order and place it
        Order order = new Order();
        order.setStock(stock);
        order.setType(type);
        order.setPrice(round(price));

        // Send order to exchange
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
