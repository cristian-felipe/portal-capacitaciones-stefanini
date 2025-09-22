package com.stefanini.portal_capacitaciones.content.service.enums;

public enum MigrationStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En Proceso"),
    COMPLETED("Completado"),
    FAILED("Fallido"),
    RETRY("Reintento"),
    VERIFIED("Verificado"),
    BACKUP_CREATED("Backup Creado"),
    LOCAL_DELETED("Archivo Local Eliminado");
    
    private final String description;
    
    MigrationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name();
    }
}
