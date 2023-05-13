package com.kodlamaio.rentalservice.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "inventory-service") //optional name
public interface CarClient
{
    @GetMapping(value = "/api/cars/check-car-available/{carId}")
    void checkIfCarAvailable(@PathVariable UUID carId);
}
