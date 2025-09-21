package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.InsigniaDTO;
import com.stefanini.portal.capacitaciones.dto.InsigniaOtorgadaDTO;
import com.stefanini.portal.capacitaciones.entity.Insignia;
import com.stefanini.portal.capacitaciones.entity.InsigniaOtorgada;
import com.stefanini.portal.capacitaciones.repository.InsigniaOtorgadaRepository;
import com.stefanini.portal.capacitaciones.repository.InsigniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsigniaService {
    
    @Autowired
    private InsigniaRepository insigniaRepository;
    
    @Autowired
    private InsigniaOtorgadaRepository insigniaOtorgadaRepository;
    
    // Obtener todas las insignias
    public List<InsigniaDTO> getAllInsignias() {
        return insigniaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener insignias por usuario
    public List<InsigniaOtorgadaDTO> getInsigniasByUsuarioId(UUID usuarioId) {
        List<InsigniaOtorgada> insignias = insigniaOtorgadaRepository.findByUsuarioId(usuarioId);
        return insignias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Contar insignias por usuario
    public Long countInsigniasByUsuarioId(UUID usuarioId) {
        return insigniaOtorgadaRepository.countByUsuarioId(usuarioId);
    }
    
    // Otorgar insignia a usuario
    public InsigniaOtorgadaDTO otorgarInsignia(UUID usuarioId, Long insigniaId) {
        Optional<Insignia> insigniaOpt = insigniaRepository.findById(insigniaId);
        if (insigniaOpt.isEmpty()) {
            throw new RuntimeException("Insignia no encontrada");
        }
        
        // Verificar si ya tiene la insignia
        boolean yaTieneInsignia = insigniaOtorgadaRepository.existsByUsuarioIdAndInsigniaId(usuarioId, insigniaId);
        if (yaTieneInsignia) {
            throw new RuntimeException("El usuario ya tiene esta insignia");
        }
        
        Insignia insignia = insigniaOpt.get();
        InsigniaOtorgada insigniaOtorgada = new InsigniaOtorgada(usuarioId, insignia);
        insigniaOtorgada = insigniaOtorgadaRepository.save(insigniaOtorgada);
        
        return convertToDTO(insigniaOtorgada);
    }
    
    // Crear nueva insignia
    public InsigniaDTO crearInsignia(InsigniaDTO insigniaDTO) {
        Insignia insignia = new Insignia(
                insigniaDTO.getNombre(),
                insigniaDTO.getDescripcion(),
                insigniaDTO.getUrlImagen()
        );
        
        insignia = insigniaRepository.save(insignia);
        return convertToDTO(insignia);
    }
    
    // Convertir entidad a DTO
    private InsigniaDTO convertToDTO(Insignia insignia) {
        return new InsigniaDTO(
                insignia.getId(),
                insignia.getNombre(),
                insignia.getDescripcion(),
                insignia.getUrlImagen()
        );
    }
    
    // Convertir InsigniaOtorgada a DTO
    private InsigniaOtorgadaDTO convertToDTO(InsigniaOtorgada insigniaOtorgada) {
        InsigniaOtorgadaDTO dto = new InsigniaOtorgadaDTO(
                insigniaOtorgada.getId(),
                insigniaOtorgada.getUsuarioId(),
                insigniaOtorgada.getInsignia().getId(),
                insigniaOtorgada.getFechaOtorgada()
        );
        
        dto.setInsignia(convertToDTO(insigniaOtorgada.getInsignia()));
        return dto;
    }
}

