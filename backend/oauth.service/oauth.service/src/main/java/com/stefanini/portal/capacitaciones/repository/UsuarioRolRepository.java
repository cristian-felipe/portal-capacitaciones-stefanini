package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.entity.UsuarioRol;
import com.stefanini.portal.capacitaciones.entity.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    
    /**
     * Busca todos los roles de un usuario específico
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.id = :usuarioId")
    List<UsuarioRol> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    /**
     * Busca todos los usuarios que tienen un rol específico
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.rol.id = :rolId")
    List<UsuarioRol> findByRolId(@Param("rolId") UUID rolId);
    
    /**
     * Verifica si un usuario tiene un rol específico
     */
    @Query("SELECT COUNT(ur) > 0 FROM UsuarioRol ur WHERE ur.usuario.id = :usuarioId AND ur.rol.id = :rolId")
    boolean existsByUsuarioIdAndRolId(@Param("usuarioId") UUID usuarioId, @Param("rolId") UUID rolId);
    
    /**
     * Busca la relación usuario-rol específica
     */
    Optional<UsuarioRol> findByUsuarioIdAndRolId(UUID usuarioId, UUID rolId);
    
    /**
     * Busca todos los roles de un usuario por nombre de rol
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.id = :usuarioId AND ur.rol.nombre = :nombreRol")
    List<UsuarioRol> findByUsuarioIdAndRolNombre(@Param("usuarioId") UUID usuarioId, @Param("nombreRol") String nombreRol);
    
    /**
     * Elimina todos los roles de un usuario
     */
    void deleteByUsuarioId(UUID usuarioId);
    
    /**
     * Elimina un rol específico de un usuario
     */
    void deleteByUsuarioIdAndRolId(UUID usuarioId, UUID rolId);
    
    /**
     * Cuenta cuántos usuarios tienen un rol específico
     */
    @Query("SELECT COUNT(ur) FROM UsuarioRol ur WHERE ur.rol.id = :rolId")
    long countByRolId(@Param("rolId") UUID rolId);
    
    /**
     * Busca usuarios que tengan al menos uno de los roles especificados
     */
    @Query("SELECT DISTINCT ur.usuario FROM UsuarioRol ur WHERE ur.rol.nombre IN :nombresRoles")
    List<Usuario> findUsuariosByRolesNombres(@Param("nombresRoles") List<String> nombresRoles);
}
