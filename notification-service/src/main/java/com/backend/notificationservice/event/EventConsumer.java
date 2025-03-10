package com.backend.notificationservice.event;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/8/2025 9:17 PM
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventConsumer {
    @KafkaListener(topics = "test",containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message){
        log.info("Consumed message -> {}", message);
    }
}
