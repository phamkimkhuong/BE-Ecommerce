package com.backend.notificationservice.event;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/8/2025 9:17 PM
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventConsumer {
    @RetryableTopic(
            attempts = "4", // 3 retry attempts + 1 DLQ attempt
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            include = {RuntimeException.class, RetriableException.class}
    )
    @KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        log.info("Consumed message -> {}", message);
        // Processing logic
        throw new RuntimeException("Simulated error");
    }

    @DltHandler
    public void handle(@Payload String message) {
        log.info("Handling message from DLT -> {}", message);
    }
}
