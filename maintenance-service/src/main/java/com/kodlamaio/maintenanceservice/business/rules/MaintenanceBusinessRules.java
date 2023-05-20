package com.kodlamaio.maintenanceservice.business.rules;

import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.maintenanceservice.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MaintenanceBusinessRules
{
    private final MaintenanceRepository repository;

    public void checkIfMaintenanceExists(UUID id)
    {
        if(!repository.existsById(id))
        {
            //throw new BusinessException(Messages.Maintenance.NotExists);
            throw new BusinessException("Messages.Maintenance.NotExists");
        }
    }

    public void checkIfCarIsNotUnderMaintenance(UUID carId)
    {
        if(!repository.existsByCarIdAndIsCompletedIsFalse(carId))
            throw new BusinessException("Messages.Maintenance.NotExists");
        //throw new BusinessException(Messages.Maintenance.CarNotExists);
    }

    public void checkIfCarIsUnderMaintenance(UUID carId)
    {
        if(repository.existsByCarIdAndIsCompletedIsFalse(carId))
            throw new BusinessException("Messages.Maintenance.NotExists");
        //throw new BusinessException(Messages.Maintenance.CarExists);
    }

    /*
    public void checkCarAvailabilityForMaintenance(State state)
    {
        if(state.equals(State.RENTED))
        {
            throw new BusinessException(Messages.Maintenance.CarIsRented);
        }
    }
    */
}