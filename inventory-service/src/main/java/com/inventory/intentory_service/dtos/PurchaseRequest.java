package com.inventory.intentory_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseRequest {
    @NotBlank(message = "El Id del producto es obligatorio")
    private String productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debes comprar al menos 1")
    private Integer quantity;
}
