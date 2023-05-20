package com.kodlamaio.filterservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCompletedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCreatedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceDeletedEvent;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.filterservice.business.abstracts.FilterService;
import com.kodlamaio.filterservice.entities.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceConsumer
{
    private final FilterService service;
    private final ModelMapperService mapper;

    @KafkaListener
    (
            topics = "maintenance-created",
            groupId = "filter-maintenance-create"
    )
    public void consume(MaintenanceCreatedEvent event)
    {
        updateFilterCarState(event.getCarId(), "MAINTENANCE");
        log.info("Mantenance created event consumed {}", event);
    }

    @KafkaListener
    (
            topics = "maintenance-deleted",
            groupId = "filter-maintenance-delete"
    )
    public void consume(MaintenanceDeletedEvent event)
    {
        updateFilterCarState(event.getCarId(), "AVAILABLE");
        log.info("Maintenance deleted event consumed {}", event);
    }

    @KafkaListener
    (
            topics = "maintenance-completed",
            groupId = "filter-maintenance-complete"
    )
    public void consume(MaintenanceCompletedEvent event)
    {
        updateFilterCarState(event.getCarId(), "AVAILABLE");
        log.info("Maintenance completed event consumed {}", event);
    }

    private void updateFilterCarState(UUID carId, String state)
    {
        Filter filter = service.getByCarId(carId);
        filter.setState(state);
        service.add(filter);
    }
}
