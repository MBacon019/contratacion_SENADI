package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Role;
import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.repository.RoleRepository;
import com.example.sistema_contratacion.repository.UsuarioRepository;
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

    // 1. LISTAR USUARIOS (Carga la tabla en el HTML)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarUsuarios() {
        List<Map<String, Object>> usuarios = usuarioRepository.findAll().stream().map(usuario -> {
            
            // Extraemos todos los roles del usuario a una lista de textos
            List<String> nombresRoles = usuario.getRoles().stream()
                    .map(Role::getNombre)
                    .collect(Collectors.toList());

            return Map.of(
                    "id", usuario.getId(),
                    "nombreCompleto", usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "Sin Nombre",
                    "email", usuario.getEmail(),
                    "activo", usuario.isActivo(), // Necesario para el botón Activar/Desactivar
                    "roles", nombresRoles // Enviamos el arreglo plural "roles"
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    // 2. ACTUALIZAR MÚLTIPLES ROLES (Ruta plural: /roles)
    @PutMapping("/{id}/roles")
    public ResponseEntity<?> actualizarRoles(@PathVariable Long id, @RequestBody Map<String, List<String>> body) {
        List<String> nombresRoles = body.get("roles");

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();
        Set<Role> nuevosRoles = new HashSet<>();

        // Buscamos cada rol seleccionado en la base de datos y lo asignamos
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

    // 3. ACTIVAR / DESACTIVAR USUARIO
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(body.get("activo"));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
    }

    // 4. ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
    }


    // --- 5. VERIFICAR USUARIO EN LDAP Y AUTOCOMPLETAR CORREO ---
    @GetMapping("/ldap/verificar/{username}")
    public ResponseEntity<?> verificarUsuarioLdap(@PathVariable String username) {
        try {
            // Aquí puedes conectar tu clase LDAP real en el futuro si tienes un método de búsqueda.
            // Por ahora, validamos que el formato sea correcto y armamos el correo "sea cual sea".
            
            if (username == null || username.trim().length() < 3) {
                return ResponseEntity.status(404).body(Map.of("error", "El nombre de usuario es demasiado corto o inválido."));
            }

            // Limpiamos espacios y armamos el correo automáticamente
            String nombreLimpio = username.trim().toLowerCase();
            String correoAutogenerado = nombreLimpio + "@senadi.gob.ec";
            
            return ResponseEntity.ok(Map.of(
                "existe", true,
                "mensaje", "Usuario válido en LDAP",
                "email", correoAutogenerado
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error de conexión con LDAP"));
        }
    }
}