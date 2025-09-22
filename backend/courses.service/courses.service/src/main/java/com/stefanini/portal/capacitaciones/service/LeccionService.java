package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.LeccionDTO;
import com.stefanini.portal.capacitaciones.entity.Leccion;
import com.stefanini.portal.capacitaciones.entity.Unidad;
import com.stefanini.portal.capacitaciones.entity.Material;
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
    
    @Transactional(readOnly = true)
    public List<LeccionDTO> obtenerLeccionesPorUnidad(Integer unidadId) {
        return leccionRepository.findByUnidadIdOrderByOrden(unidadId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<LeccionDTO> obtenerLeccionPorId(Integer id) {
        return leccionRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<LeccionDTO> obtenerLeccionCompleta(Integer id) {
        return leccionRepository.findByIdWithUnidadAndPrograma(id)
                .map(this::convertirADTO);
    }
    
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
    
    public boolean eliminarLeccion(Integer id) {
        if (leccionRepository.existsById(id)) {
            leccionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = true)
    public List<LeccionDTO> buscarPorTipoMaterial(String tipoMaterial) {
        return leccionRepository.findByTipoMaterial(tipoMaterial).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<LeccionDTO> buscarPorTitulo(String titulo) {
        return leccionRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<LeccionDTO> obtenerLeccionesPorPrograma(Integer programaId) {
        return leccionRepository.findByProgramaId(programaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Long contarLeccionesPorUnidad(Integer unidadId) {
        return leccionRepository.countByUnidadId(unidadId);
    }
    
    @Transactional(readOnly = true)
    public Long contarLeccionesPorTipoMaterial(String tipoMaterial) {
        return leccionRepository.countByTipoMaterial(tipoMaterial);
    }
    
    @Transactional(readOnly = true)
    public boolean existeLeccionConTituloEnUnidad(String titulo, Integer unidadId) {
        return leccionRepository.existsByTituloIgnoreCaseAndUnidadId(titulo, unidadId);
    }
    
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
    
    
    @Transactional(readOnly = true)
    public List<LeccionDTO> obtenerLeccionesCompletasPorUnidad(Integer unidadId) {
        return leccionRepository.findByUnidadIdOrderByOrden(unidadId).stream()
                .map(this::convertirADTOConMaterial)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<LeccionDTO> obtenerLeccionCompletaConMaterial(Integer id) {
        return leccionRepository.findById(id)
                .map(this::convertirADTOConMaterial);
    }
    
    private LeccionDTO convertirADTOConMaterial(Leccion leccion) {
        LeccionDTO dto = convertirADTO(leccion);
        
        // Si la lección tiene material asociado, agregar información completa
        if (leccion.getMaterial() != null) {
            Material material = leccion.getMaterial();
            dto.setMaterialId(material.getId());
            dto.setMaterialNombreOriginal(material.getNombreOriginal());
            dto.setMaterialTipoMaterial(material.getTipoMaterial());
            dto.setMaterialExtension(material.getExtension());
            dto.setMaterialTamañoBytes(material.getTamañoBytes());
            dto.setMaterialTamañoFormateado(material.getTamañoFormateado());
            dto.setMaterialUrlAcceso(material.getUrlAcceso());
            dto.setMaterialDescripcion(material.getDescripcion());
            dto.setMaterialS3Uploaded(material.getS3Uploaded());
            dto.setMaterialS3Key(material.getS3Key());
        }
        
        return dto;
    }
}
