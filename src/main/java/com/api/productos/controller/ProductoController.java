package com.api.productos.controller;

import com.api.productos.dto.ProductoDTO;
import com.api.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * GET /api/v1/productos
     * Lista todos los productos activos.
     * Soporta SSE (Server-Sent Events) si el cliente lo solicita.
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE})
    public Flux<ProductoDTO.Response> listarTodos() {
        return productoService.listarTodos();
    }

    /**
     * GET /api/v1/productos/{id}
     * Obtiene un producto por ID.
     */
    @GetMapping("/{id}")
    public Mono<ProductoDTO.Response> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    /**
     * GET /api/v1/productos/categoria/{categoria}
     * Filtra productos por categoría.
     */
    @GetMapping("/categoria/{categoria}")
    public Flux<ProductoDTO.Response> buscarPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    /**
     * GET /api/v1/productos/buscar?nombre=xyz
     * Busca productos por nombre (búsqueda parcial).
     */
    @GetMapping("/buscar")
    public Flux<ProductoDTO.Response> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    /**
     * GET /api/v1/productos/precio?min=10&max=100
     * Filtra por rango de precio.
     */
    @GetMapping("/precio")
    public Flux<ProductoDTO.Response> buscarPorPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return productoService.buscarPorRangoPrecio(min, max);
    }

    /**
     * POST /api/v1/productos
     * Crea un nuevo producto.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductoDTO.Response> crear(@Valid @RequestBody ProductoDTO.Request request) {
        return productoService.crear(request);
    }

    /**
     * PUT /api/v1/productos/{id}
     * Actualiza un producto existente.
     */
    @PutMapping("/{id}")
    public Mono<ProductoDTO.Response> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO.Request request) {
        return productoService.actualizar(id, request);
    }

    /**
     * DELETE /api/v1/productos/{id}
     * Realiza un soft delete (activo = false).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return productoService.eliminar(id);
    }
}
