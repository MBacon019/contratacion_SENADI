package com.example.sistema_contratacion.entity;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    // Constructor vacío obligado por JPA
    public Role() {
    }

    public Role(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters explícitos para no depender de Lombok
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}