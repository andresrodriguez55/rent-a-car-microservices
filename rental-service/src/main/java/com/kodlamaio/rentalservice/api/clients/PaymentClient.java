package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service",
        fallback = PaymentClientFallback.class)
public interface PaymentClient
{
    @PostMapping(value = "/api/payments/process-rental-payment", consumes = "application/x-www-form-urlencoded")
     ClientResponse processRentalPayment(CreateRentalPaymentRequest request)
            throws InterruptedException;
}