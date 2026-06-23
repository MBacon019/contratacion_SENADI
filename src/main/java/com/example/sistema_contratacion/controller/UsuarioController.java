package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Role;
import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.repository.RoleRepository;
import com.example.sistema_contratacion.repository.UsuarioRepository;
import com.example.sistema_contratacion.util.LDAP;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    // GET /api/usuarios — lista todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // PUT /api/usuarios/{id}/rol — reemplaza todos los roles por uno solo (compatibilidad)
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
        Set<Role> nuevoSet = new HashSet<>();
        nuevoSet.add(rolOpt.get());
        usuario.setRoles(nuevoSet);
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

    // PUT /api/usuarios/{id}/roles — asigna múltiples roles al usuario
    @PutMapping("/{id}/roles")
    public ResponseEntity<?> actualizarRoles(@PathVariable Long id,
                                             @RequestBody Map<String, List<String>> body) {
        List<String> rolesNombres = body.get("roles");
        if (rolesNombres == null || rolesNombres.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La lista de roles no puede estar vacía"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<Role> roleSet = new HashSet<>();
        for (String nombreRol : rolesNombres) {
            Optional<Role> rolOpt = roleRepository.findByNombre(nombreRol);
            if (rolOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Rol no encontrado: " + nombreRol));
            }
            roleSet.add(rolOpt.get());
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setRoles(roleSet);
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // GET /api/usuarios/ldap/verificar/{username} — verifica existencia en LDAP
    @GetMapping("/ldap/verificar/{username}")
    public ResponseEntity<?> verificarLdap(@PathVariable String username) {
        try {
            LDAP ldap = new LDAP();
            boolean existe = ldap.validarIngresoLDAPSinrestrinccion(username, username);
            if (existe) {
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Usuario verificado",
                    "email", username + "@senadi.gob.ec"
                ));
            }
        } catch (Exception e) {
            // LDAP no alcanzable o error de conexión
        }
        return ResponseEntity.status(404)
            .body(Map.of("error", "Usuario no encontrado en LDAP"));
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
