package com.inventory.intentory_service.controllers;

import com.inventory.intentory_service.dtos.InventoryResponse;
import com.inventory.intentory_service.dtos.InventoryRequest;
import com.inventory.intentory_service.dtos.PurchaseRequest;
import com.inventory.intentory_service.entities.Inventory;
import com.inventory.intentory_service.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody InventoryRequest request){

        return new ResponseEntity<>(inventoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getByProductId(@PathVariable String productId){
        return ResponseEntity.ok(inventoryService.findByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
        @PageableDefault(size = 10) Pageable pageable){
            Page<Inventory>  inventoryPage = inventoryService.findAll(pageable);
            Map<String, Object> response = new HashMap<>();
            response.put("data", inventoryPage.getContent());
            Map<String, Object> meta  = new HashMap<>();
            meta.put("meta", Map.of(
                    "totalItems", inventoryPage.getTotalElements(),
                    "totalPages", inventoryPage.getTotalPages(),
                    "currentPage", inventoryPage.getNumber(),
                    "page-size", inventoryPage.getSize()
            ));
            response.put("meta", meta);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/purchases")

    public ResponseEntity<InventoryResponse> purchase(@Valid @RequestBody PurchaseRequest request){
        return new ResponseEntity<>(inventoryService.purchase(request.getProductId(), request.getQuantity()), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable String productId, @RequestParam Integer quantity){
        return ResponseEntity.ok(inventoryService.updateAvailableStock(productId, quantity));
    }
}
