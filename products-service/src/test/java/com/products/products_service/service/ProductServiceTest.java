package com.products.products_service.service;

import com.products.products_service.entities.Product;
import com.products.products_service.repositories.ProductRepository;
import com.products.products_service.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldSaveProductSuccessfully() {
        Product productInput = new Product();
        productInput.setName("Laptop");
        productInput.setSku("SKU123");
        productInput.setPrice(new BigDecimal("1000.00"));
        productInput.setStatus(Product.Status.ACTIVE);


        UUID fakeId = UUID.randomUUID();
        Product savedProduct = new Product(
                fakeId,
                "SKU123",
                "Laptop",
                new BigDecimal("1000.00"),
                Product.Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = service.saveProduct(productInput);

        assertNotNull(result.getId());
        assertEquals(fakeId, result.getId());
        assertEquals("SKU123", result.getSku());
    }
}
