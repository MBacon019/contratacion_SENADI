package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Cpc;
import com.example.sistema_contratacion.service.CpcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cpc")
public class CpcController {

    private final CpcService cpcService;

    public CpcController(CpcService cpcService) {
        this.cpcService = cpcService;
    }

    /**
     * POST /api/cpc/cargar
     * Sube el archivo umbral_vae.csv y recarga la tabla cpcs.
     */
    @PostMapping("/cargar")
    public ResponseEntity<?> cargarCSV(@RequestParam("archivo") MultipartFile archivo) {
        try {
            Map<String, Object> resultado = cpcService.cargarDesdeCSV(archivo);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al procesar el CSV: " + e.getMessage()));
        }
    }

    /**
     * GET /api/cpc/buscar?q=texto
     * Devuelve hasta 25 resultados para el autocomplete.
     */
    @GetMapping("/buscar")
    public List<Cpc> buscar(@RequestParam("q") String q) {
        return cpcService.buscar(q);
    }

    /**
     * GET /api/cpc/total
     * Devuelve cuántos registros hay cargados.
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", cpcService.contarRegistros()));
    }
}
