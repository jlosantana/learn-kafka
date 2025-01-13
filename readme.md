curl https://start.spring.io/starter.zip \
-d dependencies=kafka,webflux,actuator \
-d name=spring-kafka-producer \
-d type=maven-project \
-d language=java \
-d packaging=jar \
-d javaVersion=17 \
-o spring-kafka-producer.zip

curl https://start.spring.io/starter.zip \
-d dependencies=kafka,webflux,actuator \
-d name=spring-kafka-consumer \
-d type=maven-project \
-d language=java \
-d packaging=jar \
-d javaVersion=17 \
-o spring-kafka-consumer.zip