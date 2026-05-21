package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.TipoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoContratoRepository extends JpaRepository<TipoContrato, Long> {
    Optional<TipoContrato> findByNombre(String nombre);
}