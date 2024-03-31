package com.example.supplier.exception;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timestamp, int status, String message) {
}
