package com.simulator.price.websocket;
import com.simulator.common.model.PriceUpdate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
//it listen to current price and publishes it on web socket
@Service
public class PriceWebSocketPublisher {

    private final SimpMessagingTemplate template;

    public PriceWebSocketPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(topics = "price-topic")
    public void publish(PriceUpdate update) {
        template.convertAndSend("/topic/prices", update);
    }
}