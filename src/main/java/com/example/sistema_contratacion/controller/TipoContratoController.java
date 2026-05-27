package com.example.sistema_contratacion.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_contratacion.entity.PasoContrato;
import com.example.sistema_contratacion.entity.TipoContrato;
import com.example.sistema_contratacion.repository.TipoContratoRepository;

@RestController
@RequestMapping("/api/tipos-contrato")
@CrossOrigin(origins = "*") // Evita bloqueos de CORS si tu frontend corre en otro puerto (ej: Live Server)
public class TipoContratoController {

    private final TipoContratoRepository tipoContratoRepository;

    // Inyección por constructor (Excelente práctica)
    public TipoContratoController(TipoContratoRepository tipoContratoRepository) {
        this.tipoContratoRepository = tipoContratoRepository;
    }

    // Listar todos los contratos
    @GetMapping
    public List<TipoContrato> listarTodos() {
        return tipoContratoRepository.findAll();
    }

    // Agregar un nuevo paso personalizado a un contrato específico
    @PostMapping("/{id}/pasos")
    public ResponseEntity<?> agregarPaso(@PathVariable Long id, @RequestBody PasoContrato nuevoPaso) {
        Optional<TipoContrato> contratoOpt = tipoContratoRepository.findById(id);
        
        if (contratoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: El tipo de contrato con ID " + id + " no existe.");
        }
        
        TipoContrato contrato = contratoOpt.get();
        
        // 1. Asignamos la relación inversa (Padre del paso)
        nuevoPaso.setTipoContrato(contrato);
        
        // 2. Calculamos automáticamente el orden basándonos en los pasos actuales
        int siguienteOrden = contrato.getPasos().size() + 1;
        nuevoPaso.setOrden(String.valueOf(siguienteOrden));
        
        // 3. Añadimos el paso a la lista interna del contrato
        contrato.getPasos().add(nuevoPaso);
        
        // 4. Persistimos el cambio en cascada
        tipoContratoRepository.save(contrato);
        
        // 5. Retornamos HTTP 210/201 pasándole el nuevo paso creado
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaso);
    }
}