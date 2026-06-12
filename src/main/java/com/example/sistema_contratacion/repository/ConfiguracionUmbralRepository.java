package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.ConfiguracionUmbral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracionUmbralRepository extends JpaRepository<ConfiguracionUmbral, Long> {
    Optional<ConfiguracionUmbral> findByActivoTrue();
    Optional<ConfiguracionUmbral> findByAnio(Integer anio);
}
