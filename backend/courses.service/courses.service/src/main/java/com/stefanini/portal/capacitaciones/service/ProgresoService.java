package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.ProgresoDTO;
import com.stefanini.portal.capacitaciones.dto.ProgresoProgramaDTO;
import com.stefanini.portal.capacitaciones.dto.UserStatsDTO;
import com.stefanini.portal.capacitaciones.entity.Leccion;
import com.stefanini.portal.capacitaciones.entity.Progreso;
import com.stefanini.portal.capacitaciones.repository.InsigniaOtorgadaRepository;
import com.stefanini.portal.capacitaciones.repository.LeccionRepository;
import com.stefanini.portal.capacitaciones.repository.ProgresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgresoService {
    
    @Autowired
    private ProgresoRepository progresoRepository;
    
    @Autowired
    private LeccionRepository leccionRepository;
    
    @Autowired
    private InsigniaOtorgadaRepository insigniaOtorgadaRepository;
    
    // Obtener progreso por usuario (detalle por lección) con validaciones de estado
    public List<ProgresoDTO> getProgresoByUsuarioId(UUID usuarioId) {
        List<Progreso> progresos = progresoRepository.findByUsuarioId(usuarioId);
        
        // Aplicar validaciones de estado por programa
        List<ProgresoDTO> progresosConValidacion = aplicarValidacionesEstado(progresos);
        
        return progresosConValidacion;
    }
    
    // Obtener progreso por usuario agrupado por programas
    public List<ProgresoProgramaDTO> getProgresoProgramasByUsuarioId(UUID usuarioId) {
        List<Progreso> progresos = progresoRepository.findByUsuarioId(usuarioId);
        
        // Agrupar por programa
        Map<Long, List<Progreso>> progresosPorPrograma = progresos.stream()
                .filter(p -> p.getLeccion() != null && 
                           p.getLeccion().getUnidad() != null && 
                           p.getLeccion().getUnidad().getPrograma() != null)
                .collect(Collectors.groupingBy(p -> Long.valueOf(p.getLeccion().getUnidad().getPrograma().getId())));
        
        List<ProgresoProgramaDTO> resultado = new ArrayList<>();
        
        for (Map.Entry<Long, List<Progreso>> entry : progresosPorPrograma.entrySet()) {
            Long programaId = entry.getKey();
            List<Progreso> progresosDelPrograma = entry.getValue();
            
            // Obtener información del programa (tomamos de la primera lección)
            var programa = progresosDelPrograma.get(0).getLeccion().getUnidad().getPrograma();
            
            // Calcular estadísticas
            int totalLecciones = progresosDelPrograma.size();
            int leccionesCompletadas = (int) progresosDelPrograma.stream()
                    .filter(p -> "completado".equals(p.getEstado()) && p.getPorcentaje() >= 100)
                    .count();
            int leccionesEnProgreso = (int) progresosDelPrograma.stream()
                    .filter(p -> "en_progreso".equals(p.getEstado()) || 
                                ("iniciado".equals(p.getEstado()) && p.getPorcentaje() > 0))
                    .count();
            int leccionesInscritas = (int) progresosDelPrograma.stream()
                    .filter(p -> "inscrito".equals(p.getEstado()) || 
                                ("iniciado".equals(p.getEstado()) && p.getPorcentaje() == 0))
                    .count();
            
            // Calcular progreso general (promedio de porcentajes)
            double progresoGeneral = progresosDelPrograma.stream()
                    .mapToDouble(p -> p.getPorcentaje() != null ? p.getPorcentaje() : 0.0)
                    .average()
                    .orElse(0.0);
            
            // Determinar estado del programa
            String estadoPrograma;
            if (leccionesCompletadas == totalLecciones && progresoGeneral >= 100) {
                estadoPrograma = "completado";
            } else if (leccionesEnProgreso > 0 || (leccionesCompletadas > 0 && leccionesCompletadas < totalLecciones)) {
                estadoPrograma = "en_progreso";
            } else if (leccionesInscritas == totalLecciones && progresoGeneral == 0) {
                estadoPrograma = "inscrito";
            } else {
                // Estado mixto, priorizar en_progreso
                estadoPrograma = "en_progreso";
            }
            
            // Obtener fecha de última actualización
            var fechaUltimaActualizacion = progresosDelPrograma.stream()
                    .map(Progreso::getFechaActualizacion)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(programa.getFechaCreacion());
            
            ProgresoProgramaDTO dto = new ProgresoProgramaDTO(
                    programaId,
                    programa.getTitulo(),
                    programa.getDescripcion(),
                    programa.getAreaConocimiento(),
                    estadoPrograma,
                    Math.round(progresoGeneral * 100.0) / 100.0, // Redondear a 2 decimales
                    totalLecciones,
                    leccionesCompletadas,
                    leccionesEnProgreso,
                    leccionesInscritas,
                    programa.getFechaCreacion(),
                    fechaUltimaActualizacion
            );
            
            resultado.add(dto);
        }
        
        return resultado;
    }
    
    // Actualizar o crear progreso
    public ProgresoDTO actualizarProgreso(UUID usuarioId, Integer leccionId, Double porcentaje) {
        Optional<Leccion> leccionOpt = leccionRepository.findById(leccionId);
        if (leccionOpt.isEmpty()) {
            throw new RuntimeException("Lección no encontrada");
        }
        
        Leccion leccion = leccionOpt.get();
        Optional<Progreso> progresoOpt = progresoRepository.findByUsuarioIdAndLeccionId(usuarioId, leccionId.longValue());
        
        Progreso progreso;
        if (progresoOpt.isPresent()) {
            progreso = progresoOpt.get();
            progreso.setPorcentaje(porcentaje);
            progreso.setEstado(porcentaje >= 100 ? "completado" : "en_progreso");
        } else {
            progreso = new Progreso(usuarioId, leccion, 
                    porcentaje >= 100 ? "completado" : "en_progreso", porcentaje);
        }
        
        progreso = progresoRepository.save(progreso);
        return convertToDTO(progreso);
    }
    
    // Obtener estadísticas del usuario con validaciones por programa
    public UserStatsDTO getEstadisticasUsuario(UUID usuarioId) {
        // Obtener progreso agrupado por programas para calcular correctamente
        List<ProgresoProgramaDTO> progresosPorPrograma = getProgresoProgramasByUsuarioId(usuarioId);
        
        // Calcular estadísticas basadas en programas, no en lecciones individuales
        int totalCursos = progresosPorPrograma.size();
        int cursosCompletados = (int) progresosPorPrograma.stream()
                .filter(p -> "completado".equals(p.getEstadoPrograma()))
                .count();
        int cursosEnProgreso = (int) progresosPorPrograma.stream()
                .filter(p -> "en_progreso".equals(p.getEstadoPrograma()))
                .count();
        int cursosInscritos = (int) progresosPorPrograma.stream()
                .filter(p -> "inscrito".equals(p.getEstadoPrograma()))
                .count();
        
        // Calcular progreso promedio basado en programas
        double progresoPromedio = progresosPorPrograma.stream()
                .mapToDouble(ProgresoProgramaDTO::getProgresoGeneral)
                .average()
                .orElse(0.0);
        
        // Obtener total de insignias del usuario
        Long totalInsignias = insigniaOtorgadaRepository.countByUsuarioId(usuarioId);
        
        // Calcular lecciones completadas y en progreso (para compatibilidad con el DTO)
        int leccionesCompletadas = progresosPorPrograma.stream()
                .mapToInt(ProgresoProgramaDTO::getLeccionesCompletadas)
                .sum();
        int leccionesEnProgreso = progresosPorPrograma.stream()
                .mapToInt(ProgresoProgramaDTO::getLeccionesEnProgreso)
                .sum();
        
        return new UserStatsDTO(
                totalCursos,
                cursosEnProgreso + cursosInscritos, // cursos iniciados = en progreso + inscritos
                cursosCompletados,
                progresoPromedio,
                0, // tiempoTotalEstudio - por implementar
                totalInsignias.intValue(),
                leccionesCompletadas,
                leccionesEnProgreso
        );
    }
    
    // Convertir entidad a DTO
    private ProgresoDTO convertToDTO(Progreso progreso) {
        ProgresoDTO dto = new ProgresoDTO(
                progreso.getId(),
                progreso.getUsuarioId(),
                progreso.getLeccion().getId().longValue(),
                progreso.getEstado(),
                progreso.getPorcentaje(),
                progreso.getFechaActualizacion()
        );
        
        // Verificar que la lección existe antes de acceder a sus propiedades
        if (progreso.getLeccion() != null) {
            dto.setTituloLeccion(progreso.getLeccion().getTitulo());
            
            // Verificar que la unidad y programa existen
            if (progreso.getLeccion().getUnidad() != null && 
                progreso.getLeccion().getUnidad().getPrograma() != null) {
                dto.setTituloPrograma(progreso.getLeccion().getUnidad().getPrograma().getTitulo());
            } else {
                dto.setTituloPrograma("Programa no disponible");
            }
        } else {
            dto.setTituloLeccion("Lección no disponible");
            dto.setTituloPrograma("Programa no disponible");
        }
        
        return dto;
    }
    
    // Método para aplicar validaciones de estado por programa
    private List<ProgresoDTO> aplicarValidacionesEstado(List<Progreso> progresos) {
        // Agrupar por programa
        Map<Long, List<Progreso>> progresosPorPrograma = progresos.stream()
                .filter(p -> p.getLeccion() != null && 
                           p.getLeccion().getUnidad() != null && 
                           p.getLeccion().getUnidad().getPrograma() != null)
                .collect(Collectors.groupingBy(p -> Long.valueOf(p.getLeccion().getUnidad().getPrograma().getId())));
        
        List<ProgresoDTO> resultado = new ArrayList<>();
        
        for (Map.Entry<Long, List<Progreso>> entry : progresosPorPrograma.entrySet()) {
            List<Progreso> progresosDelPrograma = entry.getValue();
            
            // Calcular estadísticas del programa
            int totalLecciones = progresosDelPrograma.size();
            int leccionesCompletadas = (int) progresosDelPrograma.stream()
                    .filter(p -> "completado".equals(p.getEstado()) && p.getPorcentaje() >= 100)
                    .count();
            int leccionesEnProgreso = (int) progresosDelPrograma.stream()
                    .filter(p -> "en_progreso".equals(p.getEstado()) || 
                                ("iniciado".equals(p.getEstado()) && p.getPorcentaje() != null && p.getPorcentaje() > 0))
                    .count();
            int leccionesInscritas = (int) progresosDelPrograma.stream()
                    .filter(p -> "inscrito".equals(p.getEstado()) || 
                                ("iniciado".equals(p.getEstado()) && (p.getPorcentaje() == null || p.getPorcentaje() == 0.0)))
                    .count();
            
            // Determinar estado del programa según las validaciones
            String estadoPrograma;
            if (leccionesCompletadas == totalLecciones) {
                // Todas completadas al 100%
                estadoPrograma = "completado";
            } else if (leccionesInscritas == totalLecciones && 
                      progresosDelPrograma.stream().allMatch(p -> p.getPorcentaje() == null || p.getPorcentaje() == 0.0)) {
                // Todas inscritas con porcentaje 0
                estadoPrograma = "inscrito";
            } else if (leccionesEnProgreso > 0 || (leccionesCompletadas > 0 && leccionesCompletadas < totalLecciones)) {
                // Algunas en progreso o mixto
                estadoPrograma = "en_progreso";
            } else {
                // Estado mixto, priorizar en_progreso
                estadoPrograma = "en_progreso";
            }
            
            // Aplicar el estado calculado a todos los progresos del programa
            for (Progreso progreso : progresosDelPrograma) {
                // Convertir a DTO sin modificar la entidad original
                ProgresoDTO dto = convertToDTO(progreso);
                
                // Aplicar el estado validado al DTO
                dto.setEstado(estadoPrograma);
                
                resultado.add(dto);
            }
        }
        
        return resultado;
    }
}
