package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/events")
public class EventController {


    private final KafkaMessageProducer kafkaMessageProducer;

    public EventController(KafkaMessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }    

    @PostMapping("/publish/{message}")
    public ResponseEntity<?> publishEvent(@PathVariable String message) {
        kafkaMessageProducer.sendMessage("javatechie-topic", message);
        return ResponseEntity.ok().build();
    }
}
