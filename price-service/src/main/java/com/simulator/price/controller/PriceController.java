package com.simulator.price.controller;

import com.simulator.price.service.PriceService;
import com.simulator.common.enums.StockSymbol;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public Map<StockSymbol, Double> getPrices() {
        return priceService.getPrices();
    }
}