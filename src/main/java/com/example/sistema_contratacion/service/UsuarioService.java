package com.example.sistema_contratacion.service;

import com.example.sistema_contratacion.entity.Role;
import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.repository.RoleRepository;
import com.example.sistema_contratacion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
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

    public Usuario registrar(String nombre, String email, String password) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe una cuenta registrada con ese correo");
        }

        Role rolUser = roleRepository.findByNombre("ROLE_USER")
            .orElseThrow(() -> new RuntimeException(
                "Rol ROLE_USER no encontrado. Verifica el DataInitializer."));

        Usuario nuevo = new Usuario();
        nuevo.setNombreCompleto(nombre);
        nuevo.setEmail(email);
        nuevo.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        nuevo.setActivo(true);
        
        // --- AQUÍ ESTÁ EL CAMBIO QUE SOLUCIONA EL ERROR ---
        nuevo.addRol(rolUser);

        return usuarioRepository.save(nuevo);
    }
}