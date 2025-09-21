package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, UUID> {
    
    /**
     * Busca un rol por su nombre
     */
    Optional<RolEntity> findByNombre(String nombre);
    
    /**
     * Verifica si existe un rol con el nombre especificado
     */
    boolean existsByNombre(String nombre);
    
    /**
     * Busca roles por nombre (case insensitive)
     */
    @Query("SELECT r FROM RolEntity r WHERE LOWER(r.nombre) = LOWER(:nombre)")
    Optional<RolEntity> findByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca todos los roles activos (si se implementa un campo activo en el futuro)
     */
    @Query("SELECT r FROM RolEntity r ORDER BY r.nombre")
    List<RolEntity> findAllOrderByNombre();
    
    /**
     * Busca roles que contengan el texto especificado en el nombre
     */
    @Query("SELECT r FROM RolEntity r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<RolEntity> findByNombreContainingIgnoreCase(@Param("texto") String texto);
}


