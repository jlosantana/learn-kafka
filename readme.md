# Spring Kafka Study Repository

This repository serves as a study guide for implementing Kafka with Spring Boot. It includes examples of a Kafka producer and consumer written in Java, as well as a `docker-compose.yml` file to quickly set up a local Kafka and Zookeeper environment for testing.

## Project Structure

### Files and Directories
- **spring-kafka-consumer**: Contains the Spring Boot Kafka consumer project.
- **spring-kafka-producer**: Contains the Spring Boot Kafka producer project.
- **.gitignore**: Specifies files and directories to be ignored by Git.
- **docker-compose.yml**: Configures Docker containers for Kafka and Zookeeper.

## Kafka Producer

### Classes
#### KafkaMessageProducer
A service that sends messages to a specified Kafka topic.

```java
@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Unable to send message=[" + message + "] to topic=[" + topic + "] with error=[" + ex.getMessage() + "]");
            } else {
                System.out.println("Sent message=[" + message + "] to topic=[" + topic + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }
}
```

#### EventController
A REST controller to expose an API endpoint for publishing messages to Kafka.

```java
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
```

## Kafka Consumer

### Classes
#### KafkaMessageConsumer
A service that listens to messages on the specified Kafka topic and logs them.

```java
@Service
public class KafkaMessageConsumer {

    Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    @KafkaListener(topics = "javatechie-topic", groupId = "jt-group-1")
    public void consume(String message) {
        log.info("consumer consume message {}", message);
    }
}
```

## Docker Compose
The `docker-compose.yml` file sets up a local Kafka and Zookeeper environment:

```yaml
version: '3.1'

services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
    container_name: zookeeper

  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: localhost
      KAFKA_ADVERTISED_PORT: 9092
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    container_name: kafka
```

## How to Run the Projects

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Start Kafka and Zookeeper using Docker Compose:
   ```bash
   docker-compose up
   ```

3. Run the producer application:
   - Navigate to the `spring-kafka-producer` directory.
   - Start the application using your preferred IDE or command line:
     ```bash
     ./mvnw spring-boot:run
     ```

4. Run the consumer application:
   - Navigate to the `spring-kafka-consumer` directory.
   - Start the application using your preferred IDE or command line:
     ```bash
     ./mvnw spring-boot:run
     ```

5. Test the setup:
   - Use a tool like Postman or cURL to send a POST request to the producer:
     ```bash
     curl -X POST http://localhost:8080/events/publish/{message}
     ```
   - Replace `{message}` with your desired message.
   - Check the consumer logs to see if the message was received.

## Notes
- Ensure that you have Docker installed and running on your machine.
- Both producer and consumer projects are set to listen to the topic `javatechie-topic`. You can customize this as needed.

## Purpose
This repository is intended for educational purposes to help developers understand how to integrate Kafka with Spring Boot for message-driven microservices.
