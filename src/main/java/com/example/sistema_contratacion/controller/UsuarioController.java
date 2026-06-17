package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Role;
import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.repository.RoleRepository;
import com.example.sistema_contratacion.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    // GET /api/usuarios — lista todos los usuarios (solo para ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // PUT /api/usuarios/{id}/rol — cambia el rol de un usuario
    @PutMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id,
                                        @RequestBody Map<String, String> body) {
        String rolNombre = body.get("rolNombre");
        if (rolNombre == null || rolNombre.isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El campo rolNombre es obligatorio"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Role> rolOpt = roleRepository.findByNombre(rolNombre);
        if (rolOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Rol no encontrado: " + rolNombre));
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setRol(rolOpt.get());
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // PUT /api/usuarios/{id}/estado — activa o desactiva un usuario
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestBody Map<String, Boolean> body) {
        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El campo activo es obligatorio"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(activo);
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // DELETE /api/usuarios/{id} — elimina un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
    }
}
