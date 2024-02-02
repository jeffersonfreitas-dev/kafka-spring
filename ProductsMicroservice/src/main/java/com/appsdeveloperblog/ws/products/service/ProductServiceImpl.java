package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.dto.CreatedProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService{
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public String create(CreatedProductRequest product) {
        final var productId = UUID.randomUUID().toString();
        //TODO: Persisted the data into database before sending event
        ProductCreatedEvent event = new ProductCreatedEvent(productId, product.getTitle(),
                product.getPrice(), product.getQuantity());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate
                .send("product-created-events-topic", productId, event);
        future.whenComplete((result, error) -> {
           if(error != null){
                LOG.error("Failed to send message " + error.getMessage());
           }else {
               LOG.info("Message send succesfully " + result.getRecordMetadata());
               LOG.info("Partition: " + result.getRecordMetadata().partition());
               LOG.info("Topic: " +result.getRecordMetadata().topic());
               LOG.info("Offset: " + result.getRecordMetadata().offset());
           }
        });
        LOG.info("***** Returning product id " + productId);
        return productId;
    }
}
