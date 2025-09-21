package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.UnidadDTO;
import com.stefanini.portal.capacitaciones.service.UnidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unidades")
@CrossOrigin(origins = "*")
public class UnidadController {
    
    @Autowired
    private UnidadService unidadService;
    
    // Crear unidad
    @PostMapping
    public ResponseEntity<UnidadDTO> crearUnidad(@Valid @RequestBody UnidadDTO unidadDTO) {
        try {
            UnidadDTO unidadCreada = unidadService.crearUnidad(unidadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(unidadCreada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Obtener todas las unidades de un programa
    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<UnidadDTO>> obtenerUnidadesPorPrograma(@PathVariable Integer programaId) {
        List<UnidadDTO> unidades = unidadService.obtenerUnidadesPorPrograma(programaId);
        return ResponseEntity.ok(unidades);
    }
    
    // Obtener unidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadDTO> obtenerUnidadPorId(@PathVariable Integer id) {
        Optional<UnidadDTO> unidad = unidadService.obtenerUnidadPorId(id);
        return unidad.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtener unidad completa con lecciones
    @GetMapping("/{id}/completo")
    public ResponseEntity<UnidadDTO> obtenerUnidadCompleta(@PathVariable Integer id) {
        Optional<UnidadDTO> unidad = unidadService.obtenerUnidadCompleta(id);
        return unidad.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar unidad
    @PutMapping("/{id}")
    public ResponseEntity<UnidadDTO> actualizarUnidad(@PathVariable Integer id, 
                                                      @Valid @RequestBody UnidadDTO unidadDTO) {
        Optional<UnidadDTO> unidadActualizada = unidadService.actualizarUnidad(id, unidadDTO);
        return unidadActualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar unidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        boolean eliminado = unidadService.eliminarUnidad(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // Buscar unidades por título
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<UnidadDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<UnidadDTO> unidades = unidadService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(unidades);
    }
    
    // Contar unidades por programa
    @GetMapping("/programa/{programaId}/contar")
    public ResponseEntity<Long> contarUnidadesPorPrograma(@PathVariable Integer programaId) {
        Long cantidad = unidadService.contarUnidadesPorPrograma(programaId);
        return ResponseEntity.ok(cantidad);
    }
    
    // Verificar si existe unidad con título en programa
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeUnidadConTituloEnPrograma(@RequestParam String titulo, 
                                                                   @RequestParam Integer programaId) {
        boolean existe = unidadService.existeUnidadConTituloEnPrograma(titulo, programaId);
        return ResponseEntity.ok(existe);
    }
    
    // Reordenar unidades
    @PutMapping("/programa/{programaId}/reordenar")
    public ResponseEntity<List<UnidadDTO>> reordenarUnidades(@PathVariable Integer programaId,
                                                             @RequestBody List<Integer> idsOrdenados) {
        try {
            List<UnidadDTO> unidadesReordenadas = unidadService.reordenarUnidades(programaId, idsOrdenados);
            return ResponseEntity.ok(unidadesReordenadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


