package com.stefanini.portal_capacitaciones.content.service.repository;

import com.stefanini.portal_capacitaciones.content.service.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    /**
     * Busca materiales que no han sido subidos a S3
     */
    @Query("SELECT m FROM Material m WHERE m.s3Uploaded IS NULL OR m.s3Uploaded = false")
    List<Material> findPendingS3Upload();
    
    /**
     * Busca materiales que no han sido subidos a S3 con límite
     */
    @Query("SELECT m FROM Material m WHERE m.s3Uploaded IS NULL OR m.s3Uploaded = false ORDER BY m.fechaSubida ASC")
    List<Material> findPendingS3UploadWithLimit(@Param("limit") int limit);
    
    /**
     * Busca materiales por URL de acceso
     */
    Optional<Material> findByUrlAcceso(String urlAcceso);
    
    /**
     * Busca materiales activos
     */
    List<Material> findByActivoTrue();
    
    /**
     * Busca materiales por tipo
     */
    List<Material> findByTipoMaterial(String tipoMaterial);
    
    /**
     * Cuenta materiales pendientes de subida a S3
     */
    @Query("SELECT COUNT(m) FROM Material m WHERE m.s3Uploaded IS NULL OR m.s3Uploaded = false")
    long countPendingS3Upload();
    
    /**
     * Busca materiales listos para migración (PENDING o sin estado)
     */
    @Query("SELECT m FROM Material m WHERE (m.migrationStatus IS NULL OR m.migrationStatus = 'PENDING') AND (m.s3Uploaded IS NULL OR m.s3Uploaded = false) ORDER BY m.fechaSubida ASC")
    List<Material> findMaterialsReadyForMigration(@Param("limit") int limit);
    
    /**
     * Busca materiales que necesitan reintento
     */
    @Query("SELECT m FROM Material m WHERE m.migrationStatus = 'RETRY' AND m.migrationNextRetry <= :now ORDER BY m.migrationNextRetry ASC")
    List<Material> findMaterialsForRetry(@Param("now") LocalDateTime now, @Param("limit") int limit);
    
    /**
     * Cuenta materiales por estado de migración
     */
    @Query("SELECT COUNT(m) FROM Material m WHERE m.migrationStatus = :status")
    long countByMigrationStatus(@Param("status") String status);
    
    /**
     * Busca materiales por estado de migración
     */
    List<Material> findByMigrationStatus(String migrationStatus);
    
    /**
     * Busca materiales fallidos
     */
    @Query("SELECT m FROM Material m WHERE m.migrationStatus = 'FAILED' ORDER BY m.migrationLastAttempt DESC")
    List<Material> findFailedMaterials(@Param("limit") int limit);
    
    /**
     * Busca materiales completados
     */
    @Query("SELECT m FROM Material m WHERE m.migrationStatus = 'COMPLETED' ORDER BY m.s3UploadDate DESC")
    List<Material> findCompletedMaterials(@Param("limit") int limit);
}
