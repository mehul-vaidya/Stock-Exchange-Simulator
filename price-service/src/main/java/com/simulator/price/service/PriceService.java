package com.simulator.price.service;
import com.simulator.common.model.PriceUpdate;
import com.simulator.common.enums.StockSymbol;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

//Listens to Kafka price-topic
//Updates in-memory map
//so that when Price controller explicitely ask for it we can provide

@Service
public class PriceService {

    private final Map<StockSymbol, Double> prices = new EnumMap<>(StockSymbol.class);

    @KafkaListener(topics = "price-topic")
    public void consume(PriceUpdate update) {
        prices.put(update.getStock(), update.getPrice());
    }

    public Map<StockSymbol, Double> getPrices() {
        return prices;
    }
}
