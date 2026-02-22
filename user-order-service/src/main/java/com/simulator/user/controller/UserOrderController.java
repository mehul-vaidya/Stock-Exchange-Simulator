package com.simulator.user.controller;
import com.simulator.common.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Takes order sent via Post Call and  send it on order-topic
@RestController
@RequestMapping("/api/orders")
public class UserOrderController {

    private static final Logger log = LoggerFactory.getLogger(UserOrderController.class);

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public UserOrderController(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String placeOrder(@RequestBody Order order) {
        log.info("User Placed Order →{} {} @ {}", order.getType() , order.getStock(), order.getPrice());
        kafkaTemplate.send("orders-topic", order);
        return "Order accepted";
    }

}
