package com.example.sistema_contratacion.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "pasos_contrato")
public class PasoContrato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NUEVO: Campo indispensable para mapear la fase (Preparatoria, Precontractual, Contractual, etc.)
    @Column(nullable = true, length = 100)
    private String fase;

    @Column(nullable = false, length = 500)
    private String nombrePaso; // Equivalente a la descripción del flujo

    @Column(nullable = false, length = 20)
    private String orden; // Soporta formatos jerárquicos de tu Excel como "1", "1.1", "2.1", etc.

    @Column(length = 150)
    private String area;

    @Column(length = 150)
    private String medio;

    @Column(length = 150)
    private String dirigidoA;

    @Column(length = 255)
    private String anexos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_contrato_id", nullable = false)
    @JsonIgnore // Corta el bucle JSON de manera limpia sin afectar la base de datos
    private TipoContrato tipoContrato;

    // Constructor Vacío Obligatorio para JPA
    public PasoContrato() {}

// 2. Constructor antiguo de 6 parámetros (Repara los errores en rojo de tu DataInitializer)
public PasoContrato(String nombrePaso, String orden, String area, String medio, String dirigidoA, String anexos) {
    this.fase = "FASE PREPARATORIA"; // Le asignamos una por defecto para tu flujo inicial
    this.nombrePaso = nombrePaso;
    this.orden = orden;
    this.area = area;
    this.medio = medio;
    this.dirigidoA = dirigidoA;
    this.anexos = anexos;
}

// 3. Constructor nuevo de 7 parámetros (Por si en el futuro introduces fases dinámicas)
public PasoContrato(String fase, String nombrePaso, String orden, String area, String medio, String dirigidoA, String anexos) {
    this.fase = fase;
    this.nombrePaso = nombrePaso;
    this.orden = orden;
    this.area = area;
    this.medio = medio;
    this.dirigidoA = dirigidoA;
    this.anexos = anexos;
}

    // =========================================================================
    // --- GETTERS Y SETTERS COMPLETOS ---
    // =========================================================================
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

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