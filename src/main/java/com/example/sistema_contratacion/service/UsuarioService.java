package com.example.sistema_contratacion.service;

import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lógica de autenticación segura utilizando BCrypt.
     * Devuelve el usuario si las credenciales son correctas y está activo, o vacío si fallan.
     */
    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // 1. Verificamos que el usuario esté activo
            // 2. Comparamos la contraseña en texto plano con el hash seguro de la base de datos
            if (usuario.isActivo() && BCrypt.checkpw(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }
}