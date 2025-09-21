package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.ProgramaDTO;
import com.stefanini.portal.capacitaciones.service.ProgramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/programas")
@CrossOrigin(origins = "*")
public class ProgramaController {
    
    @Autowired
    private ProgramaService programaService;
    
    // Crear programa
    @PostMapping
    public ResponseEntity<ProgramaDTO> crearPrograma(@Valid @RequestBody ProgramaDTO programaDTO) {
        try {
            ProgramaDTO programaCreado = programaService.crearPrograma(programaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(programaCreado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Obtener todos los programas
    @GetMapping
    public ResponseEntity<List<ProgramaDTO>> obtenerTodosLosProgramas() {
        List<ProgramaDTO> programas = programaService.obtenerTodosLosProgramas();
        return ResponseEntity.ok(programas);
    }
    
    // Obtener programa por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProgramaDTO> obtenerProgramaPorId(@PathVariable Integer id) {
        Optional<ProgramaDTO> programa = programaService.obtenerProgramaPorId(id);
        return programa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtener programa completo con unidades y lecciones
    @GetMapping("/{id}/completo")
    public ResponseEntity<ProgramaDTO> obtenerProgramaCompleto(@PathVariable Integer id) {
        Optional<ProgramaDTO> programa = programaService.obtenerProgramaCompleto(id);
        return programa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar programa
    @PutMapping("/{id}")
    public ResponseEntity<ProgramaDTO> actualizarPrograma(@PathVariable Integer id, 
                                                         @Valid @RequestBody ProgramaDTO programaDTO) {
        Optional<ProgramaDTO> programaActualizado = programaService.actualizarPrograma(id, programaDTO);
        return programaActualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar programa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrograma(@PathVariable Integer id) {
        boolean eliminado = programaService.eliminarPrograma(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // Buscar programas por área de conocimiento
    @GetMapping("/buscar/area")
    public ResponseEntity<List<ProgramaDTO>> buscarPorAreaConocimiento(@RequestParam String area) {
        List<ProgramaDTO> programas = programaService.buscarPorAreaConocimiento(area);
        return ResponseEntity.ok(programas);
    }
    
    // Buscar programas por título
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<ProgramaDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<ProgramaDTO> programas = programaService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(programas);
    }
    
    // Obtener programas recientes
    @GetMapping("/recientes")
    public ResponseEntity<List<ProgramaDTO>> obtenerProgramasRecientes() {
        List<ProgramaDTO> programas = programaService.obtenerProgramasRecientes();
        return ResponseEntity.ok(programas);
    }
    
    // Verificar si existe programa con título
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeProgramaConTitulo(@RequestParam String titulo) {
        boolean existe = programaService.existeProgramaConTitulo(titulo);
        return ResponseEntity.ok(existe);
    }
    
    // Contar programas por área de conocimiento
    @GetMapping("/estadisticas/areas")
    public ResponseEntity<List<Object[]>> contarPorAreaConocimiento() {
        List<Object[]> estadisticas = programaService.contarPorAreaConocimiento();
        return ResponseEntity.ok(estadisticas);
    }
}


