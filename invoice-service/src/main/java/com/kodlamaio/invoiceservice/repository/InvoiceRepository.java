package com.kodlamaio.invoiceservice.repository;

import com.kodlamaio.invoiceservice.entities.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface InvoiceRepository extends MongoRepository<Invoice, UUID>
{
}
