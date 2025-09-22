package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "lecciones", schema = "capacitaciones")
public class Leccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La unidad es requerida")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id", nullable = false)
    private Unidad unidad;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;
    
    @Column(name = "orden")
    private Integer orden;
    
    @Size(max = 50, message = "El tipo de material no puede exceder 50 caracteres")
    @Column(name = "tipo_material", length = 50)
    private String tipoMaterial; // video, pdf, link
    
    @Column(name = "url_material", columnDefinition = "TEXT")
    private String urlMaterial; // Para links externos
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "material_id")
    private Material material; // Para archivos subidos
    
    // Constructores
    public Leccion() {}
    
    public Leccion(Unidad unidad, String titulo, Integer orden, String tipoMaterial, String urlMaterial) {
        this.unidad = unidad;
        this.titulo = titulo;
        this.orden = orden;
        this.tipoMaterial = tipoMaterial;
        this.urlMaterial = urlMaterial;
    }
    
    // Métodos de conveniencia
    public boolean esVideo() {
        return "video".equalsIgnoreCase(tipoMaterial);
    }
    
    public boolean esPdf() {
        return "pdf".equalsIgnoreCase(tipoMaterial);
    }
    
    public boolean esLink() {
        return "link".equalsIgnoreCase(tipoMaterial);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Unidad getUnidad() {
        return unidad;
    }
    
    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
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
    
    public String getTipoMaterial() {
        return tipoMaterial;
    }
    
    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
    
    public String getUrlMaterial() {
        return urlMaterial;
    }
    
    public void setUrlMaterial(String urlMaterial) {
        this.urlMaterial = urlMaterial;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    // Métodos de conveniencia adicionales
    public boolean tieneMaterialSubido() {
        return material != null;
    }
    
    public boolean esArchivoSubido() {
        return material != null && material.getActivo();
    }
    
    public String getUrlAcceso() {
        if (material != null && material.getActivo()) {
            return material.getUrlAcceso();
        }
        return urlMaterial; // Fallback a URL externa
    }
    
    @Override
    public String toString() {
        return "Leccion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", orden=" + orden +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", urlMaterial='" + urlMaterial + '\'' +
                '}';
    }
}


