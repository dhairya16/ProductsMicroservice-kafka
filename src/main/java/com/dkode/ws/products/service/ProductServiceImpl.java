package com.dkode.ws.products.service;

import com.dkode.ws.products.model.Product;
import org.springframework.kafka.support.SendResult;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(Product product) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        CompletableFuture<SendResult<String, ProductCreatedEvent>> futureObj = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        futureObj.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully {}", result.getRecordMetadata());
            }
        });

        return productId;
    }
}
