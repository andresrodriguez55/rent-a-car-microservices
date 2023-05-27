package com.kodlamaio.invoiceservice.business.abstracts;

import com.kodlamaio.invoiceservice.business.dto.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.entities.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceService
{
    List<GetAllInvoicesResponse> getAll();
    GetInvoiceResponse getById(UUID id);
    void add(Invoice invoice);
    //UpdateInvoiceResponse update(UUID id, UpdateInvoiceRequest request);
    //void delete(UUID id);
    //void deleteByRentalId(int id);
}
