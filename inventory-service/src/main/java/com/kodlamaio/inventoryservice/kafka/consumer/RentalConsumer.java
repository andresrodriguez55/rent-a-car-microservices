package com.kodlamaio.inventoryservice.kafka.consumer;

import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalDeletedEvent;
import com.kodlamaio.inventoryservice.business.abstracts.CarService;
import com.kodlamaio.inventoryservice.entities.enums.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalConsumer
{
    private final CarService service;

    @KafkaListener(
            topics = "rental-created",
            groupId = "inventory-rental-create"
    )
    public void consume(RentalCreatedEvent event)
    {
        service.changeStateByCarId(State.RENTED, event.getCarId());
        log.info("Rental created event consumed {}", event);
    }

    @KafkaListener(
            topics = "rental-deleted",
            groupId = "inventory-rental-delete"
    )
    public void consume(RentalDeletedEvent event)
    {
        service.changeStateByCarId(State.AVAILABLE, event.getCarId());
        log.info("Rental deleted event consumed {}", event);
    }
}
