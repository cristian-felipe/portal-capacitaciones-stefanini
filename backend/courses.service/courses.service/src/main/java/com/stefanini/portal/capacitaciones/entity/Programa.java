package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programas", schema = "capacitaciones")
public class Programa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Size(max = 100, message = "El área de conocimiento no puede exceder 100 caracteres")
    @Column(name = "area_conocimiento", length = 100)
    private String areaConocimiento;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Unidad> unidades = new ArrayList<>();
    
    // Constructores
    public Programa() {}
    
    public Programa(String titulo, String descripcion, String areaConocimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.areaConocimiento = areaConocimiento;
    }
    
    // Métodos de conveniencia
    public void agregarUnidad(Unidad unidad) {
        unidades.add(unidad);
        unidad.setPrograma(this);
    }
    
    public void removerUnidad(Unidad unidad) {
        unidades.remove(unidad);
        unidad.setPrograma(null);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getAreaConocimiento() {
        return areaConocimiento;
    }
    
    public void setAreaConocimiento(String areaConocimiento) {
        this.areaConocimiento = areaConocimiento;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<Unidad> getUnidades() {
        return unidades;
    }
    
    public void setUnidades(List<Unidad> unidades) {
        this.unidades = unidades;
    }
    
    @Override
    public String toString() {
        return "Programa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", areaConocimiento='" + areaConocimiento + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}


