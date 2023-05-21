package com.kodlamaio.paymentservice.business.rules;

import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.paymentservice.business.dto.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentBusinessRules
{
    private final PaymentRepository repository;

    public void checkIfPaymentExists(UUID id)
    {
        if(!repository.existsById(id))
        {
            throw new BusinessException("PAYMENT_NOT_FOUND");
        }
    }

    public void checkIfCardExistsByCardNumber(CreatePaymentRequest request)
    {
        if(repository.existsByCardNumber(request.getCardNumber()))
        {
            throw new BusinessException("CARD_NUMBER_ALREADY_EXISTS");
        }
    }
    public void checkIfPaymentIsValid(CreateRentalPaymentRequest request)
    {
        if(!repository.existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(
                request.getCardNumber(),
                request.getCardHolder(),
                request.getCardExpirationYear(),
                request.getCardExpirationMonth(),
                request.getCardCvv()
        ))
        {
            throw new BusinessException("NOT_VALID_PAYMENT");
        }
    }

    public void checkIfBalanceIsEnough(double price, double balance)
    {
        if(balance < price)
        {
            throw new BusinessException("NOT_ENOUGH_MONEY");
        }
    }
}