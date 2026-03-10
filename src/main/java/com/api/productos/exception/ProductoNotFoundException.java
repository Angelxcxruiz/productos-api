package com.api.productos.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("Producto con id " + id + " no encontrado");
    }
}
