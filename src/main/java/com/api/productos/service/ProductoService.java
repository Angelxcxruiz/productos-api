package com.api.productos.service;

import com.api.productos.dto.ProductoDTO;
import com.api.productos.exception.ProductoNotFoundException;
import com.api.productos.model.Producto;
import com.api.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // ── Listar todos activos ──────────────────────────────────────────────────
    public Flux<ProductoDTO.Response> listarTodos() {
        log.debug("Listando todos los productos activos");
        return productoRepository.findByActivoTrue()
                .map(this::toResponse);
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────────
    public Mono<ProductoDTO.Response> buscarPorId(Long id) {
        log.debug("Buscando producto con id: {}", id);
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .map(this::toResponse);
    }

    // ── Buscar por categoría ──────────────────────────────────────────────────
    public Flux<ProductoDTO.Response> buscarPorCategoria(String categoria) {
        log.debug("Buscando productos de categoría: {}", categoria);
        return productoRepository.findByCategoriaIgnoreCase(categoria)
                .map(this::toResponse);
    }

    // ── Buscar por nombre ─────────────────────────────────────────────────────
    public Flux<ProductoDTO.Response> buscarPorNombre(String nombre) {
        log.debug("Buscando productos con nombre: {}", nombre);
        return productoRepository.findByNombreContaining(nombre)
                .map(this::toResponse);
    }

    // ── Buscar por rango de precio ────────────────────────────────────────────
    public Flux<ProductoDTO.Response> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        log.debug("Buscando productos con precio entre {} y {}", min, max);
        return productoRepository.findByPrecioRange(min, max)
                .map(this::toResponse);
    }

    // ── Crear ─────────────────────────────────────────────────────────────────
    public Mono<ProductoDTO.Response> crear(ProductoDTO.Request request) {
        log.debug("Creando producto: {}", request.getNombre());
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .categoria(request.getCategoria())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return productoRepository.save(producto)
                .map(this::toResponse);
    }

    // ── Actualizar ────────────────────────────────────────────────────────────
    public Mono<ProductoDTO.Response> actualizar(Long id, ProductoDTO.Request request) {
        log.debug("Actualizando producto con id: {}", id);
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .flatMap(existing -> {
                    existing.setNombre(request.getNombre());
                    existing.setDescripcion(request.getDescripcion());
                    existing.setPrecio(request.getPrecio());
                    existing.setStock(request.getStock());
                    existing.setCategoria(request.getCategoria());
                    if (request.getActivo() != null) existing.setActivo(request.getActivo());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return productoRepository.save(existing);
                })
                .map(this::toResponse);
    }

    // ── Eliminar (soft delete) ────────────────────────────────────────────────
    public Mono<Void> eliminar(Long id) {
        log.debug("Eliminando producto con id: {}", id);
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .flatMap(producto -> {
                    producto.setActivo(false);
                    producto.setUpdatedAt(LocalDateTime.now());
                    return productoRepository.save(producto);
                })
                .then();
    }

    // ── Mapper ────────────────────────────────────────────────────────────────
    private ProductoDTO.Response toResponse(Producto p) {
        return ProductoDTO.Response.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .categoria(p.getCategoria())
                .activo(p.getActivo())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
