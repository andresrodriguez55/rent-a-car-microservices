package com.code.inventoryservice.business.abstracts;

import com.code.inventoryservice.business.dto.requests.create.CreateModelRequest;
import com.code.inventoryservice.business.dto.requests.update.UpdateModelRequest;
import com.code.inventoryservice.business.dto.responses.create.CreateModelResponse;
import com.code.inventoryservice.business.dto.responses.get.GetAllModelsResponse;
import com.code.inventoryservice.business.dto.responses.get.GetModelResponse;
import com.code.inventoryservice.business.dto.responses.update.UpdateModelResponse;

import java.util.List;
import java.util.UUID;

public interface ModelService {
    List<GetAllModelsResponse> getAll();
    GetModelResponse getById(UUID id);
    CreateModelResponse add(CreateModelRequest request);
    UpdateModelResponse update(UUID id, UpdateModelRequest request);
    void delete(UUID id);
}
