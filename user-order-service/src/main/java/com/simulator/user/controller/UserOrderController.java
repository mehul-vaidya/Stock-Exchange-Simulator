package com.simulator.user.controller;
import com.simulator.common.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
//Takes order sent via Post Call and  send it on order-topic
@RestController
@RequestMapping("/api/orders")
public class UserOrderController {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public UserOrderController(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String placeOrder(@RequestBody Order order) {
        kafkaTemplate.send("orders-topic", order);
        return "Order accepted";
    }

}
