package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service",
        fallback = PaymentClientFallback.class)
public interface PaymentClient
{
    @GetMapping(value = "/api/payments/process-rental-payment")
     ClientResponse processRentalPayment(@RequestBody CreateRentalPaymentRequest request)
            throws InterruptedException;
}