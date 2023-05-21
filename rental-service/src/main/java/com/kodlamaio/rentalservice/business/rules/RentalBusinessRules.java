package com.kodlamaio.rentalservice.business.rules;

import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.rentalservice.api.clients.CarClient;
import com.kodlamaio.rentalservice.api.clients.PaymentClient;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RentalBusinessRules
{
    private final RentalRepository repository;
    private final CarClient carClient;
    private final PaymentClient paymentClient;

    public void checkIfRentalExists(UUID id)
    {
        if (!repository.existsById(id))
        {
            throw new BusinessException("MODEL_NOT_EXISTS");
        }
    }

    public void ensureCarIsAvailable(UUID carId) throws InterruptedException
    {
        var response = carClient.checkIfCarAvailable(carId);
        if(!response.isSuccess())
        {
            throw new BusinessException(response.getMessage());
        }
    }

    public void ensurePaymentIsProcessed(CreateRentalPaymentRequest request) throws InterruptedException
    {
        var response = paymentClient.processRentalPayment(request);
        if(!response.isSuccess())
        {
            throw new BusinessException(response.getMessage());
        }
    }
}
