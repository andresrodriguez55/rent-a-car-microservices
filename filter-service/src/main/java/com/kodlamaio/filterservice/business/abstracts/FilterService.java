package com.kodlamaio.filterservice.business.abstracts;

import com.kodlamaio.filterservice.business.dto.responses.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.responses.GetFilterResponse;
import com.kodlamaio.filterservice.entities.Filter;

import java.util.List;
import java.util.UUID;

public interface FilterService
{
    List<GetAllFiltersResponse> getAll();
    GetFilterResponse getById(UUID id);

    //Ağıdakiler dış dünyaya açık değil
    void add(Filter filter);
    void delete(UUID id);
    void deleteAllByBrandId(UUID brandId);
    void deleteAllByModelId(UUID modelId);
    void deleteByCarId(UUID carId);
    Filter getByCarId(UUID carId);
}
