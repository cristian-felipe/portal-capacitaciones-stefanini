package com.stefanini.portal.capacitaciones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgramaCompletoDTO {
    
    private Integer id;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    private String descripcion;
    
    @Size(max = 100, message = "El área de conocimiento no puede exceder 100 caracteres")
    private String areaConocimiento;
    
    private LocalDateTime fechaCreacion;
    
    @Valid
    private List<UnidadCompletaDTO> unidades = new ArrayList<>();
    
    // Constructores
    public ProgramaCompletoDTO() {}
    
    public ProgramaCompletoDTO(String titulo, String descripcion, String areaConocimiento) {
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
    
    public List<UnidadCompletaDTO> getUnidades() {
        return unidades;
    }
    
    public void setUnidades(List<UnidadCompletaDTO> unidades) {
        this.unidades = unidades;
    }
    
    @Override
    public String toString() {
        return "ProgramaCompletoDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", areaConocimiento='" + areaConocimiento + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", unidades=" + unidades.size() + " unidades" +
                '}';
    }
    
    // Clase interna para Unidad Completa
    public static class UnidadCompletaDTO {
        
        private Integer id;
        
        @NotBlank(message = "El título de la unidad es requerido")
        @Size(max = 255, message = "El título de la unidad no puede exceder 255 caracteres")
        private String titulo;
        
        private Integer orden;
        
        @Valid
        private List<LeccionCompletaDTO> lecciones = new ArrayList<>();
        
        // Constructores
        public UnidadCompletaDTO() {}
        
        public UnidadCompletaDTO(String titulo, Integer orden) {
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
        
        public List<LeccionCompletaDTO> getLecciones() {
            return lecciones;
        }
        
        public void setLecciones(List<LeccionCompletaDTO> lecciones) {
            this.lecciones = lecciones;
        }
        
        @Override
        public String toString() {
            return "UnidadCompletaDTO{" +
                    "id=" + id +
                    ", titulo='" + titulo + '\'' +
                    ", orden=" + orden +
                    ", lecciones=" + lecciones.size() + " lecciones" +
                    '}';
        }
    }
    
    // Clase interna para Lección Completa
    public static class LeccionCompletaDTO {
        
        private Integer id;
        
        @NotBlank(message = "El título de la lección es requerido")
        @Size(max = 255, message = "El título de la lección no puede exceder 255 caracteres")
        private String titulo;
        
        private Integer orden;
        
        @Size(max = 50, message = "El tipo de material no puede exceder 50 caracteres")
        private String tipoMaterial; // video, pdf, link
        
        private String urlMaterial;
        
        private Integer materialId; // ID del material subido
        
        // Constructores
        public LeccionCompletaDTO() {}
        
        public LeccionCompletaDTO(String titulo, Integer orden, String tipoMaterial, String urlMaterial) {
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
        
        public Integer getMaterialId() {
            return materialId;
        }
        
        public void setMaterialId(Integer materialId) {
            this.materialId = materialId;
        }
        
        @Override
        public String toString() {
            return "LeccionCompletaDTO{" +
                    "id=" + id +
                    ", titulo='" + titulo + '\'' +
                    ", orden=" + orden +
                    ", tipoMaterial='" + tipoMaterial + '\'' +
                    ", urlMaterial='" + urlMaterial + '\'' +
                    '}';
        }
    }
}
