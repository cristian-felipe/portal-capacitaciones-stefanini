package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.ProgramaCompletoDTO;
import com.stefanini.portal.capacitaciones.entity.Programa;
import com.stefanini.portal.capacitaciones.entity.Unidad;
import com.stefanini.portal.capacitaciones.entity.Leccion;
import com.stefanini.portal.capacitaciones.entity.Material;
import com.stefanini.portal.capacitaciones.repository.ProgramaRepository;
import com.stefanini.portal.capacitaciones.repository.UnidadRepository;
import com.stefanini.portal.capacitaciones.repository.LeccionRepository;
import com.stefanini.portal.capacitaciones.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgramaCompletoService {
    
    @Autowired
    private ProgramaRepository programaRepository;
    
    @Autowired
    private UnidadRepository unidadRepository;
    
    @Autowired
    private LeccionRepository leccionRepository;
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ProgramaCompletoDTO crearProgramaCompleto(ProgramaCompletoDTO programaCompletoDTO) {
        try {
            // Validaciones previas
            validarProgramaCompletoDTO(programaCompletoDTO);
            
            // 1. Crear el programa
            Programa programa = new Programa();
            programa.setTitulo(programaCompletoDTO.getTitulo());
            programa.setDescripcion(programaCompletoDTO.getDescripcion());
            programa.setAreaConocimiento(programaCompletoDTO.getAreaConocimiento());
            
            Programa programaGuardado = programaRepository.save(programa);
            
            // 2. Crear las unidades y lecciones
            if (programaCompletoDTO.getUnidades() != null && !programaCompletoDTO.getUnidades().isEmpty()) {
                for (ProgramaCompletoDTO.UnidadCompletaDTO unidadDTO : programaCompletoDTO.getUnidades()) {
                    // Validar unidad
                    validarUnidadDTO(unidadDTO);
                    
                    Unidad unidad = new Unidad();
                    unidad.setPrograma(programaGuardado);
                    unidad.setTitulo(unidadDTO.getTitulo());
                    unidad.setOrden(unidadDTO.getOrden() != null ? unidadDTO.getOrden() : 
                            unidadRepository.getNextOrden(programaGuardado.getId()));
                    
                    Unidad unidadGuardada = unidadRepository.save(unidad);
                    
                    // 3. Crear las lecciones de cada unidad
                    if (unidadDTO.getLecciones() != null && !unidadDTO.getLecciones().isEmpty()) {
                        for (ProgramaCompletoDTO.LeccionCompletaDTO leccionDTO : unidadDTO.getLecciones()) {
                            // Validar lección
                            validarLeccionDTO(leccionDTO);
                            
                            Leccion leccion = new Leccion();
                            leccion.setUnidad(unidadGuardada);
                            leccion.setTitulo(leccionDTO.getTitulo());
                            leccion.setOrden(leccionDTO.getOrden() != null ? leccionDTO.getOrden() : 
                                    leccionRepository.getNextOrden(unidadGuardada.getId()));
                            leccion.setTipoMaterial(leccionDTO.getTipoMaterial());
                            leccion.setUrlMaterial(leccionDTO.getUrlMaterial());
                            
                            // Asociar material si existe
                            if (leccionDTO.getMaterialId() != null) {
                                Material material = materialRepository.findById(leccionDTO.getMaterialId()).orElse(null);
                                leccion.setMaterial(material);
                            }
                            
                            leccionRepository.save(leccion);
                        }
                    }
                }
            }
            
            // 4. Retornar el programa completo creado
            return obtenerProgramaCompletoPorId(programaGuardado.getId())
                    .orElseThrow(() -> new RuntimeException("Error al recuperar el programa creado"));
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al crear programa completo: " + e.getMessage(), e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<ProgramaCompletoDTO> obtenerTodosLosProgramasCompletos() {
        return programaRepository.findAllWithUnidades().stream()
                .map(this::cargarProgramaCompleto)
                .map(this::convertirAProgramaCompletoDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<ProgramaCompletoDTO> obtenerProgramaCompletoPorId(Integer id) {
        return programaRepository.findByIdWithUnidades(id)
                .map(this::cargarProgramaCompleto)
                .map(this::convertirAProgramaCompletoDTO);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Optional<ProgramaCompletoDTO> actualizarProgramaCompleto(Integer id, ProgramaCompletoDTO programaCompletoDTO) {
        try {
            // Validaciones previas
            validarProgramaCompletoDTO(programaCompletoDTO);
            
            return programaRepository.findById(id)
                    .map(programa -> {
                        // 1. Actualizar datos del programa
                        programa.setTitulo(programaCompletoDTO.getTitulo());
                        programa.setDescripcion(programaCompletoDTO.getDescripcion());
                        programa.setAreaConocimiento(programaCompletoDTO.getAreaConocimiento());
                        
                        // 2. Eliminar unidades y lecciones existentes (cascade delete)
                        programa.getUnidades().clear();
                        programaRepository.save(programa);
                        
                        // 3. Crear nuevas unidades y lecciones
                        if (programaCompletoDTO.getUnidades() != null && !programaCompletoDTO.getUnidades().isEmpty()) {
                            for (ProgramaCompletoDTO.UnidadCompletaDTO unidadDTO : programaCompletoDTO.getUnidades()) {
                                // Validar unidad
                                validarUnidadDTO(unidadDTO);
                                
                                Unidad unidad = new Unidad();
                                unidad.setPrograma(programa);
                                unidad.setTitulo(unidadDTO.getTitulo());
                                unidad.setOrden(unidadDTO.getOrden() != null ? unidadDTO.getOrden() : 
                                        unidadRepository.getNextOrden(programa.getId()));
                                
                                Unidad unidadGuardada = unidadRepository.save(unidad);
                                
                                // Crear lecciones de la unidad
                                if (unidadDTO.getLecciones() != null && !unidadDTO.getLecciones().isEmpty()) {
                                    for (ProgramaCompletoDTO.LeccionCompletaDTO leccionDTO : unidadDTO.getLecciones()) {
                                        // Validar lección
                                        validarLeccionDTO(leccionDTO);
                                        
                                        Leccion leccion = new Leccion();
                                        leccion.setUnidad(unidadGuardada);
                                        leccion.setTitulo(leccionDTO.getTitulo());
                                        leccion.setOrden(leccionDTO.getOrden() != null ? leccionDTO.getOrden() : 
                                                leccionRepository.getNextOrden(unidadGuardada.getId()));
                                        leccion.setTipoMaterial(leccionDTO.getTipoMaterial());
                                        leccion.setUrlMaterial(leccionDTO.getUrlMaterial());
                                        
                                        leccionRepository.save(leccion);
                                    }
                                }
                            }
                        }
                        
                        Programa programaActualizado = programaRepository.save(programa);
                        return convertirAProgramaCompletoDTO(programaActualizado);
                    });
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar programa completo: " + e.getMessage(), e);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarProgramaCompleto(Integer id) {
        if (programaRepository.existsById(id)) {
            programaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = true)
    public List<ProgramaCompletoDTO> buscarProgramasCompletosPorTitulo(String titulo) {
        return programaRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::cargarProgramaCompleto)
                .map(this::convertirAProgramaCompletoDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProgramaCompletoDTO> buscarProgramasCompletosPorArea(String areaConocimiento) {
        return programaRepository.findByAreaConocimiento(areaConocimiento).stream()
                .map(this::cargarProgramaCompleto)
                .map(this::convertirAProgramaCompletoDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProgramaCompletoDTO> obtenerProgramasCompletosRecientes() {
        return programaRepository.findRecentPrograms().stream()
                .map(this::cargarProgramaCompleto)
                .map(this::convertirAProgramaCompletoDTO)
                .collect(Collectors.toList());
    }
    
    // Método auxiliar para cargar programa completo con unidades y lecciones
    private Programa cargarProgramaCompleto(Programa programa) {
        // Cargar unidades y lecciones por separado para evitar MultipleBagFetchException
        programa.setUnidades(programaRepository.findByIdWithUnidades(programa.getId())
                .map(Programa::getUnidades)
                .orElse(new ArrayList<>()));
        
        programa.getUnidades().forEach(unidad -> {
            unidad.setLecciones(unidadRepository.findByIdWithLecciones(unidad.getId())
                    .map(Unidad::getLecciones)
                    .orElse(new ArrayList<>()));
        });
        
        return programa;
    }
    
    private ProgramaCompletoDTO convertirAProgramaCompletoDTO(Programa programa) {
        ProgramaCompletoDTO dto = new ProgramaCompletoDTO();
        dto.setId(programa.getId());
        dto.setTitulo(programa.getTitulo());
        dto.setDescripcion(programa.getDescripcion());
        dto.setAreaConocimiento(programa.getAreaConocimiento());
        dto.setFechaCreacion(programa.getFechaCreacion());
        
        // Convertir unidades
        if (programa.getUnidades() != null) {
            List<ProgramaCompletoDTO.UnidadCompletaDTO> unidadesDTO = programa.getUnidades().stream()
                    .map(this::convertirAUnidadCompletaDTO)
                    .collect(Collectors.toList());
            dto.setUnidades(unidadesDTO);
        }
        
        return dto;
    }
    
    private ProgramaCompletoDTO.UnidadCompletaDTO convertirAUnidadCompletaDTO(Unidad unidad) {
        ProgramaCompletoDTO.UnidadCompletaDTO dto = new ProgramaCompletoDTO.UnidadCompletaDTO();
        dto.setId(unidad.getId());
        dto.setTitulo(unidad.getTitulo());
        dto.setOrden(unidad.getOrden());
        
        // Convertir lecciones
        if (unidad.getLecciones() != null) {
            List<ProgramaCompletoDTO.LeccionCompletaDTO> leccionesDTO = unidad.getLecciones().stream()
                    .map(this::convertirALeccionCompletaDTO)
                    .collect(Collectors.toList());
            dto.setLecciones(leccionesDTO);
        }
        
        return dto;
    }
    
    private ProgramaCompletoDTO.LeccionCompletaDTO convertirALeccionCompletaDTO(Leccion leccion) {
        ProgramaCompletoDTO.LeccionCompletaDTO dto = new ProgramaCompletoDTO.LeccionCompletaDTO();
        dto.setId(leccion.getId());
        dto.setTitulo(leccion.getTitulo());
        dto.setOrden(leccion.getOrden());
        dto.setTipoMaterial(leccion.getTipoMaterial());
        dto.setUrlMaterial(leccion.getUrlMaterial());
        
        // Incluir materialId si existe
        if (leccion.getMaterial() != null) {
            dto.setMaterialId(leccion.getMaterial().getId());
        }
        
        return dto;
    }
    
    private void validarProgramaCompletoDTO(ProgramaCompletoDTO programaDTO) {
        if (programaDTO == null) {
            throw new IllegalArgumentException("El programa no puede ser nulo");
        }
        if (programaDTO.getTitulo() == null || programaDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del programa es requerido");
        }
        if (programaDTO.getTitulo().length() > 255) {
            throw new IllegalArgumentException("El título del programa no puede exceder 255 caracteres");
        }
        if (programaDTO.getAreaConocimiento() != null && programaDTO.getAreaConocimiento().length() > 100) {
            throw new IllegalArgumentException("El área de conocimiento no puede exceder 100 caracteres");
        }
    }
    
    private void validarUnidadDTO(ProgramaCompletoDTO.UnidadCompletaDTO unidadDTO) {
        if (unidadDTO == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula");
        }
        if (unidadDTO.getTitulo() == null || unidadDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la unidad es requerido");
        }
        if (unidadDTO.getTitulo().length() > 255) {
            throw new IllegalArgumentException("El título de la unidad no puede exceder 255 caracteres");
        }
    }
    
    private void validarLeccionDTO(ProgramaCompletoDTO.LeccionCompletaDTO leccionDTO) {
        if (leccionDTO == null) {
            throw new IllegalArgumentException("La lección no puede ser nula");
        }
        if (leccionDTO.getTitulo() == null || leccionDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la lección es requerido");
        }
        if (leccionDTO.getTitulo().length() > 255) {
            throw new IllegalArgumentException("El título de la lección no puede exceder 255 caracteres");
        }
        if (leccionDTO.getTipoMaterial() != null && leccionDTO.getTipoMaterial().length() > 50) {
            throw new IllegalArgumentException("El tipo de material no puede exceder 50 caracteres");
        }
    }
}
