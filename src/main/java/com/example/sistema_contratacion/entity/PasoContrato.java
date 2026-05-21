package com.example.sistema_contratacion.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pasos_contrato")
public class PasoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String nombrePaso; // Equivalente a FLUJO en tu Excel

    @Column(nullable = false, length = 20)
    private String orden; // Cambiado a String para soportar "2.1", "2.2", "4.1", etc.

    @Column(length = 150)
    private String area;

    @Column(length = 150)
    private String medio;

    @Column(length = 150)
    private String dirigidoA;

    @Column(length = 255)
    private String anexos;

    @ManyToOne
    @JoinColumn(name = "tipo_contrato_id", nullable = false)
    @JsonIgnore
    private TipoContrato tipoContrato;

    public PasoContrato() {}

    // Constructor completo para facilitar la inserción de datos del Excel
    public PasoContrato(String nombrePaso, String orden, String area, String medio, String dirigidoA, String anexos) {
        this.nombrePaso = nombrePaso;
        this.orden = orden;
        this.area = area;
        this.medio = medio;
        this.dirigidoA = dirigidoA;
        this.anexos = anexos;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombrePaso() { return nombrePaso; }
    public void setNombrePaso(String nombrePaso) { this.nombrePaso = nombrePaso; }

    public String getOrden() { return orden; }
    public void setOrden(String orden) { this.orden = orden; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getMedio() { return medio; }
    public void setMedio(String medio) { this.medio = medio; }

    public String getDirigidoA() { return dirigidoA; }
    public void setDirigidoA(String dirigidoA) { this.dirigidoA = dirigidoA; }

    public String getAnexos() { return anexos; }
    public void setAnexos(String anexos) { this.anexos = anexos; }

    public TipoContrato getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(TipoContrato tipoContrato) { this.tipoContrato = tipoContrato; }
}