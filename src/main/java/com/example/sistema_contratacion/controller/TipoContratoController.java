package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.TipoContrato;
import com.example.sistema_contratacion.repository.TipoContratoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-contrato")
public class TipoContratoController {

    private final TipoContratoRepository tipoContratoRepository;

    public TipoContratoController(TipoContratoRepository tipoContratoRepository) {
        this.tipoContratoRepository = tipoContratoRepository;
    }

    @GetMapping
    public List<TipoContrato> listarTodos() {
        return tipoContratoRepository.findAll();
    }
}