package com.simulator.price.service;
import com.simulator.common.model.PriceUpdate;
import com.simulator.common.enums.StockSymbol;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Listens to Kafka price-topic
//Updates in-memory map
//so that when Price controller explicitely ask for it we can provide

@Service
public class PriceService {

    private final Map<StockSymbol, Double> prices = new EnumMap<>(StockSymbol.class);
    private static final Logger log = LoggerFactory.getLogger(PriceService.class);

    // Initialize starting prices
    public PriceService() {
        prices.put(StockSymbol.AA, 100.0);
        prices.put(StockSymbol.BB, 150.0);
        prices.put(StockSymbol.CC, 200.0);
        prices.put(StockSymbol.DD, 25.0);
    }

    @KafkaListener(topics = "price-topic")
    public void consume(PriceUpdate update) {

        prices.put(update.getStock(), update.getPrice());
        log.info("Price update Received → {} @ {}", update.getStock(), update.getPrice());
    }

    public Map<StockSymbol, Double> getPrices() {
        return prices;
    }
}
