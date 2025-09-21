package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.LeccionDTO;
import com.stefanini.portal.capacitaciones.service.LeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecciones")
@CrossOrigin(origins = "*")
public class LeccionController {
    
    @Autowired
    private LeccionService leccionService;
    
    // Crear lección
    @PostMapping
    public ResponseEntity<LeccionDTO> crearLeccion(@Valid @RequestBody LeccionDTO leccionDTO) {
        try {
            LeccionDTO leccionCreada = leccionService.crearLeccion(leccionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(leccionCreada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Obtener todas las lecciones de una unidad
    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<List<LeccionDTO>> obtenerLeccionesPorUnidad(@PathVariable Integer unidadId) {
        List<LeccionDTO> lecciones = leccionService.obtenerLeccionesPorUnidad(unidadId);
        return ResponseEntity.ok(lecciones);
    }
    
    // Obtener lección por ID
    @GetMapping("/{id}")
    public ResponseEntity<LeccionDTO> obtenerLeccionPorId(@PathVariable Integer id) {
        Optional<LeccionDTO> leccion = leccionService.obtenerLeccionPorId(id);
        return leccion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtener lección completa con unidad y programa
    @GetMapping("/{id}/completo")
    public ResponseEntity<LeccionDTO> obtenerLeccionCompleta(@PathVariable Integer id) {
        Optional<LeccionDTO> leccion = leccionService.obtenerLeccionCompleta(id);
        return leccion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar lección
    @PutMapping("/{id}")
    public ResponseEntity<LeccionDTO> actualizarLeccion(@PathVariable Integer id, 
                                                        @Valid @RequestBody LeccionDTO leccionDTO) {
        Optional<LeccionDTO> leccionActualizada = leccionService.actualizarLeccion(id, leccionDTO);
        return leccionActualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar lección
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLeccion(@PathVariable Integer id) {
        boolean eliminado = leccionService.eliminarLeccion(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // Buscar lecciones por tipo de material
    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<LeccionDTO>> buscarPorTipoMaterial(@RequestParam String tipo) {
        List<LeccionDTO> lecciones = leccionService.buscarPorTipoMaterial(tipo);
        return ResponseEntity.ok(lecciones);
    }
    
    // Buscar lecciones por título
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<LeccionDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<LeccionDTO> lecciones = leccionService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(lecciones);
    }
    
    // Obtener lecciones por programa
    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<LeccionDTO>> obtenerLeccionesPorPrograma(@PathVariable Integer programaId) {
        List<LeccionDTO> lecciones = leccionService.obtenerLeccionesPorPrograma(programaId);
        return ResponseEntity.ok(lecciones);
    }
    
    // Contar lecciones por unidad
    @GetMapping("/unidad/{unidadId}/contar")
    public ResponseEntity<Long> contarLeccionesPorUnidad(@PathVariable Integer unidadId) {
        Long cantidad = leccionService.contarLeccionesPorUnidad(unidadId);
        return ResponseEntity.ok(cantidad);
    }
    
    // Contar lecciones por tipo de material
    @GetMapping("/estadisticas/tipo")
    public ResponseEntity<Long> contarLeccionesPorTipoMaterial(@RequestParam String tipo) {
        Long cantidad = leccionService.contarLeccionesPorTipoMaterial(tipo);
        return ResponseEntity.ok(cantidad);
    }
    
    // Verificar si existe lección con título en unidad
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeLeccionConTituloEnUnidad(@RequestParam String titulo, 
                                                                  @RequestParam Integer unidadId) {
        boolean existe = leccionService.existeLeccionConTituloEnUnidad(titulo, unidadId);
        return ResponseEntity.ok(existe);
    }
    
    // Reordenar lecciones
    @PutMapping("/unidad/{unidadId}/reordenar")
    public ResponseEntity<List<LeccionDTO>> reordenarLecciones(@PathVariable Integer unidadId,
                                                               @RequestBody List<Integer> idsOrdenados) {
        try {
            List<LeccionDTO> leccionesReordenadas = leccionService.reordenarLecciones(unidadId, idsOrdenados);
            return ResponseEntity.ok(leccionesReordenadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


