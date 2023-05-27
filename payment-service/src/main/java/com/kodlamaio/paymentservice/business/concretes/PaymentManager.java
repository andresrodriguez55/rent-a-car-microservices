package com.kodlamaio.paymentservice.business.concretes;

import com.kodlamaio.commonpackage.utils.dto.responses.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.paymentservice.business.abstracts.PaymentService;
import com.kodlamaio.paymentservice.business.abstracts.PostService;
import com.kodlamaio.paymentservice.business.dto.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.update.UpdatePaymentResponse;
import com.kodlamaio.paymentservice.business.rules.PaymentBusinessRules;
import com.kodlamaio.paymentservice.entities.Payment;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService
{
    private final PaymentRepository repository;
    private final ModelMapperService mapper;
    private final PostService postService;
    private final PaymentBusinessRules rules;

    @Override
    public List<GetAllPaymentsResponse> getAll()
    {
        List<Payment> payments = repository.findAll();

        List<GetAllPaymentsResponse> response =
                payments.stream().map(payment -> mapper.forResponse().map(payment, GetAllPaymentsResponse.class)).toList();
        return response;
    }

    @Override
    public GetPaymentResponse getById(UUID id)
    {
        Payment payment = repository.findById(id).orElseThrow();

        GetPaymentResponse response = mapper.forResponse().map(payment, GetPaymentResponse.class);
        return response;
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request)
    {
        rules.checkIfCardExistsByCardNumber(request);

        Payment payment = mapper.forRequest().map(request, Payment.class);
        payment.setId(null);
        repository.save(payment);

        CreatePaymentResponse response = mapper.forResponse().map(payment, CreatePaymentResponse.class);
        return response;
    }

    @Override
    public UpdatePaymentResponse update(UUID id, UpdatePaymentRequest request)
    {
        rules.checkIfPaymentExists(id);

        Payment payment = mapper.forRequest().map(request, Payment.class);
        payment.setId(id);
        repository.save(payment);

        UpdatePaymentResponse response = mapper.forResponse().map(payment, UpdatePaymentResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id)
    {
        rules.checkIfPaymentExists(id);

        repository.deleteById(id);
    }

    @Override
    public ClientResponse processRentalPayment(CreateRentalPaymentRequest request)
    {
        ClientResponse response = new ClientResponse();
        validatePayment(request, response);
        return response;
    }

    private void validatePayment(CreateRentalPaymentRequest request, ClientResponse response)
    {
        try
        {
            rules.checkIfPaymentIsValid(request);
            Payment payment = repository.findByCardNumber(request.getCardNumber());

            rules.checkIfBalanceIsEnough(request.getPrice(), payment.getBalance());
            //FAKE POS SERVICE
            postService.pay();

            processPayment(payment, request.getPrice());
            response.setSuccess(true);
        }
        catch(Exception e)
        {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
    }

    private void processPayment(Payment payment, double price)
    {
        payment.setBalance(payment.getBalance() - price);
        repository.save(payment);
    }
}