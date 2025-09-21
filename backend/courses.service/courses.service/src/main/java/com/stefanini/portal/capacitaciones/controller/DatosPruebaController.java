package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.entity.Insignia;
import com.stefanini.portal.capacitaciones.repository.InsigniaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/datos-prueba")
@Tag(name = "Datos de Prueba", description = "API para inicializar datos de prueba")
@CrossOrigin(origins = "*")
public class DatosPruebaController {
    
    @Autowired
    private InsigniaRepository insigniaRepository;
    
    @PostMapping("/inicializar-insignias")
    @Operation(summary = "Inicializar insignias de prueba")
    public ResponseEntity<String> inicializarInsignias() {
        try {
            // Verificar si ya existen insignias
            if (insigniaRepository.count() > 0) {
                return ResponseEntity.ok("Las insignias ya están inicializadas");
            }
            
            // Crear insignias de prueba
            Insignia insignia1 = new Insignia(
                "Primer Curso Completado",
                "Has completado tu primer curso de capacitación",
                "https://via.placeholder.com/100x100/4CAF50/FFFFFF?text=1st"
            );
            
            Insignia insignia2 = new Insignia(
                "Estudiante Dedicado",
                "Has completado 5 lecciones",
                "https://via.placeholder.com/100x100/2196F3/FFFFFF?text=5"
            );
            
            Insignia insignia3 = new Insignia(
                "Experto en Fullstack",
                "Has completado el curso de Desarrollo Fullstack",
                "https://via.placeholder.com/100x100/FF9800/FFFFFF?text=FS"
            );
            
            Insignia insignia4 = new Insignia(
                "Maestro de APIs",
                "Has completado el curso de APIs e Integraciones",
                "https://via.placeholder.com/100x100/9C27B0/FFFFFF?text=API"
            );
            
            Insignia insignia5 = new Insignia(
                "Cloud Master",
                "Has completado el curso de Cloud Computing",
                "https://via.placeholder.com/100x100/00BCD4/FFFFFF?text=CLOUD"
            );
            
            insigniaRepository.save(insignia1);
            insigniaRepository.save(insignia2);
            insigniaRepository.save(insignia3);
            insigniaRepository.save(insignia4);
            insigniaRepository.save(insignia5);
            
            return ResponseEntity.ok("Insignias de prueba inicializadas correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al inicializar insignias: " + e.getMessage());
        }
    }
    
    @PostMapping("/inicializar-progreso")
    @Operation(summary = "Inicializar progreso de prueba para usuario demo")
    public ResponseEntity<String> inicializarProgreso() {
        try {
            // Crear progreso de prueba para el usuario demo
            UUID usuarioId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
            
            // Crear algunos registros de progreso de prueba
            // Nota: En una implementación real, esto se haría a través del servicio
            return ResponseEntity.ok("Progreso de prueba inicializado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al inicializar progreso: " + e.getMessage());
        }
    }
}