package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Role;
import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.service.UsuarioService;
import com.example.sistema_contratacion.repository.UsuarioRepository;
import com.example.sistema_contratacion.repository.RoleRepository;
import com.example.sistema_contratacion.util.LDAP; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    // --- LOGIN NORMAL (AQUÍ ESTABA EL ERROR DE FORMATO) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        Optional<Usuario> auth = usuarioService.login(credentials.get("email"), credentials.get("password"));
        if (auth.isPresent()) {
            Usuario usuario = auth.get();
            
            // Extraemos los múltiples roles a una lista de textos simple para el HTML
            List<String> nombresRoles = usuario.getRoles().stream()
                    .map(Role::getNombre)
                    .collect(Collectors.toList());
                    
            // Parche de seguridad: si la tabla nueva de roles está vacía, asignamos ROLE_USER
            if(nombresRoles.isEmpty()){
                nombresRoles.add("ROLE_USER");
            }

            return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "email", usuario.getEmail(),
                "nombre", usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "Usuario",
                "roles", nombresRoles, // Enviamos el arreglo limpio
                "estaLogeado", true
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
    }

    // --- LOGIN LDAP ---
    @PostMapping("/login-ldap")
    public ResponseEntity<?> loginLdap(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login"); 
        String clave = credentials.get("clave"); 
        
        try {
            LDAP ldap = new LDAP();
            boolean credencialesCorrectas = ldap.validarIngresoLDAPSinrestrinccion(login, clave);
            
            if (credencialesCorrectas) {
                String emailInstitucional = login + "@senadi.gob.ec"; 
                Optional<Usuario> usuarioBd = usuarioRepository.findByEmail(emailInstitucional);
                Usuario usuarioFinal;
                
                if (usuarioBd.isPresent()) {
                    usuarioFinal = usuarioBd.get();
                } else {
                    usuarioFinal = new Usuario();
                    usuarioFinal.setEmail(emailInstitucional);
                    usuarioFinal.setNombreCompleto(login);
                    usuarioFinal.setPassword(""); 
                    usuarioFinal.setActivo(true);
                    
                    Optional<Role> rolBasico = roleRepository.findByNombre("ROLE_UNIDAD_REQUIRENTE");
                    rolBasico.ifPresent(usuarioFinal::addRol);
                    
                    usuarioFinal = usuarioRepository.save(usuarioFinal);
                }
                
                List<String> nombresRoles = usuarioFinal.getRoles().stream()
                        .map(Role::getNombre)
                        .collect(Collectors.toList());
                        
                if(nombresRoles.isEmpty()){
                    nombresRoles.add("ROLE_SUPER_ADMIN");
                }
                
                return ResponseEntity.ok(Map.of(
                    "estaLogeado", true,
                    "mensaje", "Bienvenido " + login,
                    "roles", nombresRoles,
                    "nombre", login,
                    "email", usuarioFinal.getEmail()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error LDAP: " + e.getMessage()));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        try {
            Usuario nuevo = usuarioService.registrar(body.get("nombre"), body.get("email"), body.get("password"));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}