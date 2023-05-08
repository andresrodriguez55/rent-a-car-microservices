package com.code.inventoryservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryProducer
{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage()
    {

    }
}
