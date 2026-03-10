package com.api.productos.repository;

import com.api.productos.model.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {

    Flux<Producto> findByActivoTrue();

    Flux<Producto> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT * FROM productos WHERE LOWER(nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND activo = true")
    Flux<Producto> findByNombreContaining(String nombre);

    @Query("SELECT * FROM productos WHERE precio BETWEEN :min AND :max AND activo = true")
    Flux<Producto> findByPrecioRange(java.math.BigDecimal min, java.math.BigDecimal max);
}
