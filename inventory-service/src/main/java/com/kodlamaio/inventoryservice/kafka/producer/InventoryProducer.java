package com.kodlamaio.inventoryservice.kafka.producer;

import com.kodlamaio.commonpackage.events.inventory.BrandDeletedEvent;
import com.kodlamaio.commonpackage.events.inventory.CarCreatedEvent;
import com.kodlamaio.commonpackage.events.inventory.CarDeletedEvent;
import com.kodlamaio.commonpackage.events.inventory.ModelDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final ile çalışır
public class InventoryProducer
{
    private static Logger LOGGER = LoggerFactory.getLogger(InventoryProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(CarCreatedEvent event)
    {
        LOGGER.info(String.format("car-created event => %s", event.toString()));
        Message<CarCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "car-created")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendMessage(CarDeletedEvent event)
    {
        LOGGER.info(String.format("car-deleted event => %s", event.toString()));
        Message<CarDeletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "car-deleted")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendMessage(ModelDeletedEvent event)
    {
        LOGGER.info(String.format("model-deleted event => %s", event.toString()));
        Message<ModelDeletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "model-deleted")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendMessage(BrandDeletedEvent event)
    {
        LOGGER.info(String.format("brand-deleted event => %s", event.toString()));
        Message<BrandDeletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "brand-deleted")
                .build();
        kafkaTemplate.send(message);
    }
}
