package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.ProgramaCompletoDTO;
import com.stefanini.portal.capacitaciones.service.ProgramaCompletoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/programas-completos")
@CrossOrigin(origins = "*")
public class ProgramaCompletoController {
    
    @Autowired
    private ProgramaCompletoService programaCompletoService;
    
    @PostMapping
    public ResponseEntity<?> crearProgramaCompleto(@Valid @RequestBody ProgramaCompletoDTO programaCompletoDTO) {
        try {
            ProgramaCompletoDTO programaCreado = programaCompletoService.crearProgramaCompleto(programaCompletoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(programaCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de validación: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear programa: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ProgramaCompletoDTO>> obtenerTodosLosProgramasCompletos() {
        List<ProgramaCompletoDTO> programas = programaCompletoService.obtenerTodosLosProgramasCompletos();
        return ResponseEntity.ok(programas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProgramaCompletoDTO> obtenerProgramaCompletoPorId(@PathVariable Integer id) {
        Optional<ProgramaCompletoDTO> programa = programaCompletoService.obtenerProgramaCompletoPorId(id);
        return programa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProgramaCompleto(@PathVariable Integer id, 
                                                        @Valid @RequestBody ProgramaCompletoDTO programaCompletoDTO) {
        try {
            Optional<ProgramaCompletoDTO> programaActualizado = programaCompletoService.actualizarProgramaCompleto(id, programaCompletoDTO);
            return programaActualizado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de validación: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar programa: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProgramaCompleto(@PathVariable Integer id) {
        boolean eliminado = programaCompletoService.eliminarProgramaCompleto(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<ProgramaCompletoDTO>> buscarProgramasCompletosPorTitulo(@RequestParam String titulo) {
        List<ProgramaCompletoDTO> programas = programaCompletoService.buscarProgramasCompletosPorTitulo(titulo);
        return ResponseEntity.ok(programas);
    }
    
    @GetMapping("/buscar/area")
    public ResponseEntity<List<ProgramaCompletoDTO>> buscarProgramasCompletosPorArea(@RequestParam String area) {
        List<ProgramaCompletoDTO> programas = programaCompletoService.buscarProgramasCompletosPorArea(area);
        return ResponseEntity.ok(programas);
    }
    
    @GetMapping("/recientes")
    public ResponseEntity<List<ProgramaCompletoDTO>> obtenerProgramasCompletosRecientes() {
        List<ProgramaCompletoDTO> programas = programaCompletoService.obtenerProgramasCompletosRecientes();
        return ResponseEntity.ok(programas);
    }
}
