package com.simulator.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotTraderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotTraderApplication.class, args);
    }
}