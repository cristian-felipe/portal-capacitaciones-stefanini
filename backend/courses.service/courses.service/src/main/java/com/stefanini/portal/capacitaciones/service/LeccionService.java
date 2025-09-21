package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.LeccionDTO;
import com.stefanini.portal.capacitaciones.entity.Leccion;
import com.stefanini.portal.capacitaciones.entity.Unidad;
import com.stefanini.portal.capacitaciones.repository.LeccionRepository;
import com.stefanini.portal.capacitaciones.repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeccionService {
    
    @Autowired
    private LeccionRepository leccionRepository;
    
    @Autowired
    private UnidadRepository unidadRepository;
    
    // Crear lección
    public LeccionDTO crearLeccion(LeccionDTO leccionDTO) {
        Unidad unidad = unidadRepository.findById(leccionDTO.getUnidadId())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
        
        Leccion leccion = new Leccion();
        leccion.setUnidad(unidad);
        leccion.setTitulo(leccionDTO.getTitulo());
        leccion.setOrden(leccionDTO.getOrden() != null ? leccionDTO.getOrden() : 
                leccionRepository.getNextOrden(leccionDTO.getUnidadId()));
        leccion.setTipoMaterial(leccionDTO.getTipoMaterial());
        leccion.setUrlMaterial(leccionDTO.getUrlMaterial());
        
        Leccion leccionGuardada = leccionRepository.save(leccion);
        return convertirADTO(leccionGuardada);
    }
    
    // Obtener todas las lecciones de una unidad
    @Transactional(readOnly = true)
    public List<LeccionDTO> obtenerLeccionesPorUnidad(Integer unidadId) {
        return leccionRepository.findByUnidadIdOrderByOrden(unidadId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener lección por ID
    @Transactional(readOnly = true)
    public Optional<LeccionDTO> obtenerLeccionPorId(Integer id) {
        return leccionRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    // Obtener lección completa con unidad y programa
    @Transactional(readOnly = true)
    public Optional<LeccionDTO> obtenerLeccionCompleta(Integer id) {
        return leccionRepository.findByIdWithUnidadAndPrograma(id)
                .map(this::convertirADTOCompleto);
    }
    
    // Actualizar lección
    public Optional<LeccionDTO> actualizarLeccion(Integer id, LeccionDTO leccionDTO) {
        return leccionRepository.findById(id)
                .map(leccion -> {
                    leccion.setTitulo(leccionDTO.getTitulo());
                    leccion.setOrden(leccionDTO.getOrden());
                    leccion.setTipoMaterial(leccionDTO.getTipoMaterial());
                    leccion.setUrlMaterial(leccionDTO.getUrlMaterial());
                    
                    Leccion leccionActualizada = leccionRepository.save(leccion);
                    return convertirADTO(leccionActualizada);
                });
    }
    
    // Eliminar lección
    public boolean eliminarLeccion(Integer id) {
        if (leccionRepository.existsById(id)) {
            leccionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Buscar lecciones por tipo de material
    @Transactional(readOnly = true)
    public List<LeccionDTO> buscarPorTipoMaterial(String tipoMaterial) {
        return leccionRepository.findByTipoMaterial(tipoMaterial).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Buscar lecciones por título
    @Transactional(readOnly = true)
    public List<LeccionDTO> buscarPorTitulo(String titulo) {
        return leccionRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener lecciones por programa
    @Transactional(readOnly = true)
    public List<LeccionDTO> obtenerLeccionesPorPrograma(Integer programaId) {
        return leccionRepository.findByProgramaId(programaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Contar lecciones por unidad
    @Transactional(readOnly = true)
    public Long contarLeccionesPorUnidad(Integer unidadId) {
        return leccionRepository.countByUnidadId(unidadId);
    }
    
    // Contar lecciones por tipo de material
    @Transactional(readOnly = true)
    public Long contarLeccionesPorTipoMaterial(String tipoMaterial) {
        return leccionRepository.countByTipoMaterial(tipoMaterial);
    }
    
    // Verificar si existe lección con título en unidad
    @Transactional(readOnly = true)
    public boolean existeLeccionConTituloEnUnidad(String titulo, Integer unidadId) {
        return leccionRepository.existsByTituloIgnoreCaseAndUnidadId(titulo, unidadId);
    }
    
    // Reordenar lecciones
    public List<LeccionDTO> reordenarLecciones(Integer unidadId, List<Integer> idsOrdenados) {
        List<Leccion> lecciones = leccionRepository.findByUnidadIdOrderByOrden(unidadId);
        
        for (int i = 0; i < idsOrdenados.size(); i++) {
            final int orden = i + 1;
            Integer id = idsOrdenados.get(i);
            lecciones.stream()
                    .filter(l -> l.getId().equals(id))
                    .findFirst()
                    .ifPresent(l -> l.setOrden(orden));
        }
        
        List<Leccion> leccionesActualizadas = leccionRepository.saveAll(lecciones);
        return leccionesActualizadas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversión
    private LeccionDTO convertirADTO(Leccion leccion) {
        LeccionDTO dto = new LeccionDTO();
        dto.setId(leccion.getId());
        dto.setUnidadId(leccion.getUnidad().getId());
        dto.setTitulo(leccion.getTitulo());
        dto.setOrden(leccion.getOrden());
        dto.setTipoMaterial(leccion.getTipoMaterial());
        dto.setUrlMaterial(leccion.getUrlMaterial());
        return dto;
    }
    
    private LeccionDTO convertirADTOCompleto(Leccion leccion) {
        return convertirADTO(leccion);
    }
}
