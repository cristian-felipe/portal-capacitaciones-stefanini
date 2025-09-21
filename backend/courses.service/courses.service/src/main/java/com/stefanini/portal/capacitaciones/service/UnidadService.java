package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.UnidadDTO;
import com.stefanini.portal.capacitaciones.entity.Programa;
import com.stefanini.portal.capacitaciones.entity.Unidad;
import com.stefanini.portal.capacitaciones.repository.ProgramaRepository;
import com.stefanini.portal.capacitaciones.repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnidadService {
    
    @Autowired
    private UnidadRepository unidadRepository;
    
    @Autowired
    private ProgramaRepository programaRepository;
    
    // Crear unidad
    public UnidadDTO crearUnidad(UnidadDTO unidadDTO) {
        Programa programa = programaRepository.findById(unidadDTO.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no encontrado"));
        
        Unidad unidad = new Unidad();
        unidad.setPrograma(programa);
        unidad.setTitulo(unidadDTO.getTitulo());
        unidad.setOrden(unidadDTO.getOrden() != null ? unidadDTO.getOrden() : 
                unidadRepository.getNextOrden(unidadDTO.getProgramaId()));
        
        Unidad unidadGuardada = unidadRepository.save(unidad);
        return convertirADTO(unidadGuardada);
    }
    
    // Obtener todas las unidades de un programa
    @Transactional(readOnly = true)
    public List<UnidadDTO> obtenerUnidadesPorPrograma(Integer programaId) {
        return unidadRepository.findByProgramaIdOrderByOrden(programaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener unidad por ID
    @Transactional(readOnly = true)
    public Optional<UnidadDTO> obtenerUnidadPorId(Integer id) {
        return unidadRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    // Obtener unidad completa con lecciones
    @Transactional(readOnly = true)
    public Optional<UnidadDTO> obtenerUnidadCompleta(Integer id) {
        return unidadRepository.findByIdWithLecciones(id)
                .map(this::convertirADTOCompleto);
    }
    
    // Actualizar unidad
    public Optional<UnidadDTO> actualizarUnidad(Integer id, UnidadDTO unidadDTO) {
        return unidadRepository.findById(id)
                .map(unidad -> {
                    unidad.setTitulo(unidadDTO.getTitulo());
                    unidad.setOrden(unidadDTO.getOrden());
                    
                    Unidad unidadActualizada = unidadRepository.save(unidad);
                    return convertirADTO(unidadActualizada);
                });
    }
    
    // Eliminar unidad
    public boolean eliminarUnidad(Integer id) {
        if (unidadRepository.existsById(id)) {
            unidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Buscar unidades por título
    @Transactional(readOnly = true)
    public List<UnidadDTO> buscarPorTitulo(String titulo) {
        return unidadRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Contar unidades por programa
    @Transactional(readOnly = true)
    public Long contarUnidadesPorPrograma(Integer programaId) {
        return unidadRepository.countByProgramaId(programaId);
    }
    
    // Verificar si existe unidad con título en programa
    @Transactional(readOnly = true)
    public boolean existeUnidadConTituloEnPrograma(String titulo, Integer programaId) {
        return unidadRepository.existsByTituloIgnoreCaseAndProgramaId(titulo, programaId);
    }
    
    // Reordenar unidades
    public List<UnidadDTO> reordenarUnidades(Integer programaId, List<Integer> idsOrdenados) {
        List<Unidad> unidades = unidadRepository.findByProgramaIdOrderByOrden(programaId);
        
        for (int i = 0; i < idsOrdenados.size(); i++) {
            final int orden = i + 1;
            Integer id = idsOrdenados.get(i);
            unidades.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .ifPresent(u -> u.setOrden(orden));
        }
        
        List<Unidad> unidadesActualizadas = unidadRepository.saveAll(unidades);
        return unidadesActualizadas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversión
    private UnidadDTO convertirADTO(Unidad unidad) {
        UnidadDTO dto = new UnidadDTO();
        dto.setId(unidad.getId());
        dto.setProgramaId(unidad.getPrograma().getId());
        dto.setTitulo(unidad.getTitulo());
        dto.setOrden(unidad.getOrden());
        return dto;
    }
    
    private UnidadDTO convertirADTOCompleto(Unidad unidad) {
        UnidadDTO dto = convertirADTO(unidad);
        // Aquí se pueden agregar las lecciones si es necesario
        return dto;
    }
}
