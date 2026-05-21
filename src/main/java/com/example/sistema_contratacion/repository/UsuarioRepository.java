package com.example.sistema_contratacion.repository;

import com.example.sistema_contratacion.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Spring Data generará la query automáticamente buscando por el email
    Optional<Usuario> findByEmail(String email);
}