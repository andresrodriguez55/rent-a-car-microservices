package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallback implements PaymentClient
{
    @Override
    public ClientResponse processRentalPayment(CreateRentalPaymentRequest request) throws InterruptedException
    {
        log.info("PAYMENT SERVICE IS DOWN");
        throw new RuntimeException("PAYMENT DOWN");
    }
}
