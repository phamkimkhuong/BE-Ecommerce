package com.backend.commonservice.service;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/8/2025 9:12 PM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        logger.info("Message sent to topic {}", topic);
    }
}
