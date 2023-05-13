package com.kodlamaio.rentalservice.business.rules;

import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RentalBusinessRules
{
    private final RentalRepository repository;

    public void checkIfRentalExists(UUID id)
    {
        if (!repository.existsById(id))
        {
            throw new BusinessException("MODEL_NOT_EXISTS");
        }
    }
}
