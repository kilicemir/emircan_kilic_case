package com.insider.petstore.api.model;

import lombok.Data;

@Data
public class ApiErrorResponse {

    private Integer code;
    private String type;
    private String message;
}
