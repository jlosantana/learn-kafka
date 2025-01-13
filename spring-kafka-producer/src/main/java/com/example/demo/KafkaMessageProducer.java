package com.example.demo;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result,ex) -> {
            if (ex != null) {
                System.out.println("Unable to send message=[" + message + "] to topic=[" + topic + "] with erro=[" + ex.getMessage()+"]");
            } else {
                System.out.println("Sent message=[" + message + "] to topic=[" + topic + "] with offset=[" + result.getRecordMetadata().offset()+"]");
            }
        });
    }
}
