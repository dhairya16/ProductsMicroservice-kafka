package com.dkode.ws.products.controller;

import com.dkode.ws.products.model.ErrorMessage;
import com.dkode.ws.products.model.Product;
import com.dkode.ws.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        String productId = null;
        try {
            productId = productService.createProduct(product);
        } catch (Exception e) {
            ErrorMessage errorMessage = ErrorMessage.builder()
                    .timestamp(new Date())
                    .message(e.getMessage())
                    .details("/products")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
