package com.kodlamaio.commonpackage.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRentalPaymentRequest
{
    private String cardNumber;
    private String cardHolder;
    private int cardExpirationYear;
    private int cardExpirationMonth;
    private String cardCvv;
    private double price;

    @Override
    public String toString()
    {
        return "{" +
                "cardNumber:'" + cardNumber + '\'' +
                ", cardHolder:'" + cardHolder + '\'' +
                ", cardExpirationYear:" + cardExpirationYear +
                ", cardExpirationMonth:" + cardExpirationMonth +
                ", cardCvv:'" + cardCvv + '\'' +
                ", price:" + price +
                '}';
    }
}