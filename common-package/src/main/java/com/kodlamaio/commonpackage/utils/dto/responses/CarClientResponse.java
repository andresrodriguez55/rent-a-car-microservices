package com.kodlamaio.commonpackage.utils.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarClientResponse extends ClientResponse
{
    private String modelName;
    private String brandName;
    private String plate;
    private int modelYear;
}
