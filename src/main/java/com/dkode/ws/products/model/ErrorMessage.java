package com.dkode.ws.products.model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorMessage {
    private Date timestamp;
    private String message;
    private String details;
}
