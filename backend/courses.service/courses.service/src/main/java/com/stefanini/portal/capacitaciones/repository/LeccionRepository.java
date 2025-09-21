package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Leccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeccionRepository extends JpaRepository<Leccion, Integer> {
    
    // Buscar lecciones por unidad
    List<Leccion> findByUnidadIdOrderByOrden(Integer unidadId);
    
    // Buscar lecciones por tipo de material
    List<Leccion> findByTipoMaterial(String tipoMaterial);
    
    // Buscar lecciones por título (búsqueda parcial)
    List<Leccion> findByTituloContainingIgnoreCase(String titulo);
    
    // Obtener lección con su unidad y programa
    @Query("SELECT l FROM Leccion l LEFT JOIN FETCH l.unidad u LEFT JOIN FETCH u.programa WHERE l.id = :id")
    Optional<Leccion> findByIdWithUnidadAndPrograma(@Param("id") Integer id);
    
    // Obtener todas las lecciones de una unidad con query personalizada
    @Query("SELECT l FROM Leccion l WHERE l.unidad.id = :unidadId ORDER BY l.orden")
    List<Leccion> findByUnidadIdWithQuery(@Param("unidadId") Integer unidadId);
    
    // Contar lecciones por unidad
    @Query("SELECT COUNT(l) FROM Leccion l WHERE l.unidad.id = :unidadId")
    Long countByUnidadId(@Param("unidadId") Integer unidadId);
    
    // Contar lecciones por tipo de material
    @Query("SELECT COUNT(l) FROM Leccion l WHERE l.tipoMaterial = :tipoMaterial")
    Long countByTipoMaterial(@Param("tipoMaterial") String tipoMaterial);
    
    // Obtener el siguiente orden para una nueva lección
    @Query("SELECT COALESCE(MAX(l.orden), 0) + 1 FROM Leccion l WHERE l.unidad.id = :unidadId")
    Integer getNextOrden(@Param("unidadId") Integer unidadId);
    
    // Verificar si existe una lección con el mismo título en la unidad
    boolean existsByTituloAndUnidadId(String titulo, Integer unidadId);
    
    // Verificar si existe una lección con el mismo título en la unidad (ignorando mayúsculas)
    boolean existsByTituloIgnoreCaseAndUnidadId(String titulo, Integer unidadId);
    
    // Buscar lecciones por programa (a través de la unidad)
    @Query("SELECT l FROM Leccion l WHERE l.unidad.programa.id = :programaId ORDER BY l.unidad.orden, l.orden")
    List<Leccion> findByProgramaId(@Param("programaId") Integer programaId);
}
