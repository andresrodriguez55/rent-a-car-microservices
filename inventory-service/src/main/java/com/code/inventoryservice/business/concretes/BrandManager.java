package com.code.inventoryservice.business.concretes;

import com.code.commonpackage.utils.mappers.ModelMapperService;
import com.code.inventoryservice.business.abstracts.BrandService;
import com.code.inventoryservice.business.dto.requests.create.CreateBrandRequest;
import com.code.inventoryservice.business.dto.requests.update.UpdateBrandRequest;
import com.code.inventoryservice.business.dto.responses.create.CreateBrandResponse;
import com.code.inventoryservice.business.dto.responses.get.GetAllBrandsResponse;
import com.code.inventoryservice.business.dto.responses.get.GetBrandResponse;
import com.code.inventoryservice.business.dto.responses.update.UpdateBrandResponse;
import com.code.inventoryservice.business.rules.BrandBusinessRules;
import com.code.inventoryservice.entities.Brand;
import com.code.inventoryservice.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository repository;
    private final ModelMapperService mapper;
    private final BrandBusinessRules rules;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        var brands = repository.findAll();
        var response = brands
                .stream()
                .map(brand -> mapper.forResponse().map(brand, GetAllBrandsResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetBrandResponse getById(UUID id) {
        rules.checkIfBrandExists(id);
        var brand = repository.findById(id).orElseThrow();
        var response = mapper.forResponse().map(brand, GetBrandResponse.class);

        return response;
    }

    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
        var brand = mapper.forRequest().map(request, Brand.class);
        repository.save(brand);
        var response = mapper.forResponse().map(brand, CreateBrandResponse.class);

        return response;
    }

    @Override
    public UpdateBrandResponse update(UUID id, UpdateBrandRequest request) {
        rules.checkIfBrandExists(id);
        var brand = mapper.forRequest().map(request, Brand.class);
        brand.setId(id);
        repository.save(brand);
        var response = mapper.forResponse().map(brand, UpdateBrandResponse.class);

        return response;
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfBrandExists(id);
        repository.deleteById(id);
    }
}
