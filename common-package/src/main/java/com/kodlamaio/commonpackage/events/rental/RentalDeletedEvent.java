package com.kodlamaio.commonpackage.events.rental;

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
public class RentalDeletedEvent implements Event
{
    private UUID carId;
}