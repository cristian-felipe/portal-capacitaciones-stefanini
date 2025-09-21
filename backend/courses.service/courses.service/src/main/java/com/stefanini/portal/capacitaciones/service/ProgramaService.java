package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.ProgramaDTO;
import com.stefanini.portal.capacitaciones.entity.Programa;
import com.stefanini.portal.capacitaciones.repository.ProgramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgramaService {
    
    @Autowired
    private ProgramaRepository programaRepository;
    
    // Crear programa
    public ProgramaDTO crearPrograma(ProgramaDTO programaDTO) {
        Programa programa = new Programa();
        programa.setTitulo(programaDTO.getTitulo());
        programa.setDescripcion(programaDTO.getDescripcion());
        programa.setAreaConocimiento(programaDTO.getAreaConocimiento());
        
        Programa programaGuardado = programaRepository.save(programa);
        return convertirADTO(programaGuardado);
    }
    
    // Obtener todos los programas
    @Transactional(readOnly = true)
    public List<ProgramaDTO> obtenerTodosLosProgramas() {
        return programaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener programa por ID
    @Transactional(readOnly = true)
    public Optional<ProgramaDTO> obtenerProgramaPorId(Integer id) {
        return programaRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    // Obtener programa completo con unidades y lecciones
    @Transactional(readOnly = true)
    public Optional<ProgramaDTO> obtenerProgramaCompleto(Integer id) {
        return programaRepository.findByIdWithUnidadesAndLecciones(id)
                .map(this::convertirADTOCompleto);
    }
    
    // Actualizar programa
    public Optional<ProgramaDTO> actualizarPrograma(Integer id, ProgramaDTO programaDTO) {
        return programaRepository.findById(id)
                .map(programa -> {
                    programa.setTitulo(programaDTO.getTitulo());
                    programa.setDescripcion(programaDTO.getDescripcion());
                    programa.setAreaConocimiento(programaDTO.getAreaConocimiento());
                    
                    Programa programaActualizado = programaRepository.save(programa);
                    return convertirADTO(programaActualizado);
                });
    }
    
    // Eliminar programa
    public boolean eliminarPrograma(Integer id) {
        if (programaRepository.existsById(id)) {
            programaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Buscar programas por área de conocimiento
    @Transactional(readOnly = true)
    public List<ProgramaDTO> buscarPorAreaConocimiento(String areaConocimiento) {
        return programaRepository.findByAreaConocimiento(areaConocimiento).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Buscar programas por título
    @Transactional(readOnly = true)
    public List<ProgramaDTO> buscarPorTitulo(String titulo) {
        return programaRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener programas recientes
    @Transactional(readOnly = true)
    public List<ProgramaDTO> obtenerProgramasRecientes() {
        return programaRepository.findRecentPrograms().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Verificar si existe programa con título
    @Transactional(readOnly = true)
    public boolean existeProgramaConTitulo(String titulo) {
        return programaRepository.existsByTituloIgnoreCase(titulo);
    }
    
    // Contar programas por área de conocimiento
    @Transactional(readOnly = true)
    public List<Object[]> contarPorAreaConocimiento() {
        return programaRepository.countByAreaConocimiento();
    }
    
    // Métodos de conversión
    private ProgramaDTO convertirADTO(Programa programa) {
        ProgramaDTO dto = new ProgramaDTO();
        dto.setId(programa.getId());
        dto.setTitulo(programa.getTitulo());
        dto.setDescripcion(programa.getDescripcion());
        dto.setAreaConocimiento(programa.getAreaConocimiento());
        dto.setFechaCreacion(programa.getFechaCreacion());
        return dto;
    }
    
    private ProgramaDTO convertirADTOCompleto(Programa programa) {
        ProgramaDTO dto = convertirADTO(programa);
        // Aquí se pueden agregar las unidades y lecciones si es necesario
        return dto;
    }
}


