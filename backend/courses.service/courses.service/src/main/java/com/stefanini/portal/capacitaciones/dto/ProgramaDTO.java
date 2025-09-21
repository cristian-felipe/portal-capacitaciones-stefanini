package com.stefanini.portal.capacitaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDTO {
    
    private Integer id;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    private String descripcion;
    
    @Size(max = 100, message = "El área de conocimiento no puede exceder 100 caracteres")
    private String areaConocimiento;
    
    private LocalDateTime fechaCreacion;
    
    private List<UnidadDTO> unidades = new ArrayList<>();
    
    // Constructores
    public ProgramaDTO() {}
    
    public ProgramaDTO(String titulo, String descripcion, String areaConocimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.areaConocimiento = areaConocimiento;
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
    
    public List<UnidadDTO> getUnidades() {
        return unidades;
    }
    
    public void setUnidades(List<UnidadDTO> unidades) {
        this.unidades = unidades;
    }
    
    @Override
    public String toString() {
        return "ProgramaDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", areaConocimiento='" + areaConocimiento + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}


