package com.example.sistema_contratacion.entity;

import javax.persistence.*;

@Entity
@Table(name = "items", indexes = {
    @Index(name = "idx_item_codigo_completo", columnList = "codigo_completo"),
    @Index(name = "idx_item_nombre",          columnList = "nombre")
})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_completo", length = 50)
    private String codigoCompleto;

    @Column(name = "naturaleza", length = 200)
    private String naturaleza;

    @Column(name = "grupo", length = 200)
    private String grupo;

    @Column(name = "subgrupo", length = 200)
    private String subgrupo;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "nombre", length = 500)
    private String nombre;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    public Item() {}

    public Item(Long id, String codigoCompleto, String naturaleza, String grupo,
                String subgrupo, String codigo, String nombre, String descripcion) {
        this.id = id;
        this.codigoCompleto = codigoCompleto;
        this.naturaleza = naturaleza;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoCompleto() { return codigoCompleto; }
    public void setCodigoCompleto(String codigoCompleto) { this.codigoCompleto = codigoCompleto; }

    public String getNaturaleza() { return naturaleza; }
    public void setNaturaleza(String naturaleza) { this.naturaleza = naturaleza; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public String getSubgrupo() { return subgrupo; }
    public void setSubgrupo(String subgrupo) { this.subgrupo = subgrupo; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
