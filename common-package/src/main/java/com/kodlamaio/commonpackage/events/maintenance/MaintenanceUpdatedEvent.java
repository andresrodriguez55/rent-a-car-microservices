package com.kodlamaio.commonpackage.events.maintenance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaintenanceUpdatedEvent
{
    private UUID carId;
}
