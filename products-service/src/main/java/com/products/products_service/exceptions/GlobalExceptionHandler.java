package com.products.products_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error","Conflicto de datos");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(Map.of("errors", List.of(error)), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    assert error.getDefaultMessage() != null;
                    return Map.of(
                            "status", "400",
                            "soruce", Map.of("pointer", "/data/attributes/" + error.getField()),
                            "title", "Invalide Attribute",
                            "detail", error.getDefaultMessage()
                    );
                }).toList();
        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
    }
}
