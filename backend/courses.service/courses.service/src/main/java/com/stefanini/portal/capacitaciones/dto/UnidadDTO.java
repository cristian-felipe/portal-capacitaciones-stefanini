package com.stefanini.portal.capacitaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class UnidadDTO {
    
    private Integer id;
    
    private Integer programaId;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    private Integer orden;
    
    private List<LeccionDTO> lecciones = new ArrayList<>();
    
    // Constructores
    public UnidadDTO() {}
    
    public UnidadDTO(Integer programaId, String titulo, Integer orden) {
        this.programaId = programaId;
        this.titulo = titulo;
        this.orden = orden;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getProgramaId() {
        return programaId;
    }
    
    public void setProgramaId(Integer programaId) {
        this.programaId = programaId;
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
    
    public List<LeccionDTO> getLecciones() {
        return lecciones;
    }
    
    public void setLecciones(List<LeccionDTO> lecciones) {
        this.lecciones = lecciones;
    }
    
    @Override
    public String toString() {
        return "UnidadDTO{" +
                "id=" + id +
                ", programaId=" + programaId +
                ", titulo='" + titulo + '\'' +
                ", orden=" + orden +
                '}';
    }
}


