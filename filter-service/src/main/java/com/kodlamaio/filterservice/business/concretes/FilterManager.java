package com.kodlamaio.filterservice.business.concretes;

import com.code.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.filterservice.business.abstracts.FilterService;
import com.kodlamaio.filterservice.business.dto.responses.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.responses.GetFilterResponse;
import com.kodlamaio.filterservice.entities.Filter;
import com.kodlamaio.filterservice.repository.FilterRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
//@RequiredArgsConstructor -> final yazılanlar static ile eşitlenir, final olmayanlar...
public class FilterManager implements FilterService
{
    private final FilterRepository repository;
    private ModelMapperService mapper;

    @Override
    public List<GetAllFiltersResponse> getAll()
    {
        var filters = repository.findAll();
        var response = filters
                .stream()
                .map(filter -> mapper.forResponse().map(filter, GetAllFiltersResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetFilterResponse getById(UUID id)
    {
        var filter = repository.findById(id).orElseThrow();
        var response = mapper.forResponse().map(filter, GetFilterResponse.class);

        return response;
    }

    @Override
    public void add(Filter filter)
    {
        filter.setId(UUID.randomUUID());
        repository.save(filter);
    }

    @Override
    public void delete(UUID id)
    {
        repository.deleteById(id);
    }


    @Override
    public void deleteAllByBrandId(UUID brandId)
    {
        repository.deleteAllByBrandId(brandId);
    }

    @Override
    public void deleteByCarId(UUID carId)
    {
        repository.deleteByCarId(carId);
    }

    @Override
    public void deleteAllByModelId(UUID modelId)
    {
        repository.deleteAllByModelId(modelId);
    }
}
