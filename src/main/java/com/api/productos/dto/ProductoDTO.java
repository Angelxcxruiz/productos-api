package com.api.productos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        private String nombre;

        private String descripcion;

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
        private BigDecimal precio;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;

        @Size(max = 100, message = "La categoría no puede superar 100 caracteres")
        private String categoria;

        private Boolean activo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String categoria;
        private Boolean activo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
