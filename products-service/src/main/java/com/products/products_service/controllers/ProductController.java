package com.products.products_service.controllers;

import com.products.products_service.entities.Product;
import com.products.products_service.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String sku,
        @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<Product> productsPage = productService.getProducts(name, sku, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("data", productsPage.getContent());

        Map<String, Object> meta  = new HashMap<>();
        meta.put("meta", Map.of(
        "totalItems", productsPage.getTotalElements(),
        "totalPages", productsPage.getTotalPages(),
        "currentPage", productsPage.getNumber(),
        "page-size", productsPage.getSize()
        ));
        response.put("meta", meta);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);

        Map<String, Object> response = new HashMap<>();
        response.put("data", savedProduct);

        response.put("links",Map.of(
                "self", "/api/products/" + savedProduct.getId()
        ));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        return ResponseEntity.ok(productService.exists(id));
    }
}
