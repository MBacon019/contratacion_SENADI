package com.example.sistema_contratacion.entity;

import javax.persistence.*;

@Entity
@Table(name = "configuracion_umbrales")
public class ConfiguracionUmbral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private Double montoUmbral;

    @Column
    private Double montoSubastaMinimo;

    @Column(nullable = false)
    private boolean activo = false;

    @Column(length = 255)
    private String descripcion;

    public ConfiguracionUmbral() {}

    public ConfiguracionUmbral(Integer anio, Double montoUmbral, boolean activo, String descripcion) {
        this.anio = anio;
        this.montoUmbral = montoUmbral;
        this.montoSubastaMinimo = montoUmbral;
        this.activo = activo;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public Double getMontoUmbral() { return montoUmbral; }
    public void setMontoUmbral(Double montoUmbral) { this.montoUmbral = montoUmbral; }
    public Double getMontoSubastaMinimo() { return montoSubastaMinimo != null ? montoSubastaMinimo : montoUmbral; }
    public void setMontoSubastaMinimo(Double montoSubastaMinimo) { this.montoSubastaMinimo = montoSubastaMinimo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
