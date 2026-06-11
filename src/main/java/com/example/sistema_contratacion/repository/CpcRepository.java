package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.Cpc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpcRepository extends JpaRepository<Cpc, Long> {

    @Query("SELECT c FROM Cpc c WHERE " +
           "LOWER(c.codigo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "ORDER BY c.codigo ASC")
    List<Cpc> buscarPorTermino(@Param("termino") String termino, Pageable pageable);
}
