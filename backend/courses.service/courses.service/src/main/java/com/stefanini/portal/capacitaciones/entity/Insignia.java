package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "insignias", schema = "capacitaciones")
public class Insignia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "url_imagen", columnDefinition = "TEXT")
    private String urlImagen;
    
    @OneToMany(mappedBy = "insignia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InsigniaOtorgada> insigniasOtorgadas;
    
    // Constructores
    public Insignia() {}
    
    public Insignia(String nombre, String descripcion, String urlImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }
    
    // Getters y Setters
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getUrlImagen() {
        return urlImagen;
    }
    
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
    
    public List<InsigniaOtorgada> getInsigniasOtorgadas() {
        return insigniasOtorgadas;
    }
    
    public void setInsigniasOtorgadas(List<InsigniaOtorgada> insigniasOtorgadas) {
        this.insigniasOtorgadas = insigniasOtorgadas;
    }
}

