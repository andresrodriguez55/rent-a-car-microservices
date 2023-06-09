package com.kodlamaio.invoiceservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.invoice.CreateInvoiceEvent;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.entities.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalConsumer
{
    private final InvoiceService service;
    private final ModelMapperService mapper;

    @KafkaListener
    (
            topics = "create-invoice",
            groupId = "rental-invoice-create"
    )
    public void consume(CreateInvoiceEvent event)
    {
        var invoice = mapper.forRequest().map(event, Invoice.class);
        service.add(invoice);
        log.info("Create invoice event consumed {}", event);
    }
}
