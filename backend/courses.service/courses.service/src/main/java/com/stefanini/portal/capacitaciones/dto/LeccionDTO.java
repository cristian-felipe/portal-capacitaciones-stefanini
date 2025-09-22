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
    
    // Información completa del material (si existe)
    private Integer materialId;
    private String materialNombreOriginal;
    private String materialTipoMaterial;
    private String materialExtension;
    private Long materialTamañoBytes;
    private String materialTamañoFormateado;
    private String materialUrlAcceso;
    private String materialDescripcion;
    private Boolean materialS3Uploaded;
    private String materialS3Key;
    
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
    
    public Integer getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
    
    public String getMaterialNombreOriginal() {
        return materialNombreOriginal;
    }
    
    public void setMaterialNombreOriginal(String materialNombreOriginal) {
        this.materialNombreOriginal = materialNombreOriginal;
    }
    
    public String getMaterialTipoMaterial() {
        return materialTipoMaterial;
    }
    
    public void setMaterialTipoMaterial(String materialTipoMaterial) {
        this.materialTipoMaterial = materialTipoMaterial;
    }
    
    public String getMaterialExtension() {
        return materialExtension;
    }
    
    public void setMaterialExtension(String materialExtension) {
        this.materialExtension = materialExtension;
    }
    
    public Long getMaterialTamañoBytes() {
        return materialTamañoBytes;
    }
    
    public void setMaterialTamañoBytes(Long materialTamañoBytes) {
        this.materialTamañoBytes = materialTamañoBytes;
    }
    
    public String getMaterialTamañoFormateado() {
        return materialTamañoFormateado;
    }
    
    public void setMaterialTamañoFormateado(String materialTamañoFormateado) {
        this.materialTamañoFormateado = materialTamañoFormateado;
    }
    
    public String getMaterialUrlAcceso() {
        return materialUrlAcceso;
    }
    
    public void setMaterialUrlAcceso(String materialUrlAcceso) {
        this.materialUrlAcceso = materialUrlAcceso;
    }
    
    public String getMaterialDescripcion() {
        return materialDescripcion;
    }
    
    public void setMaterialDescripcion(String materialDescripcion) {
        this.materialDescripcion = materialDescripcion;
    }
    
    public Boolean getMaterialS3Uploaded() {
        return materialS3Uploaded;
    }
    
    public void setMaterialS3Uploaded(Boolean materialS3Uploaded) {
        this.materialS3Uploaded = materialS3Uploaded;
    }
    
    public String getMaterialS3Key() {
        return materialS3Key;
    }
    
    public void setMaterialS3Key(String materialS3Key) {
        this.materialS3Key = materialS3Key;
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


