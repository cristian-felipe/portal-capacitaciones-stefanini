package com.stefanini.portal_capacitaciones.content.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "materiales", schema = "capacitaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
    
    @Column(name = "extension", length = 10)
    private String extension;
    
    @Column(name = "fecha_subida", nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaSubida;
    
    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;
    
    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;
    
    @Column(name = "ruta_archivo", nullable = false, length = 255)
    private String rutaArchivo;
    
    @Column(name = "tamaño_bytes")
    private Long tamañoBytes;
    
    @Column(name = "tipo_material", nullable = false, length = 50)
    private String tipoMaterial;
    
    @Column(name = "url_acceso", length = 255)
    private String urlAcceso;
    
    @Column(name = "s3_key")
    private String s3Key;
    
    @Column(name = "s3_uploaded")
    private Boolean s3Uploaded;
    
    @Column(name = "s3_upload_date")
    private LocalDateTime s3UploadDate;
    
    @Column(name = "s3_error_message")
    private String s3ErrorMessage;
    
    @Column(name = "migration_status", length = 20)
    private String migrationStatus;
    
    @Column(name = "migration_attempts")
    private Integer migrationAttempts;
    
    @Column(name = "migration_last_attempt")
    private LocalDateTime migrationLastAttempt;
    
    @Column(name = "migration_next_retry")
    private LocalDateTime migrationNextRetry;
    
    @Column(name = "file_hash")
    private String fileHash;
    
    @Column(name = "file_size_verified")
    private Boolean fileSizeVerified;
    
    @Column(name = "local_file_deleted")
    private Boolean localFileDeleted;
    
    @Column(name = "backup_created")
    private Boolean backupCreated;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
