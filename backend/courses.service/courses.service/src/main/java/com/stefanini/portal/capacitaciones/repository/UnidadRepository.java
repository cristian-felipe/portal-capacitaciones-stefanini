package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    
    // Buscar unidades por programa
    List<Unidad> findByProgramaIdOrderByOrden(Integer programaId);
    
    // Buscar unidades por título (búsqueda parcial)
    List<Unidad> findByTituloContainingIgnoreCase(String titulo);
    
    // Obtener unidad con sus lecciones
    @Query("SELECT u FROM Unidad u LEFT JOIN FETCH u.lecciones WHERE u.id = :id")
    Optional<Unidad> findByIdWithLecciones(@Param("id") Integer id);
    
    // Obtener todas las unidades de un programa con sus lecciones
    @Query("SELECT u FROM Unidad u LEFT JOIN FETCH u.lecciones WHERE u.programa.id = :programaId ORDER BY u.orden")
    List<Unidad> findByProgramaIdWithLecciones(@Param("programaId") Integer programaId);
    
    // Contar unidades por programa
    @Query("SELECT COUNT(u) FROM Unidad u WHERE u.programa.id = :programaId")
    Long countByProgramaId(@Param("programaId") Integer programaId);
    
    // Obtener el siguiente orden para una nueva unidad
    @Query("SELECT COALESCE(MAX(u.orden), 0) + 1 FROM Unidad u WHERE u.programa.id = :programaId")
    Integer getNextOrden(@Param("programaId") Integer programaId);
    
    // Verificar si existe una unidad con el mismo título en el programa
    boolean existsByTituloAndProgramaId(String titulo, Integer programaId);
    
    // Verificar si existe una unidad con el mismo título en el programa (ignorando mayúsculas)
    boolean existsByTituloIgnoreCaseAndProgramaId(String titulo, Integer programaId);
}


