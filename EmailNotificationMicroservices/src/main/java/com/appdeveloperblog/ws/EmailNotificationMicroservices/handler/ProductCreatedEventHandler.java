package com.appdeveloperblog.ws.EmailNotificationMicroservices.handler;

import com.appdeveloperblog.ws.EmailNotificationMicroservices.error.*;
import com.appdeveloperblog.ws.core.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-events-topic", groupId = "product-created-events")
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent){
        LOGGER.info("Received a new event: " + productCreatedEvent);

        final var url = "http://localhost:8082";
        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().is2xxSuccessful()){
                LOGGER.info("Received a new event: " + response.getBody());
            }
        }catch(ResourceAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RetryableException(e.getMessage());
        }catch(HttpServerErrorException e){
            LOGGER.error(e.getMessage());
            throw new NotRetryableException(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new NotRetryableException(e.getMessage());
        }
    }
}
