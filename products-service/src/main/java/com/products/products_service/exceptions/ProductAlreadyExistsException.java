package com.products.products_service.exceptions;

public class ProductAlreadyExistsException  extends RuntimeException {

    public String sku;

    public ProductAlreadyExistsException(String sku) {
        super("Ya axiste un producto con el SKU"+ sku);
        this.sku = sku;
    }

}
