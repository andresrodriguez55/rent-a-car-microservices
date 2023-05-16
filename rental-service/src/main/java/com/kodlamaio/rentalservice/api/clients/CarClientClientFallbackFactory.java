package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class CarClientClientFallbackFactory implements FallbackFactory<CarClient>
{
    @Override
    public CarClient create(Throwable cause)
    {
        log.error("An exception occurred when calling the UserSessionClient", cause);

        return new CarClient()
        {
            @Override
            public ClientResponse checkIfCarAvailable(UUID carId)
            {
                log.info("[Fallback] validateSession");
                return new ClientResponse(false, "SERVER DOWN");
            }
        };
    }
}
