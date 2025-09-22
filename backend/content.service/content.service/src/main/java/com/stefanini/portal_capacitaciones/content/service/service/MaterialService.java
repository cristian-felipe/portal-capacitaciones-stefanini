package com.stefanini.portal_capacitaciones.content.service.service;

import com.stefanini.portal_capacitaciones.content.service.entity.Material;

import com.stefanini.portal_capacitaciones.content.service.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialService {
    
    private final MaterialRepository materialRepository;
    private final S3Service s3Service;
    private final MigrationService migrationService;
    
    /**
     * Obtiene todos los materiales
     */
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
    
    /**
     * Obtiene un material por ID
     */
    public Optional<Material> getMaterialById(Integer id) {
        return materialRepository.findById(id.longValue());
    }
    
    /**
     * Obtiene materiales pendientes de subida a S3
     */
    public List<Material> getPendingS3UploadMaterials(int limit) {
        return materialRepository.findPendingS3UploadWithLimit(limit);
    }
    
    /**
     * Cuenta materiales pendientes de subida a S3
     */
    public long countPendingS3UploadMaterials() {
        return materialRepository.countPendingS3Upload();
    }
    
    /**
     * Procesa un material para migración completa a S3
     */
    @Transactional
    public boolean processMaterialForS3(Material material) {
        return migrationService.startMigration(material);
    }
    
    /**
     * Actualiza el estado de S3 de un material
     */
    @Transactional
    public void updateMaterialS3Status(Material material, boolean uploaded, String errorMessage) {
        material.setS3Uploaded(uploaded);
        material.setS3UploadDate(LocalDateTime.now());
        material.setS3ErrorMessage(errorMessage);
        materialRepository.save(material);
    }
    
    /**
     * Procesa múltiples materiales en lote
     */
    @Transactional
    public int processBatchMaterials(List<Material> materials) {
        int successCount = 0;
        
        for (Material material : materials) {
            if (migrationService.startMigration(material)) {
                successCount++;
            }
        }
        
        log.info("Procesamiento en lote completado: {}/{} materiales procesados exitosamente", 
                successCount, materials.size());
        
        return successCount;
    }
    
    /**
     * Obtiene un material por URL de acceso
     */
    public Optional<Material> getMaterialByUrlAcceso(String urlAcceso) {
        return materialRepository.findByUrlAcceso(urlAcceso);
    }
    
    /**
     * Obtiene materiales activos
     */
    public List<Material> getActiveMaterials() {
        return materialRepository.findByActivoTrue();
    }
    
    /**
     * Obtiene materiales por tipo
     */
    public List<Material> getMaterialsByType(String tipoMaterial) {
        return materialRepository.findByTipoMaterial(tipoMaterial);
    }
    
    /**
     * Crea un nuevo material
     */
    @Transactional
    public Material createMaterial(Material material) {
        material.setS3Uploaded(false);
        material.setS3UploadDate(null);
        material.setS3ErrorMessage(null);
        return materialRepository.save(material);
    }
    
    /**
     * Actualiza un material existente
     */
    @Transactional
    public Material updateMaterial(Material material) {
        return materialRepository.save(material);
    }
    
    /**
     * Elimina un material
     */
    @Transactional
    public void deleteMaterial(Integer id) {
        Optional<Material> materialOpt = materialRepository.findById(id.longValue());
        if (materialOpt.isPresent()) {
            Material material = materialOpt.get();
            
            // Si el material está en S3, eliminarlo también
            if (material.getS3Key() != null && material.getS3Uploaded()) {
                s3Service.deleteFile(material.getS3Key());
            }
            
            materialRepository.deleteById(id.longValue());
        }
    }
    
    /**
     * Obtiene materiales listos para migración
     */
    public List<Material> getMaterialsReadyForMigration(int limit) {
        return migrationService.getMaterialsReadyForMigration(limit);
    }
    
    /**
     * Obtiene materiales que necesitan reintento
     */
    public List<Material> getMaterialsForRetry(int limit) {
        return migrationService.getMaterialsForRetry(limit);
    }
    
    /**
     * Obtiene materiales por estado de migración
     */
    public List<Material> getMaterialsByMigrationStatus(String status) {
        return materialRepository.findByMigrationStatus(status);
    }
    
    /**
     * Obtiene materiales fallidos
     */
    public List<Material> getFailedMaterials(int limit) {
        return materialRepository.findFailedMaterials(limit);
    }
    
    /**
     * Obtiene materiales completados
     */
    public List<Material> getCompletedMaterials(int limit) {
        return materialRepository.findCompletedMaterials(limit);
    }
    
    /**
     * Obtiene estadísticas de migración
     */
    public MigrationService.MigrationStats getMigrationStats() {
        return migrationService.getMigrationStats();
    }
    
    /**
     * Inicia la migración de un material
     */
    public void startMigration(Material material) {
        migrationService.startMigration(material);
    }
    
    public FileDownloadResult getFileForDownload(Integer materialId) {
        Material material = validateMaterialForDownload(materialId);
        byte[] fileContent = s3Service.downloadFile(material.getS3Key());
        
        return new FileDownloadResult(fileContent, material);
    }
    
    private Material validateMaterialForDownload(Integer materialId) {
        Optional<Material> materialOpt = getMaterialById(materialId);
        if (materialOpt.isEmpty()) {
            throw new RuntimeException("Material no encontrado con ID: " + materialId);
        }

        Material material = materialOpt.get();
        
        if (material.getS3Uploaded() == null || !material.getS3Uploaded() || material.getS3Key() == null) {
            throw new RuntimeException("Material ID " + materialId + " no está disponible en S3");
        }

        return material;
    }
    
    public static class FileDownloadResult {
        private final byte[] fileContent;
        private final Material material;
        
        public FileDownloadResult(byte[] fileContent, Material material) {
            this.fileContent = fileContent;
            this.material = material;
        }
        
        public byte[] getFileContent() { return fileContent; }
        public Material getMaterial() { return material; }
        public String getContentType() { return getContentTypeFromExtension(material.getExtension()); }
        public String getFileName() { return material.getNombreOriginal(); }
        
        private String getContentTypeFromExtension(String extension) {
            if (extension == null) return "application/octet-stream";
            
            return switch (extension.toLowerCase()) {
                case "pdf" -> "application/pdf";
                case "jpg", "jpeg" -> "image/jpeg";
                case "png" -> "image/png";
                case "gif" -> "image/gif";
                case "mp4" -> "video/mp4";
                case "avi" -> "video/x-msvideo";
                case "mov" -> "video/quicktime";
                case "doc" -> "application/msword";
                case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                case "txt" -> "text/plain";
                default -> "application/octet-stream";
            };
        }
    }
}
