package com.inventory.intentory_service.services;

import com.inventory.intentory_service.dtos.InventoryRequest;
import com.inventory.intentory_service.dtos.InventoryResponse;
import com.inventory.intentory_service.entities.Inventory;
import com.inventory.intentory_service.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;
    private final WebClient webClient;

    private final String INTERNAL_API_KEY="inventoryS3rvice";

    public InventoryService(InventoryRepository inventoryRepository, WebClient webClient) {
        this.inventoryRepository = inventoryRepository;
        this.webClient = webClient;
    }

    @Transactional
    public InventoryResponse create(InventoryRequest request){
        Boolean exists = webClient.get()
                .uri("/products/exists/" + request.getProductId())
                .header("X-API-KEY", INTERNAL_API_KEY)
                .header("X-Correlation-Id", MDC.get("correlationId"))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(Boolean.FALSE.equals(exists)){
            throw new RuntimeException("El producto no existe en el catalogo");
        }

        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .available(request.getAvailable())
                .reserved(request.getReserved())
                .build();
        return mapToResponse(inventoryRepository.save(inventory));
    }

    @Transactional(readOnly = true)
    public InventoryResponse findByProductId(String productId){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No hay inventario para el producto "+productId));
        return mapToResponse(inventory);
    }

    @Transactional
    public InventoryResponse purchase(String productId, Integer quantity){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No hay inventario para el producto "+productId));
        if(inventory.getAvailable() < quantity){
            throw new RuntimeException("Stock insuficiente. Disponible: "+inventory.getAvailable());
        }

        inventory.setAvailable(inventory.getAvailable() - quantity);

        Inventory savedInventory = inventoryRepository.save(inventory);

        publishInventoryChangedEvent(savedInventory, "PURCHASE");

        return mapToResponse(savedInventory);
    }

    @Transactional
    public InventoryResponse updateAvailableStock(String productId, Integer quantity){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("El producto no existe en el catalogo"));

        if(inventory.getAvailable() + quantity < 0){
            throw new RuntimeException("No hay stock para el producto en el catalogo");
        }

        inventory.setAvailable(inventory.getAvailable() - quantity);

        return mapToResponse(inventoryRepository.save(inventory));
    }

    @Transactional(readOnly = true)
    public Page<Inventory> findAll(Pageable pageable) {
        return  inventoryRepository.findAll(pageable);
    }



    private InventoryResponse mapToResponse(Inventory inventory){
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId().toString());
        response.setProductId(inventory.getProductId());
        response.setAvailable(inventory.getAvailable());
        response.setReserved(inventory.getReserved());
        return response;
    }

    private void publishInventoryChangedEvent(Inventory inventory, String action){
        MDC.put("event.type", "InventoryChanged");
        MDC.put("event.action", action);
        MDC.put("product.id", inventory.getProductId());
        MDC.put("stock.available", inventory.getAvailable().toString());
        MDC.put("stock.version", inventory.getVersion().toString());

        log.info("EVENT: InventoryChanged | Action: {} | Product: {} | New Stock: {}", action, inventory.getProductId(), inventory.getAvailable().toString());

        MDC.clear();
    }


}
