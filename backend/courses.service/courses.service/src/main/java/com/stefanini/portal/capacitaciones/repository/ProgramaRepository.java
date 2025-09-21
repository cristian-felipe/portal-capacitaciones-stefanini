package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Integer> {
    
    // Buscar programas por área de conocimiento
    List<Programa> findByAreaConocimiento(String areaConocimiento);
    
    // Buscar programas por título (búsqueda parcial)
    List<Programa> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar programas por descripción (búsqueda parcial)
    List<Programa> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Obtener programa con sus unidades y lecciones
    @Query("SELECT p FROM Programa p LEFT JOIN FETCH p.unidades u LEFT JOIN FETCH u.lecciones WHERE p.id = :id")
    Optional<Programa> findByIdWithUnidadesAndLecciones(@Param("id") Integer id);
    
    // Obtener todos los programas con sus unidades
    @Query("SELECT p FROM Programa p LEFT JOIN FETCH p.unidades ORDER BY p.fechaCreacion DESC")
    List<Programa> findAllWithUnidades();
    
    // Contar programas por área de conocimiento
    @Query("SELECT p.areaConocimiento, COUNT(p) FROM Programa p GROUP BY p.areaConocimiento")
    List<Object[]> countByAreaConocimiento();
    
    // Buscar programas recientes
    @Query("SELECT p FROM Programa p ORDER BY p.fechaCreacion DESC")
    List<Programa> findRecentPrograms();
    
    // Verificar si existe un programa con el mismo título
    boolean existsByTitulo(String titulo);
    
    // Verificar si existe un programa con el mismo título (ignorando mayúsculas)
    boolean existsByTituloIgnoreCase(String titulo);
}


