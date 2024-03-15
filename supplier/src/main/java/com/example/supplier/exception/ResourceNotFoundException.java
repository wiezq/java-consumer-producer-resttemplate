package com.example.supplier.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private long id;

    public ResourceNotFoundException(Long id, String message) {
        super(message);
    }
}
