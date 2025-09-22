package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    
    // Buscar materiales por tipo
    List<Material> findByTipoMaterial(String tipoMaterial);
    
    // Buscar materiales activos
    List<Material> findByActivoTrue();
    
    // Buscar materiales por tipo y activos
    List<Material> findByTipoMaterialAndActivoTrue(String tipoMaterial);
    
    // Buscar material por nombre de archivo
    Optional<Material> findByNombreArchivo(String nombreArchivo);
    
    // Buscar materiales por extensión
    List<Material> findByExtension(String extension);
    
    // Buscar materiales por tamaño (mayor que)
    @Query("SELECT m FROM Material m WHERE m.tamañoBytes > :tamañoMinimo AND m.activo = true")
    List<Material> findByTamañoMayorQue(@Param("tamañoMinimo") Long tamañoMinimo);
    
    // Buscar materiales por tamaño (menor que)
    @Query("SELECT m FROM Material m WHERE m.tamañoBytes < :tamañoMaximo AND m.activo = true")
    List<Material> findByTamañoMenorQue(@Param("tamañoMaximo") Long tamañoMaximo);
    
    // Contar materiales por tipo
    @Query("SELECT m.tipoMaterial, COUNT(m) FROM Material m WHERE m.activo = true GROUP BY m.tipoMaterial")
    List<Object[]> countByTipoMaterial();
    
    // Obtener materiales recientes
    @Query("SELECT m FROM Material m WHERE m.activo = true ORDER BY m.fechaSubida DESC")
    List<Material> findMaterialesRecientes();
    
    // Buscar materiales por nombre original (búsqueda parcial)
    List<Material> findByNombreOriginalContainingIgnoreCase(String nombreOriginal);
    
    // Verificar si existe material con el mismo nombre de archivo
    boolean existsByNombreArchivo(String nombreArchivo);
    
    // Obtener total de espacio usado por materiales activos
    @Query("SELECT COALESCE(SUM(m.tamañoBytes), 0) FROM Material m WHERE m.activo = true")
    Long getTotalEspacioUsado();
}
