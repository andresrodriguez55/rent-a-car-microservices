package com.kodlamaio.commonpackage.events.inventory;

import com.kodlamaio.commonpackage.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarCreatedEvent implements Event
{
    private UUID carId;
    private UUID modelId;
    private UUID brandId;
    private String modelName;
    private String brandName;
    private String plate;
    private int modelYear;
    private double dailyPrice;
    private String state;
}
