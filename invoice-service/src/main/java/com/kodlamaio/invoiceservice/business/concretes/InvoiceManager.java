package com.kodlamaio.invoiceservice.business.concretes;

import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.business.rules.InvoiceBusinessRules;
import com.kodlamaio.invoiceservice.entities.Invoice;
import com.kodlamaio.invoiceservice.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service //once time of creation?
@AllArgsConstructor
public class InvoiceManager implements InvoiceService
{
    private final InvoiceRepository repository;
    private ModelMapperService mapper;
    private final InvoiceBusinessRules rules;

    @Override
    public List<GetAllInvoicesResponse> getAll()
    {
        List<Invoice> invoices = repository.findAll();
        List<GetAllInvoicesResponse> response =
                invoices.stream().map(invoice -> mapper.forResponse().map(invoice, GetAllInvoicesResponse.class)).toList();
        return response;
    }

    @Override
    public GetInvoiceResponse getById(UUID id)
    {
        rules.checkIfInvoiceExists(id);
        Invoice invoice = repository.findById(id).get();
        GetInvoiceResponse response = mapper.forResponse().map(invoice, GetInvoiceResponse.class);
        return response;
    }

    @Override
    public void add(Invoice invoice)
    {
        repository.save(invoice);
    }
}
