package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "materiales", schema = "capacitaciones")
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El nombre del archivo es requerido")
    @Size(max = 255, message = "El nombre del archivo no puede exceder 255 caracteres")
    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;
    
    @NotBlank(message = "El nombre original es requerido")
    @Size(max = 255, message = "El nombre original no puede exceder 255 caracteres")
    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;
    
    @NotBlank(message = "El tipo de material es requerido")
    @Size(max = 50, message = "El tipo de material no puede exceder 50 caracteres")
    @Column(name = "tipo_material", nullable = false, length = 50)
    private String tipoMaterial; // pdf, video, imagen, documento
    
    @Size(max = 10, message = "La extensión no puede exceder 10 caracteres")
    @Column(name = "extension", length = 10)
    private String extension;
    
    @Column(name = "tamaño_bytes")
    private Long tamañoBytes;
    
    @NotBlank(message = "La ruta del archivo es requerida")
    @Column(name = "ruta_archivo", nullable = false)
    private String rutaArchivo;
    
    @Column(name = "url_acceso")
    private String urlAcceso;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    // Campos adicionales para migración y S3
    @Column(name = "migration_status", length = 20)
    private String migrationStatus;
    
    @Column(name = "migration_attempts")
    private Integer migrationAttempts;
    
    @Column(name = "migration_last_attempt")
    private LocalDateTime migrationLastAttempt;
    
    @Column(name = "migration_next_retry")
    private LocalDateTime migrationNextRetry;
    
    @Column(name = "file_hash", length = 255)
    private String fileHash;
    
    @Column(name = "file_size_verified")
    private Boolean fileSizeVerified;
    
    @Column(name = "local_file_deleted")
    private Boolean localFileDeleted;
    
    @Column(name = "backup_created")
    private Boolean backupCreated;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "s3_key", length = 255)
    private String s3Key;
    
    @Column(name = "s3_uploaded")
    private Boolean s3Uploaded;
    
    @Column(name = "s3_upload_date")
    private LocalDateTime s3UploadDate;
    
    @Column(name = "s3_error_message", columnDefinition = "TEXT")
    private String s3ErrorMessage;
    
    // Constructores
    public Material() {}
    
    public Material(String nombreArchivo, String nombreOriginal, String tipoMaterial, 
                   String extension, Long tamañoBytes, String rutaArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.nombreOriginal = nombreOriginal;
        this.tipoMaterial = tipoMaterial;
        this.extension = extension;
        this.tamañoBytes = tamañoBytes;
        this.rutaArchivo = rutaArchivo;
        this.fechaSubida = LocalDateTime.now();
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
    
    public String getTamañoFormateado() {
        if (tamañoBytes == null) return "0 B";
        
        long bytes = tamañoBytes;
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
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
    
    // Métodos de conveniencia adicionales
    public boolean isMigratedToS3() {
        return s3Uploaded != null && s3Uploaded;
    }
    
    public boolean isLocalFileAvailable() {
        return localFileDeleted == null || !localFileDeleted;
    }
    
    public boolean hasMigrationErrors() {
        return migrationStatus != null && migrationStatus.equals("error");
    }
    
    public String getAccessUrl() {
        if (isMigratedToS3() && s3Key != null) {
            return s3Key; // URL de S3
        }
        return urlAcceso; // URL local
    }
    
    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", nombreOriginal='" + nombreOriginal + '\'' +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", extension='" + extension + '\'' +
                ", tamañoBytes=" + tamañoBytes +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                ", fechaSubida=" + fechaSubida +
                ", activo=" + activo +
                '}';
    }
}
