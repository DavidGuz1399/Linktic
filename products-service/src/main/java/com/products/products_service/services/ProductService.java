package com.products.products_service.services;

import com.products.products_service.entities.Product;
import com.products.products_service.exceptions.ProductAlreadyExistsException;
import com.products.products_service.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Aqui hago el constructor manual y en el controlador por RequiredArgsConstructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new ProductAlreadyExistsException(product.getSku());
        }
        return productRepository.save(product);
    }

    public Page<Product> getProducts(String name, String sku,Pageable pageable) {
        if(name != null){
            return  productRepository.findByNameContainingIgnoreCase(name,pageable);
        }else if(sku != null){
            return  productRepository.findBySku(sku,pageable);
        }

        return productRepository.findAll(pageable);
    }

    public boolean exists(String id) {
        try{
            UUID uuid = UUID.fromString(id);
            return productRepository.existsById(uuid);
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
