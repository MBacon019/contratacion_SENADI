package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Nos servirá para verificar si el rol ya existe antes de crearlo
    Optional<Role> findByNombre(String nombre);
}