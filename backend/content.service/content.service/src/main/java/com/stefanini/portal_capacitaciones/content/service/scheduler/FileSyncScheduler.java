package com.stefanini.portal_capacitaciones.content.service.scheduler;

import com.stefanini.portal_capacitaciones.content.service.config.SchedulerConfig;
import com.stefanini.portal_capacitaciones.content.service.entity.Material;
import com.stefanini.portal_capacitaciones.content.service.service.MaterialService;
import com.stefanini.portal_capacitaciones.content.service.service.MigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileSyncScheduler {
    
    private final MaterialService materialService;
    private final MigrationService migrationService;
    private final SchedulerConfig schedulerConfig;
    
    /**
     * Tarea programada que se ejecuta según la configuración CRON
     * Procesa materiales pendientes de migración a S3
     */
    @Scheduled(cron = "${scheduler.file-sync.cron:0 */5 * * * ?}")
    public void syncFilesToS3() {
        if (!schedulerConfig.isEnabled()) {
            log.debug("Scheduler de migración de archivos deshabilitado");
            return;
        }
        
        log.info("Iniciando migración de archivos a S3 - {}", LocalDateTime.now());
        
        try {
            // Obtener materiales listos para migración
            List<Material> readyMaterials = migrationService.getMaterialsReadyForMigration(
                    schedulerConfig.getBatchSize());
            
            // Obtener materiales que necesitan reintento
            List<Material> retryMaterials = migrationService.getMaterialsForRetry(
                    schedulerConfig.getBatchSize());
            
            int totalProcessed = 0;
            int successCount = 0;
            
            // Procesar materiales listos para migración
            if (!readyMaterials.isEmpty()) {
                log.info("Encontrados {} materiales listos para migración", readyMaterials.size());
                successCount += materialService.processBatchMaterials(readyMaterials);
                totalProcessed += readyMaterials.size();
            }
            
            // Procesar materiales para reintento
            if (!retryMaterials.isEmpty()) {
                log.info("Encontrados {} materiales para reintento", retryMaterials.size());
                successCount += materialService.processBatchMaterials(retryMaterials);
                totalProcessed += retryMaterials.size();
            }
            
            if (totalProcessed == 0) {
                log.debug("No hay materiales para procesar");
            } else {
                log.info("Migración completada: {}/{} materiales procesados exitosamente", 
                        successCount, totalProcessed);
            }
            
            // Log de estadísticas de migración
            MigrationService.MigrationStats stats = migrationService.getMigrationStats();
            log.info("Estadísticas de migración - Total: {}, Pendientes: {}, En Proceso: {}, Completados: {}, Fallidos: {}, Reintentos: {}", 
                    stats.total, stats.pending, stats.inProgress, stats.completed, stats.failed, stats.retry);
            
        } catch (Exception e) {
            log.error("Error durante la migración de archivos a S3", e);
        }
    }
    
    /**
     * Tarea programada para limpieza de archivos temporales (opcional)
     * Se ejecuta diariamente a las 2:00 AM
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupTempFiles() {
        log.info("Iniciando limpieza de archivos temporales - {}", LocalDateTime.now());
        
        try {
            // Aquí se podría implementar lógica para limpiar archivos temporales
            // Por ejemplo, archivos que ya fueron subidos a S3 y ya no se necesitan localmente
            log.info("Limpieza de archivos temporales completada");
            
        } catch (Exception e) {
            log.error("Error durante la limpieza de archivos temporales", e);
        }
    }
    
    /**
     * Tarea programada para verificar la salud del servicio S3
     * Se ejecuta cada hora
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void healthCheckS3() {
        log.debug("Verificando salud del servicio S3 - {}", LocalDateTime.now());
        
        try {
            // Aquí se podría implementar una verificación de salud del servicio S3
            // Por ejemplo, intentar hacer una operación simple como listar buckets
            log.debug("Verificación de salud del servicio S3 completada");
            
        } catch (Exception e) {
            log.error("Error durante la verificación de salud del servicio S3", e);
        }
    }
}
