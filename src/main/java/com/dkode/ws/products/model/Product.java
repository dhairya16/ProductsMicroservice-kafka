package com.dkode.ws.products.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product {

    private String title;
    private BigDecimal price;
    private Integer quantity;

}
