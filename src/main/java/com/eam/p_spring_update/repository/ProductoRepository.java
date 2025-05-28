package com.eam.p_spring_update.repository;

import com.eam.p_spring_update.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Busca un producto por su código único
    Optional<Producto> findByCodigo(String codigo);

}
