package com.kodlamaio.maintenanceservice.business.concretes;

import com.kodlamaio.maintenanceservice.business.abstracts.MaintenanceService;
import com.kodlamaio.maintenanceservice.business.dto.requests.create.CreateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.requests.update.UpdateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.responses.create.CreateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.get.GetAllMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.get.GetMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.update.UpdateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.rules.MaintenanceBusinessRules;
import com.kodlamaio.maintenanceservice.entities.Maintenance;
import com.kodlamaio.maintenanceservice.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService
{
    private final MaintenanceRepository repository;
    //private final CarService carService;
    private final ModelMapper mapper;
    private final MaintenanceBusinessRules rules;

    @Override
    public List<GetAllMaintenanceResponse> getAll()
    {
        List<Maintenance> maintenances = repository.findAll();

        List<GetAllMaintenanceResponse> response =
                maintenances.stream().map(maintenance -> mapper.map(maintenance, GetAllMaintenanceResponse.class)).toList();
        return response;
    }

    @Override
    public GetMaintenanceResponse getById(UUID id)
    {
        //rules.checkIfMaintenanceExists(id);

        Maintenance maintenance = repository.findById(id).get();

        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(UUID carId)
    {
        //rules.checkIfCarIsNotUnderMaintenance(carId);

        Maintenance maintenance = repository.findByCarIdAndIsCompletedIsFalse(carId);
        maintenance.setCompleted(true);
        maintenance.setEndDate(LocalDateTime.now());
        //carService.changeState(carId, State.AVAILABLE);
        repository.save(maintenance);

        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request)
    {
        //rules.checkIfCarIsUnderMaintenance(request.getCarId());
        //rules.checkCarAvailabilityForMaintenance(carService.getById(request.getCarId()).getState());

        Maintenance maintenance = mapper.map(request, Maintenance.class);
        //maintenance.setId(0);
        maintenance.setCompleted(false);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setEndDate(null);
        repository.save(maintenance);
        //carService.changeState(request.getCarId(), State.MAINTENANCE);

        CreateMaintenanceResponse response = mapper.map(maintenance, CreateMaintenanceResponse.class);
        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(UUID id, UpdateMaintenanceRequest request)
    {
        //rules.checkIfMaintenanceExists(id);

        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(id);
        repository.save(maintenance);

        UpdateMaintenanceResponse response = mapper.map(maintenance, UpdateMaintenanceResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id)
    {
        //rules.checkIfMaintenanceExists(id);
        //makeCarAvailableIfIsCompletedFalse(id);
        repository.deleteById(id);
    }

    private void makeCarAvailableIfIsCompletedFalse(int id)
    {
        /* int carId = repository.findById(id).get().getCar().getId();
        if(repository.existsByCarIdAndIsCompletedIsFalse(carId))
        {
            carService.changeState(carId, State.AVAILABLE);
        }

         */
    }
}