package com.stefanini.portal_capacitaciones.content.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "scheduler.file-sync")
@Data
public class SchedulerConfig {
    
    private boolean enabled = true;
    private String cron = "0 */5 * * * ?"; // Cada 5 minutos por defecto
    private int batchSize = 10;
}
