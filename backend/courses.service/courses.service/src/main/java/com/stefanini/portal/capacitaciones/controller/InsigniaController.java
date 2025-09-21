package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.InsigniaDTO;
import com.stefanini.portal.capacitaciones.dto.InsigniaOtorgadaDTO;
import com.stefanini.portal.capacitaciones.service.InsigniaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/insignias")
@Tag(name = "Insignias", description = "API para gestión de insignias")
@CrossOrigin(origins = "*")
public class InsigniaController {
    
    @Autowired
    private InsigniaService insigniaService;
    
    @GetMapping
    @Operation(summary = "Obtener todas las insignias disponibles")
    public ResponseEntity<List<InsigniaDTO>> getAllInsignias() {
        try {
            List<InsigniaDTO> insignias = insigniaService.getAllInsignias();
            return ResponseEntity.ok(insignias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener insignias de un usuario")
    public ResponseEntity<List<InsigniaOtorgadaDTO>> getInsigniasByUsuarioId(@PathVariable UUID usuarioId) {
        try {
            List<InsigniaOtorgadaDTO> insignias = insigniaService.getInsigniasByUsuarioId(usuarioId);
            return ResponseEntity.ok(insignias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva insignia")
    public ResponseEntity<InsigniaDTO> crearInsignia(@RequestBody InsigniaDTO insigniaDTO) {
        try {
            InsigniaDTO insignia = insigniaService.crearInsignia(insigniaDTO);
            return ResponseEntity.ok(insignia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/otorgar")
    @Operation(summary = "Otorgar insignia a usuario")
    public ResponseEntity<InsigniaOtorgadaDTO> otorgarInsignia(@RequestBody Map<String, Object> request) {
        try {
            UUID usuarioId = UUID.fromString((String) request.get("usuario_id"));
            Long insigniaId = Long.valueOf(request.get("insignia_id").toString());
            
            InsigniaOtorgadaDTO insigniaOtorgada = insigniaService.otorgarInsignia(usuarioId, insigniaId);
            return ResponseEntity.ok(insigniaOtorgada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

