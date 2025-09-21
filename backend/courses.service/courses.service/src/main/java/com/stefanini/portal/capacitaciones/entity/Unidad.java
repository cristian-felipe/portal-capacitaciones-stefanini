package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidades", schema = "capacitaciones")
public class Unidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El programa es requerido")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;
    
    @Column(name = "orden")
    private Integer orden;
    
    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Leccion> lecciones = new ArrayList<>();
    
    // Constructores
    public Unidad() {}
    
    public Unidad(Programa programa, String titulo, Integer orden) {
        this.programa = programa;
        this.titulo = titulo;
        this.orden = orden;
    }
    
    // Métodos de conveniencia
    public void agregarLeccion(Leccion leccion) {
        lecciones.add(leccion);
        leccion.setUnidad(this);
    }
    
    public void removerLeccion(Leccion leccion) {
        lecciones.remove(leccion);
        leccion.setUnidad(null);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Programa getPrograma() {
        return programa;
    }
    
    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public Integer getOrden() {
        return orden;
    }
    
    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    
    public List<Leccion> getLecciones() {
        return lecciones;
    }
    
    public void setLecciones(List<Leccion> lecciones) {
        this.lecciones = lecciones;
    }
    
    @Override
    public String toString() {
        return "Unidad{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", orden=" + orden +
                '}';
    }
}


