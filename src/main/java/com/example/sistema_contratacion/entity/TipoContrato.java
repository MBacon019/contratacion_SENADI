package com.example.sistema_contratacion.entity;

import javax.persistence.*;
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

    // Relación uno a muchos: Un tipo de contrato tiene muchos pasos ordenados
    @OneToMany(mappedBy = "tipoContrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PasoContrato> pasos = new ArrayList<>();

    public TipoContrato() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<PasoContrato> getPasos() { return pasos; }
    public void setPasos(List<PasoContrato> pasos) { this.pasos = pasos; }
}