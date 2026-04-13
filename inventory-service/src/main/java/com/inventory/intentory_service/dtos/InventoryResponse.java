package com.inventory.intentory_service.dtos;

import lombok.Data;

@Data
public class InventoryResponse {
    private String id;
    private String productId;
    private Integer available;
    private Integer reserved;
    private Integer totalStock;

    public Integer getTotalStock() {
        return available + reserved;
    }
}
