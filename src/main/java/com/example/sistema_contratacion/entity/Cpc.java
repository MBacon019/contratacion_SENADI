package com.example.sistema_contratacion.entity;

import javax.persistence.*;

@Entity
@Table(name = "cpcs", indexes = {
    @Index(name = "idx_cpc_codigo",       columnList = "codigo"),
    @Index(name = "idx_cpc_descripcion",  columnList = "descripcion")
})
public class Cpc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "umbral_vae")
    private Double umbralVae;

    public Cpc() {}

    public Cpc(Long id, String codigo, String descripcion, Double umbralVae) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.umbralVae = umbralVae;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getUmbralVae() { return umbralVae; }
    public void setUmbralVae(Double umbralVae) { this.umbralVae = umbralVae; }
}
