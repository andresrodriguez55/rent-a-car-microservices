package com.kodlamaio.maintenanceservice.business.concretes;

import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCompletedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCreatedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceDeletedEvent;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
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
    private final ModelMapperService mapper;
    private final MaintenanceBusinessRules rules;
    private final KafkaProducer producer;


    @Override
    public List<GetAllMaintenanceResponse> getAll()
    {
        List<Maintenance> maintenances = repository.findAll();

        List<GetAllMaintenanceResponse> response =
                maintenances.stream().map(maintenance -> mapper.forResponse().map(maintenance, GetAllMaintenanceResponse.class)).toList();
        return response;
    }

    @Override
    public GetMaintenanceResponse getById(UUID id)
    {
        //rules.checkIfMaintenanceExists(id);

        Maintenance maintenance = repository.findById(id).get();

        GetMaintenanceResponse response = mapper.forResponse().map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(UUID carId)
    {
        rules.checkIfCarIsNotUnderMaintenance(carId);

        Maintenance maintenance = repository.findByCarIdAndIsCompletedIsFalse(carId);
        maintenance.setCompleted(true);
        maintenance.setEndDate(LocalDateTime.now());

        //carService.changeState(carId, State.AVAILABLE);
        sendKafkaMaintenanceCompletedEvent(carId);

        repository.save(maintenance);

        GetMaintenanceResponse response = mapper.forResponse().map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    // client
    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) throws InterruptedException
    {
        rules.checkIfCarIsUnderMaintenance(request.getCarId());
        rules.ensureCarIsAvailable(request.getCarId());

        Maintenance maintenance = mapper.forRequest().map(request, Maintenance.class);
        maintenance.setId(null);
        maintenance.setCompleted(false);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setEndDate(null);
        repository.save(maintenance);

        //carService.changeState(request.getCarId(), State.MAINTENANCE);
        sendKafkaMaintenanceCreatedEvent(request.getCarId());

        CreateMaintenanceResponse response = mapper.forResponse().map(maintenance, CreateMaintenanceResponse.class);
        return response;
    }

    //isComplete değşimi dikkate alınmadı!
    @Override
    public UpdateMaintenanceResponse update(UUID id, UpdateMaintenanceRequest request)
    {
        rules.checkIfMaintenanceExists(id);

        Maintenance maintenance = mapper.forRequest().map(request, Maintenance.class);
        maintenance.setId(id);
        repository.save(maintenance);

        UpdateMaintenanceResponse response = mapper.forResponse().map(maintenance, UpdateMaintenanceResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id)
    {
        rules.checkIfMaintenanceExists(id);
        makeCarAvailableIfIsCompletedFalse(id);
        repository.deleteById(id);
    }

    private void makeCarAvailableIfIsCompletedFalse(UUID id)
    {
        Maintenance maintenance = repository.findById(id).orElseThrow();
        UUID carId = maintenance.getCarId();
        if(repository.existsByCarIdAndIsCompletedIsFalse(carId))
        {
            sendKafkaMaintenanceDeletedEvent(carId);
        }
    }

    private void sendKafkaMaintenanceCreatedEvent(UUID carId)
    {
        producer.sendMessage(new MaintenanceCreatedEvent(carId), "maintenance-created");
    }

    private void sendKafkaMaintenanceDeletedEvent(UUID carId)
    {
        producer.sendMessage(new MaintenanceDeletedEvent(carId), "maintenance-deleted");
    }

    private void sendKafkaMaintenanceCompletedEvent(UUID carId)
    {
        producer.sendMessage(new MaintenanceCompletedEvent(carId), "maintenance-completed");
    }
}