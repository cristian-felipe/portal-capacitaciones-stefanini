package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.ProgresoDTO;
import com.stefanini.portal.capacitaciones.dto.ProgresoProgramaDTO;
import com.stefanini.portal.capacitaciones.dto.UserStatsDTO;
import com.stefanini.portal.capacitaciones.service.ProgresoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/progreso")
@Tag(name = "Progreso", description = "API para gestión de progreso de usuarios")
@CrossOrigin(origins = "*")
public class ProgresoController {
    
    @Autowired
    private ProgresoService progresoService;
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener progreso de un usuario (detalle por lección)")
    public ResponseEntity<List<ProgresoDTO>> getProgresoByUsuarioId(@PathVariable UUID usuarioId) {
        try {
            List<ProgresoDTO> progreso = progresoService.getProgresoByUsuarioId(usuarioId);
            return ResponseEntity.ok(progreso);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/programas")
    @Operation(summary = "Obtener progreso de un usuario agrupado por programas")
    public ResponseEntity<List<ProgresoProgramaDTO>> getProgresoProgramasByUsuarioId(@PathVariable UUID usuarioId) {
        try {
            List<ProgresoProgramaDTO> progresoProgramas = progresoService.getProgresoProgramasByUsuarioId(usuarioId);
            return ResponseEntity.ok(progresoProgramas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Actualizar progreso de una lección")
    public ResponseEntity<ProgresoDTO> actualizarProgreso(@RequestBody Map<String, Object> request) {
        try {
            UUID usuarioId = UUID.fromString((String) request.get("usuario_id"));
            Integer leccionId = Integer.valueOf(request.get("leccion_id").toString());
            Double porcentaje = Double.valueOf(request.get("porcentaje").toString());
            
            ProgresoDTO progreso = progresoService.actualizarProgreso(usuarioId, leccionId, porcentaje);
            return ResponseEntity.ok(progreso);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/estadisticas")
    @Operation(summary = "Obtener estadísticas de un usuario")
    public ResponseEntity<UserStatsDTO> getEstadisticasUsuario(@PathVariable UUID usuarioId) {
        try {
            UserStatsDTO estadisticas = progresoService.getEstadisticasUsuario(usuarioId);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
