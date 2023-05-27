package com.kodlamaio.invoiceservice.business.dto.requests.update;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateInvoiceRequest
{
    @NotBlank
    private String cardHolder;
    @NotBlank
    private String modelName;
    @NotBlank
    private String brandName;
    @NotBlank
    private String plate;
    @Min(1996)
    private int modelYear;
    @DecimalMin(value = "0.01")
    private double dailyPrice;
    @Min(1)
    private int rentedForDays;
    private LocalDateTime rentedAt;
}
