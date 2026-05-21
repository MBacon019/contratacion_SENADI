package com.example.sistema_contratacion.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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
public class TipoContratoController {

    private final TipoContratoRepository tipoContratoRepository;

    public TipoContratoController(TipoContratoRepository tipoContratoRepository) {
        this.tipoContratoRepository = tipoContratoRepository;
    }

    // Listar todos los contratos (El que ya tenías)
    @GetMapping
    public List<TipoContrato> listarTodos() {
        return tipoContratoRepository.findAll();
    }

    // NUEVO: Agregar un paso a un contrato específico
    @PostMapping("/{id}/pasos")
    public ResponseEntity<?> agregarPaso(@PathVariable Long id, @RequestBody PasoContrato nuevoPaso) {
        Optional<TipoContrato> contratoOpt = tipoContratoRepository.findById(id);
        
        if (contratoOpt.isPresent()) {
            TipoContrato contrato = contratoOpt.get();
            
            // Asignamos la relación inversa
            nuevoPaso.setTipoContrato(contrato);
            
            // Calculamos automáticamente el orden del paso (el último + 1)
            int siguienteOrden = contrato.getPasos().size() + 1;
            nuevoPaso.setOrden(String.valueOf(siguienteOrden));
            
            // Lo añadimos a la lista y guardamos el contrato (gracias al CascadeType.ALL)
            contrato.getPasos().add(nuevoPaso);
            tipoContratoRepository.save(contrato);
            
            return ResponseEntity.ok(contrato);
        }
        
        return ResponseEntity.notFound().build();
    }
}