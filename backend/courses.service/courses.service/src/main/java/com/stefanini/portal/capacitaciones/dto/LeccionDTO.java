package com.stefanini.portal.capacitaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LeccionDTO {
    
    private Integer id;
    
    private Integer unidadId;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    private Integer orden;
    
    @Size(max = 50, message = "El tipo de material no puede exceder 50 caracteres")
    private String tipoMaterial; // video, pdf, link
    
    private String urlMaterial;
    
    // Constructores
    public LeccionDTO() {}
    
    public LeccionDTO(Integer unidadId, String titulo, Integer orden, String tipoMaterial, String urlMaterial) {
        this.unidadId = unidadId;
        this.titulo = titulo;
        this.orden = orden;
        this.tipoMaterial = tipoMaterial;
        this.urlMaterial = urlMaterial;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUnidadId() {
        return unidadId;
    }
    
    public void setUnidadId(Integer unidadId) {
        this.unidadId = unidadId;
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
    
    @Override
    public String toString() {
        return "LeccionDTO{" +
                "id=" + id +
                ", unidadId=" + unidadId +
                ", titulo='" + titulo + '\'' +
                ", orden=" + orden +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", urlMaterial='" + urlMaterial + '\'' +
                '}';
    }
}


