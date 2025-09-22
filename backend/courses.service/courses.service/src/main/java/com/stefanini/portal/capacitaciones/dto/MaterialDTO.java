package com.stefanini.portal.capacitaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MaterialDTO {
    
    private Integer id;
    
    @NotBlank(message = "El nombre del archivo es requerido")
    @Size(max = 255, message = "El nombre del archivo no puede exceder 255 caracteres")
    private String nombreArchivo;
    
    @NotBlank(message = "El nombre original es requerido")
    @Size(max = 255, message = "El nombre original no puede exceder 255 caracteres")
    private String nombreOriginal;
    
    @NotBlank(message = "El tipo de material es requerido")
    @Size(max = 50, message = "El tipo de material no puede exceder 50 caracteres")
    private String tipoMaterial;
    
    @Size(max = 10, message = "La extensión no puede exceder 10 caracteres")
    private String extension;
    
    private Long tamañoBytes;
    private String tamañoFormateado;
    private String rutaArchivo;
    private String urlAcceso;
    private String descripcion;
    private LocalDateTime fechaSubida;
    private Boolean activo;
    
    // Campos adicionales para migración y S3
    private String migrationStatus;
    private Integer migrationAttempts;
    private LocalDateTime migrationLastAttempt;
    private LocalDateTime migrationNextRetry;
    private String fileHash;
    private Boolean fileSizeVerified;
    private Boolean localFileDeleted;
    private Boolean backupCreated;
    private LocalDateTime updatedAt;
    private String s3Key;
    private Boolean s3Uploaded;
    private LocalDateTime s3UploadDate;
    private String s3ErrorMessage;
    
    // Constructores
    public MaterialDTO() {}
    
    public MaterialDTO(String nombreArchivo, String nombreOriginal, String tipoMaterial, 
                      String extension, Long tamañoBytes, String rutaArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.nombreOriginal = nombreOriginal;
        this.tipoMaterial = tipoMaterial;
        this.extension = extension;
        this.tamañoBytes = tamañoBytes;
        this.rutaArchivo = rutaArchivo;
        this.activo = true;
    }
    
    // Métodos de conveniencia
    public boolean esPdf() {
        return "pdf".equalsIgnoreCase(tipoMaterial) || "pdf".equalsIgnoreCase(extension);
    }
    
    public boolean esVideo() {
        return "video".equalsIgnoreCase(tipoMaterial) || 
               ("mp4".equalsIgnoreCase(extension) || "avi".equalsIgnoreCase(extension) || 
                "mov".equalsIgnoreCase(extension) || "wmv".equalsIgnoreCase(extension));
    }
    
    public boolean esImagen() {
        return "imagen".equalsIgnoreCase(tipoMaterial) || 
               ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension) || 
                "png".equalsIgnoreCase(extension) || "gif".equalsIgnoreCase(extension));
    }
    
    public boolean esDocumento() {
        return "documento".equalsIgnoreCase(tipoMaterial) || 
               ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension) || 
                "txt".equalsIgnoreCase(extension));
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public String getNombreOriginal() {
        return nombreOriginal;
    }
    
    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }
    
    public String getTipoMaterial() {
        return tipoMaterial;
    }
    
    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public Long getTamañoBytes() {
        return tamañoBytes;
    }
    
    public void setTamañoBytes(Long tamañoBytes) {
        this.tamañoBytes = tamañoBytes;
    }
    
    public String getTamañoFormateado() {
        return tamañoFormateado;
    }
    
    public void setTamañoFormateado(String tamañoFormateado) {
        this.tamañoFormateado = tamañoFormateado;
    }
    
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    public String getUrlAcceso() {
        return urlAcceso;
    }
    
    public void setUrlAcceso(String urlAcceso) {
        this.urlAcceso = urlAcceso;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }
    
    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getMigrationStatus() {
        return migrationStatus;
    }
    
    public void setMigrationStatus(String migrationStatus) {
        this.migrationStatus = migrationStatus;
    }
    
    public Integer getMigrationAttempts() {
        return migrationAttempts;
    }
    
    public void setMigrationAttempts(Integer migrationAttempts) {
        this.migrationAttempts = migrationAttempts;
    }
    
    public LocalDateTime getMigrationLastAttempt() {
        return migrationLastAttempt;
    }
    
    public void setMigrationLastAttempt(LocalDateTime migrationLastAttempt) {
        this.migrationLastAttempt = migrationLastAttempt;
    }
    
    public LocalDateTime getMigrationNextRetry() {
        return migrationNextRetry;
    }
    
    public void setMigrationNextRetry(LocalDateTime migrationNextRetry) {
        this.migrationNextRetry = migrationNextRetry;
    }
    
    public String getFileHash() {
        return fileHash;
    }
    
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
    
    public Boolean getFileSizeVerified() {
        return fileSizeVerified;
    }
    
    public void setFileSizeVerified(Boolean fileSizeVerified) {
        this.fileSizeVerified = fileSizeVerified;
    }
    
    public Boolean getLocalFileDeleted() {
        return localFileDeleted;
    }
    
    public void setLocalFileDeleted(Boolean localFileDeleted) {
        this.localFileDeleted = localFileDeleted;
    }
    
    public Boolean getBackupCreated() {
        return backupCreated;
    }
    
    public void setBackupCreated(Boolean backupCreated) {
        this.backupCreated = backupCreated;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getS3Key() {
        return s3Key;
    }
    
    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }
    
    public Boolean getS3Uploaded() {
        return s3Uploaded;
    }
    
    public void setS3Uploaded(Boolean s3Uploaded) {
        this.s3Uploaded = s3Uploaded;
    }
    
    public LocalDateTime getS3UploadDate() {
        return s3UploadDate;
    }
    
    public void setS3UploadDate(LocalDateTime s3UploadDate) {
        this.s3UploadDate = s3UploadDate;
    }
    
    public String getS3ErrorMessage() {
        return s3ErrorMessage;
    }
    
    public void setS3ErrorMessage(String s3ErrorMessage) {
        this.s3ErrorMessage = s3ErrorMessage;
    }
    
    @Override
    public String toString() {
        return "MaterialDTO{" +
                "id=" + id +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", nombreOriginal='" + nombreOriginal + '\'' +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", extension='" + extension + '\'' +
                ", tamañoBytes=" + tamañoBytes +
                ", urlAcceso='" + urlAcceso + '\'' +
                ", fechaSubida=" + fechaSubida +
                ", activo=" + activo +
                '}';
    }
}
