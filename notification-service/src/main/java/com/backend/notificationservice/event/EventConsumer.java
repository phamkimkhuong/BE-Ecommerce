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
    /*
     * @description: This method consumes messages from the Kafka topic "test" and processes them.
     * If an exception occurs during processing, it will retry up to 3 times with exponential backoff.
     * If all retries fail, the message will be sent to the Dead Letter Topic (DLT).
     * @param message: The message consumed from the Kafka topic.
     */
    @RetryableTopic(
            // 2 retry attempts + 1 DLQ attempt
            // Default is 3 attempts retries
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR, // Send to DLT on failure and if DLT error will fail
            // Retry when the exception is a RuntimeException or RetriableException
            include = {RuntimeException.class, RetriableException.class}
    )
    @KafkaListener(topics = "test",groupId = "default-group",containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        log.info("Consumed message -> {}", message);
        // Processing logic
        throw new RuntimeException("Simulated error");
    }
    // This method handles messages that are sent to the Dead Letter Topic (DLT).
    // It will be called when the message cannot be processed after all retry attempts.
    // The message will be sent to the DLT with the same topic name as the original topic.
    @DltHandler
    public void handle(@Payload String message) {
        log.info("Handling message from DLT -> {}", message);
    }
}
