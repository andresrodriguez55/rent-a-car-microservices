package com.kodlamaio.maintenanceservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CarClientFallback implements CarClient
{
    @Override
    public ClientResponse checkIfCarAvailable(UUID carId) throws InterruptedException
    {
        log.info("Inventory SERVICE IS DOWN");
        throw new RuntimeException("Inventory Down");
    }
}
