package com.stefanini.portal_capacitaciones.content.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "courses.service")
@Data
public class CoursesServiceConfig {
    
    private String basePath;
    
    /**
     * Construye la ruta completa del archivo basándose en la ruta relativa del material
     * @param relativePath Ruta relativa del archivo (ej: /uploads/1758500443836_99b85b78.pdf)
     * @return Ruta completa del archivo
     */
    public String buildFullFilePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        
        // Remover la barra inicial si existe
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        
        // Construir la ruta completa
        return basePath + "/" + relativePath;
    }
    
    /**
     * Verifica si la ruta base está configurada
     * @return true si la ruta base está configurada
     */
    public boolean isConfigured() {
        return basePath != null && !basePath.trim().isEmpty();
    }
}
