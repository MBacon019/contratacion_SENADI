package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.ConfiguracionUmbral;
import com.example.sistema_contratacion.service.ConfiguracionUmbralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/umbrales")
@CrossOrigin(origins = "*")
public class ConfiguracionUmbralController {

    private final ConfiguracionUmbralService service;

    public ConfiguracionUmbralController(ConfiguracionUmbralService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConfiguracionUmbral> listar() {
        return service.listarTodos();
    }

    @GetMapping("/activo")
    public ResponseEntity<?> obtenerActivo() {
        return service.obtenerActivo()
                .map(u -> ResponseEntity.ok((Object) u))
                .orElse(ResponseEntity.ok(Map.of("montoUmbral", 10000.0, "anio", 2026, "activo", false)));
    }

    @PostMapping
    public ResponseEntity<ConfiguracionUmbral> crear(@RequestBody ConfiguracionUmbral umbral) {
        return ResponseEntity.status(201).body(service.crear(umbral));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ConfiguracionUmbral datos) {
        return service.actualizar(id, datos)
                .map(u -> ResponseEntity.ok((Object) u))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        return service.activar(id)
                .map(u -> ResponseEntity.ok((Object) u))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return service.eliminar(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
