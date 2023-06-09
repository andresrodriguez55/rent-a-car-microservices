package com.kodlamaio.rentalservice.business.concretes;

import com.kodlamaio.commonpackage.events.invoice.CreateInvoiceEvent;
import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalDeletedEvent;
import com.kodlamaio.commonpackage.utils.dto.requests.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.dto.responses.CarClientResponse;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.rentalservice.api.clients.CarClient;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;
import com.kodlamaio.rentalservice.business.rules.RentalBusinessRules;
import com.kodlamaio.rentalservice.entities.Rental;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService
{
    private final RentalRepository repository;
    private final ModelMapperService mapper;
    private final RentalBusinessRules rules;
    private final CarClient carClient;

    private final KafkaProducer producer;

    @Override
    public List<GetAllRentalsResponse> getAll()
    {
        var rentals = repository.findAll();
        var response = rentals
                .stream()
                .map(rental -> mapper.forResponse().map(rental, GetAllRentalsResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetRentalResponse getById(UUID id)
    {
        rules.checkIfRentalExists(id);
        var rental = repository.findById(id).orElseThrow();
        var response = mapper.forResponse().map(rental, GetRentalResponse.class);

        return response;
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) throws InterruptedException
    {
        rules.ensureCarIsAvailable(request.getCarId());
        Rental rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(null);
        rental.setTotalPrice(getTotalPrice(rental));
        rental.setRentedAt(LocalDate.now());

        //make payment
        CreateRentalPaymentRequest paymentRequest = new CreateRentalPaymentRequest();
        mapper.forRequest().map(request, paymentRequest);
        paymentRequest.setPrice(getTotalPrice(rental));
        rules.ensurePaymentIsProcessed(paymentRequest);

        //save rental
        repository.save(rental);
        sendKafkaRentalCreatedEvent(request.getCarId());

        //publish invoice
        CarClientResponse carClientResponse = carClient.getCar(request.getCarId()); //hata olursa????
        CreateInvoiceEvent createInvoiceEvent = new CreateInvoiceEvent();
        mapper.forRequest().map(request, createInvoiceEvent);
        mapper.forRequest().map(carClientResponse, createInvoiceEvent);
        mapper.forRequest().map(rental, createInvoiceEvent);
        createInvoiceEvent.setRentedAt(LocalDateTime.now());
        sendKafkaInvoiceCreateEvent(createInvoiceEvent);

        var response = mapper.forResponse().map(rental, CreateRentalResponse.class);
        return response;
    }

    @Override
    public UpdateRentalResponse update(UUID id, UpdateRentalRequest request)
    {
        rules.checkIfRentalExists(id);
        var rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(id);
        repository.save(rental);
        var response = mapper.forResponse().map(rental, UpdateRentalResponse.class);

        return response;
    }

    @Override
    public void delete(UUID id)
    {
        rules.checkIfRentalExists(id);
        var carId = repository.findById(id).orElseThrow().getCarId();
        repository.deleteById(id);
        sendKafkaRentalDeletedEvent(carId);

    }

    private double getTotalPrice(Rental rental)
    {
        return rental.getDailyPrice() * rental.getRentedForDays();
    }

    private void sendKafkaRentalCreatedEvent(UUID carId)
    {
        producer.sendMessage(new RentalCreatedEvent(carId), "rental-created");
    }

    private void sendKafkaRentalDeletedEvent(UUID carId)
    {
        producer.sendMessage(new RentalDeletedEvent(carId), "rental-deleted");
    }

    private void sendKafkaInvoiceCreateEvent(CreateInvoiceEvent event)
    {
        producer.sendMessage(event, "create-invoice");
    }
}
