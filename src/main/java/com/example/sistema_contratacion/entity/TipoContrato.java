package com.example.sistema_contratacion.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipos_contrato")
public class TipoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // 1. Agregamos @JsonManagedReference para evitar recursividad infinita al transformar a JSON
    @OneToMany(mappedBy = "tipoContrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PasoContrato> pasos = new ArrayList<>();

    public TipoContrato() {}

    // =========================================================================
    // METODO HELPER RECOMENDADO: Sincroniza la relación bidireccional en memoria
    // =========================================================================
    public void addPaso(PasoContrato paso) {
        this.pasos.add(paso);
        paso.setTipoContrato(this); // Sincroniza el lado del hijo automáticamente
    }

    public void removePaso(PasoContrato paso) {
        this.pasos.remove(paso);
        paso.setTipoContrato(null);
    }

    // =========================================================================
    // Getters y Setters
    // =========================================================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<PasoContrato> getPasos() { return pasos; }
    public void setPasos(List<PasoContrato> pasos) { this.pasos = pasos; }
}