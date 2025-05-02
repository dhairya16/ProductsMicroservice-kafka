package com.dkode.ws.products.service;

import com.dcode.ws.core.ProductCreatedEvent;
import com.dkode.ws.products.model.Product;
import org.apache.kafka.clients.producer.ProducerRecord;
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
    public String createProduct(Product product) throws Exception {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                productCreatedEvent
        );
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(record).get();

        return productId;
    }
}
