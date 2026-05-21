package com.example.sistema_contratacion.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pasos_contrato")
public class PasoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombrePaso;

    @Column(nullable = false)
    private Integer orden; // Para saber qué paso va primero (1, 2, 3...)

    @ManyToOne
    @JoinColumn(name = "tipo_contrato_id", nullable = false)
    @JsonIgnore // Evita bucles infinitos al transformar a JSON
    private TipoContrato tipoContrato;

    public PasoContrato() {}

    public PasoContrato(String nombrePaso, Integer orden, TipoContrato tipoContrato) {
        this.nombrePaso = nombrePaso;
        this.orden = orden;
        this.tipoContrato = tipoContrato;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombrePaso() { return nombrePaso; }
    public void setNombrePaso(String nombrePaso) { this.nombrePaso = nombrePaso; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public TipoContrato getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(TipoContrato tipoContrato) { this.tipoContrato = tipoContrato; }
}