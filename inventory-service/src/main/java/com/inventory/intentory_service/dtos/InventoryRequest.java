package com.inventory.intentory_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotBlank(message = "El productId es obligatorio")
    private String productId;

    @NotNull(message = "El stock disponible no puede ser 0")
    private Integer available;

    private Integer reserved = 0;
}
