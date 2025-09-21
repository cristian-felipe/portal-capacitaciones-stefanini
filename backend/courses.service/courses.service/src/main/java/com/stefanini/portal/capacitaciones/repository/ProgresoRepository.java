package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgresoRepository extends JpaRepository<Progreso, Long> {
    
    // Buscar progreso por usuario
    @Query("SELECT p FROM Progreso p WHERE p.usuarioId = :usuarioId")
    List<Progreso> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Buscar progreso por usuario y lección
    @Query("SELECT p FROM Progreso p WHERE p.usuarioId = :usuarioId AND p.leccion.id = :leccionId")
    Optional<Progreso> findByUsuarioIdAndLeccionId(@Param("usuarioId") UUID usuarioId, @Param("leccionId") Long leccionId);
    
    // Contar lecciones completadas por usuario
    @Query("SELECT COUNT(p) FROM Progreso p WHERE p.usuarioId = :usuarioId AND p.estado = 'completado'")
    Long countLeccionesCompletadasByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Contar lecciones en progreso por usuario
    @Query("SELECT COUNT(p) FROM Progreso p WHERE p.usuarioId = :usuarioId AND p.estado = 'en_progreso'")
    Long countLeccionesEnProgresoByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Obtener progreso promedio por usuario
    @Query("SELECT AVG(p.porcentaje) FROM Progreso p WHERE p.usuarioId = :usuarioId")
    Double getProgresoPromedioByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Obtener programas únicos por usuario - simplificado por ahora
    // @Query("SELECT DISTINCT p.leccion.unidad.programa FROM Progreso p WHERE p.usuarioId = :usuarioId")
    // List<Object> findProgramasByUsuarioId(@Param("usuarioId") UUID usuarioId);
}
