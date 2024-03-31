package com.example.supplier.exception;

import lombok.Data;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timestamp, int status, String message) {
}
