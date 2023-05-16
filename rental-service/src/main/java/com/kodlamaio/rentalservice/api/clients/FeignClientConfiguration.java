package com.kodlamaio.rentalservice.api.clients;

import feign.RequestInterceptor;
import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j

@Configuration
public class FeignClientConfiguration
{
    private static final int MAX_RETRIES = 5;
    private static final int BACKOFF_PERIOD_MS = 100;

    @Bean
    public CustomRetryer retryer()
    {
        log.info("retryyyyyyyy");
        return new CustomRetryer(100L, TimeUnit.SECONDS.toMillis(3L), 5);
    }
}
@Slf4j
class CustomRetryer extends Retryer.Default
{
    public CustomRetryer(long period, long maxPeriod, int maxAttempts){
        super(period, maxPeriod, maxAttempts);
    }

    @Override
    public void continueOrPropagate(RetryableException e)
    {
        log.info("Going to retry for ", e);
        super.continueOrPropagate(e);
    }

    @Override
    public Retryer clone(){
        return new CustomRetryer(100L, TimeUnit.SECONDS.toMillis(3L), 4);
    }
}