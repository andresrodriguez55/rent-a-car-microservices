package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.responses.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service",
        fallback = PaymentClientFallback.class)
public interface PaymentClient
{
    //@Headers("Content-Type: application/json")
    @PostMapping(value = "/api/payments/process-rental-payment")
     ClientResponse processRentalPayment(CreateRentalPaymentRequest request)
            throws InterruptedException;
}