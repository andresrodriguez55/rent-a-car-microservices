package com.kodlamaio.inventoryservice.business.abstracts;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.inventoryservice.business.dto.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryservice.business.dto.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryservice.business.dto.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetCarResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.update.UpdateCarResponse;
import com.kodlamaio.inventoryservice.entities.enums.State;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<GetAllCarsResponse> getAll();
    GetCarResponse getById(UUID id);
    CreateCarResponse add(CreateCarRequest request);
    UpdateCarResponse update(UUID id, UpdateCarRequest request);
    void delete(UUID id);
    ClientResponse checkIfCarAvailable(UUID id);
    void changeStateByCarId(State state, UUID id);
}
