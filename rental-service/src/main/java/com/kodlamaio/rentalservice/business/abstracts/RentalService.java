package com.kodlamaio.rentalservice.business.abstracts;

import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.UUID;

public interface RentalService
{
    List<GetAllRentalsResponse> getAll();
    GetRentalResponse getById(UUID id);
    CreateRentalResponse add(CreateRentalRequest request) throws InterruptedException;
    UpdateRentalResponse update(UUID id, UpdateRentalRequest request);
    void delete(UUID id);
}
