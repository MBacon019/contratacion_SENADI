package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Usuario;
import com.example.sistema_contratacion.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que tu frontend se conecte sin problemas de CORS
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Usuario> usuarioAutenticado = usuarioService.login(email, password);

        if (usuarioAutenticado.isPresent()) {
            // Si las credenciales son correctas, devolvemos los datos del usuario y su rol
            return ResponseEntity.ok(usuarioAutenticado.get());
        } else {
            // Si fallan, devolvemos un estado 401 (No autorizado) con un mensaje de error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas o usuario inactivo"));
        }
    }
}