package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE " +
           "LOWER(i.codigoCompleto) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(i.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "ORDER BY i.codigoCompleto ASC")
    List<Item> buscarPorTermino(@Param("termino") String termino, Pageable pageable);
}
