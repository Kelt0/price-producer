package com.example.price_producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class PriceGenerator {

    private static final String TOPIC_NAME = "prices-topic";
    private final KafkaTemplate<String, PriceUpdate> kafkaTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(PriceGenerator.class);
    private final Random random = new Random();

    @Autowired
    public PriceGenerator(KafkaTemplate<String, PriceUpdate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 15000)
    public void generatingPrice(){
        double randomPrice = 10.0 + (90 * random.nextDouble());
//      String message = String.format("{\"suppliedPrice\": %s}", randomPrice);
        PriceUpdate newGeneratedPrice = new PriceUpdate(randomPrice);
        LOG.info("Сгенерирована новая цена и отправлена в Kafka: {}", newGeneratedPrice.toString());
        kafkaTemplate.send(TOPIC_NAME, newGeneratedPrice);
    }
}
