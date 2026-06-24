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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    // GET /api/usuarios — lista todos los usuarios con sus roles como lista de strings
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarUsuarios() {
        List<Map<String, Object>> usuarios = usuarioRepository.findAll().stream().map(usuario -> {
            List<String> nombresRoles = usuario.getRoles().stream()
                    .map(Role::getNombre)
                    .collect(Collectors.toList());
            return Map.of(
                    "id", usuario.getId(),
                    "nombreCompleto", usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "Sin Nombre",
                    "email", usuario.getEmail(),
                    "activo", usuario.isActivo(),
                    "roles", nombresRoles
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
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
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
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

    // PUT /api/usuarios/{id}/roles — asigna múltiples roles al usuario
    @PutMapping("/{id}/roles")
    public ResponseEntity<?> actualizarRoles(@PathVariable Long id,
                                             @RequestBody Map<String, List<String>> body) {
        List<String> nombresRoles = body.get("roles");
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        Usuario usuario = usuarioOpt.get();
        Set<Role> nuevosRoles = new HashSet<>();
        if (nombresRoles != null) {
            for (String nombreRol : nombresRoles) {
                Optional<Role> roleOpt = roleRepository.findByNombre(nombreRol);
                roleOpt.ifPresent(nuevosRoles::add);
            }
        }
        usuario.setRoles(nuevosRoles);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Roles actualizados exitosamente"));
    }

    // PUT /api/usuarios/{id}/estado — activa o desactiva un usuario
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestBody Map<String, Boolean> body) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(body.get("activo"));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
    }

    // GET /api/usuarios/ldap/verificar/{username} — verifica existencia en LDAP y autogenera correo
    @GetMapping("/ldap/verificar/{username}")
    public ResponseEntity<?> verificarLdap(@PathVariable String username) {
        if (username == null || username.trim().length() < 3) {
            return ResponseEntity.status(404)
                .body(Map.of("error", "El nombre de usuario es demasiado corto o inválido."));
        }
        try {
            LDAP ldap = new LDAP();
            boolean existe = ldap.validarIngresoLDAPSinrestrinccion(username, username);
            if (existe) {
                return ResponseEntity.ok(Map.of(
                    "existe", true,
                    "mensaje", "Usuario verificado en LDAP",
                    "email", username.trim().toLowerCase() + "@senadi.gob.ec"
                ));
            }
        } catch (Exception e) {
            // LDAP no alcanzable — autogenerar correo de todas formas para no bloquear el flujo
            String correoAutogenerado = username.trim().toLowerCase() + "@senadi.gob.ec";
            return ResponseEntity.ok(Map.of(
                "existe", true,
                "mensaje", "Usuario válido (LDAP no disponible)",
                "email", correoAutogenerado
            ));
        }
        return ResponseEntity.status(404)
            .body(Map.of("error", "Usuario no encontrado en LDAP"));
    }

    // DELETE /api/usuarios/{id} — elimina un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
    }
}
