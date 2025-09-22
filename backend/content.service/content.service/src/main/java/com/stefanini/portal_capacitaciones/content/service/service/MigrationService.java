package com.stefanini.portal_capacitaciones.content.service.service;

import com.stefanini.portal_capacitaciones.content.service.entity.Material;
import com.stefanini.portal_capacitaciones.content.service.enums.MigrationStatus;
import com.stefanini.portal_capacitaciones.content.service.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationService {
    
    private final MaterialRepository materialRepository;
    private final S3Service s3Service;
    
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY_MINUTES = 5;
    
    /**
     * Inicia el proceso de migración de un material
     */
    @Transactional
    public boolean startMigration(Material material) {
        try {
            log.info("Iniciando migración del material ID: {}", material.getId());
            
            // Verificar que el archivo existe
            String fullFilePath = s3Service.buildFullFilePath(material.getRutaArchivo());
            if (!Files.exists(Paths.get(fullFilePath))) {
                log.error("Archivo no encontrado: {}", fullFilePath);
                updateMigrationStatus(material, MigrationStatus.FAILED, "Archivo no encontrado: " + fullFilePath);
                return false;
            }
            
            // Actualizar estado a EN_PROGRESO
            updateMigrationStatus(material, MigrationStatus.IN_PROGRESS, null);
            
            // Calcular hash del archivo
            String fileHash = calculateFileHash(fullFilePath);
            material.setFileHash(fileHash);
            
            // Verificar tamaño del archivo
            long fileSize = Files.size(Paths.get(fullFilePath));
            material.setFileSizeVerified(fileSize == material.getTamañoBytes());
            
            // Crear backup del archivo
            if (createBackup(material, fullFilePath)) {
                updateMigrationStatus(material, MigrationStatus.BACKUP_CREATED, null);
            }
            
            // Subir archivo a S3
            String s3Key = s3Service.uploadFile(material.getRutaArchivo(), material.getNombreOriginal());
            material.setS3Key(s3Key);
            material.setS3Uploaded(true);
            material.setS3UploadDate(LocalDateTime.now());
            
            // Verificar integridad del archivo en S3
            if (verifyS3FileIntegrity(material, s3Key)) {
                updateMigrationStatus(material, MigrationStatus.VERIFIED, null);
                
                // Eliminar archivo local
                if (deleteLocalFile(material, fullFilePath)) {
                    updateMigrationStatus(material, MigrationStatus.LOCAL_DELETED, null);
                }
                
                // Marcar como completado
                updateMigrationStatus(material, MigrationStatus.COMPLETED, null);
                log.info("Migración completada exitosamente para material ID: {}", material.getId());
                return true;
            } else {
                updateMigrationStatus(material, MigrationStatus.FAILED, "Error en verificación de integridad S3");
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error durante la migración del material ID: {} - {}", material.getId(), e.getMessage(), e);
            handleMigrationFailure(material, e.getMessage());
            return false;
        }
    }
    
    /**
     * Maneja el fallo de migración y programa reintentos
     */
    @Transactional
    public void handleMigrationFailure(Material material, String errorMessage) {
        int attempts = material.getMigrationAttempts() != null ? material.getMigrationAttempts() : 0;
        attempts++;
        
        material.setMigrationAttempts(attempts);
        material.setMigrationLastAttempt(LocalDateTime.now());
        material.setS3ErrorMessage(errorMessage);
        
        if (attempts >= MAX_RETRY_ATTEMPTS) {
            updateMigrationStatus(material, MigrationStatus.FAILED, "Máximo de reintentos alcanzado: " + errorMessage);
            log.error("Material ID: {} - Máximo de reintentos alcanzado", material.getId());
        } else {
            // Programar siguiente reintento con backoff exponencial
            int delayMinutes = RETRY_DELAY_MINUTES * (int) Math.pow(2, attempts - 1);
            LocalDateTime nextRetry = LocalDateTime.now().plusMinutes(delayMinutes);
            material.setMigrationNextRetry(nextRetry);
            updateMigrationStatus(material, MigrationStatus.RETRY, "Programado para reintento en " + delayMinutes + " minutos");
            log.warn("Material ID: {} - Programado para reintento en {} minutos (intento {}/{})", 
                    material.getId(), delayMinutes, attempts, MAX_RETRY_ATTEMPTS);
        }
        
        materialRepository.save(material);
    }
    
    /**
     * Obtiene materiales listos para migración
     */
    public List<Material> getMaterialsReadyForMigration(int limit) {
        return materialRepository.findMaterialsReadyForMigration(limit);
    }
    
    /**
     * Obtiene materiales que necesitan reintento
     */
    public List<Material> getMaterialsForRetry(int limit) {
        return materialRepository.findMaterialsForRetry(LocalDateTime.now(), limit);
    }
    
    /**
     * Actualiza el estado de migración
     */
    @Transactional
    public void updateMigrationStatus(Material material, MigrationStatus status, String errorMessage) {
        material.setMigrationStatus(status.toString());
        material.setS3ErrorMessage(errorMessage);
        material.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(material);
        
        log.debug("Material ID: {} - Estado actualizado a: {}", material.getId(), status.getDescription());
    }
    
    /**
     * Calcula el hash MD5 del archivo
     */
    private String calculateFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] hashBytes = md.digest(fileBytes);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * Crea un backup del archivo
     */
    private boolean createBackup(Material material, String originalFilePath) {
        try {
            String backupPath = originalFilePath + ".backup";
            Files.copy(Paths.get(originalFilePath), Paths.get(backupPath), StandardCopyOption.REPLACE_EXISTING);
            material.setBackupCreated(true);
            log.info("Backup creado para material ID: {} en {}", material.getId(), backupPath);
            return true;
        } catch (IOException e) {
            log.error("Error al crear backup para material ID: {} - {}", material.getId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica la integridad del archivo en S3
     */
    private boolean verifyS3FileIntegrity(Material material, String s3Key) {
        try {
            // Verificar que el archivo existe en S3
            if (!s3Service.fileExists(s3Key)) {
                log.error("Archivo no encontrado en S3: {}", s3Key);
                return false;
            }
            
            // Aquí se podría implementar verificación de hash si S3 lo soporta
            // Por ahora, solo verificamos que existe
            log.info("Verificación de integridad S3 exitosa para material ID: {}", material.getId());
            return true;
        } catch (Exception e) {
            log.error("Error en verificación de integridad S3 para material ID: {} - {}", material.getId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina el archivo local
     */
    private boolean deleteLocalFile(Material material, String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            material.setLocalFileDeleted(true);
            log.info("Archivo local eliminado para material ID: {} - {}", material.getId(), filePath);
            return true;
        } catch (IOException e) {
            log.error("Error al eliminar archivo local para material ID: {} - {}", material.getId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene estadísticas de migración
     */
    public MigrationStats getMigrationStats() {
        long total = materialRepository.count();
        long pending = materialRepository.countByMigrationStatus(MigrationStatus.PENDING.toString());
        long inProgress = materialRepository.countByMigrationStatus(MigrationStatus.IN_PROGRESS.toString());
        long completed = materialRepository.countByMigrationStatus(MigrationStatus.COMPLETED.toString());
        long failed = materialRepository.countByMigrationStatus(MigrationStatus.FAILED.toString());
        long retry = materialRepository.countByMigrationStatus(MigrationStatus.RETRY.toString());
        
        return new MigrationStats(total, pending, inProgress, completed, failed, retry);
    }
    
    /**
     * Clase para estadísticas de migración
     */
    public static class MigrationStats {
        public final long total;
        public final long pending;
        public final long inProgress;
        public final long completed;
        public final long failed;
        public final long retry;
        
        public MigrationStats(long total, long pending, long inProgress, long completed, long failed, long retry) {
            this.total = total;
            this.pending = pending;
            this.inProgress = inProgress;
            this.completed = completed;
            this.failed = failed;
            this.retry = retry;
        }
        
        public double getCompletionRate() {
            return total > 0 ? (double) completed / total : 0.0;
        }
        
        public double getFailureRate() {
            return total > 0 ? (double) failed / total : 0.0;
        }
    }
}
