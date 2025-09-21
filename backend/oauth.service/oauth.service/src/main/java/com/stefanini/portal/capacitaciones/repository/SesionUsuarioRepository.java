package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.SesionUsuario;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, UUID> {
    
    /**
     * Busca una sesión por token
     */
    Optional<SesionUsuario> findByToken(String token);
    
    /**
     * Busca sesiones activas de un usuario
     */
    @Query("SELECT s FROM SesionUsuario s WHERE s.usuario = :usuario AND s.fechaExpiracion > :fechaActual")
    List<SesionUsuario> findSesionesActivasByUsuario(@Param("usuario") Usuario usuario, @Param("fechaActual") LocalDateTime fechaActual);
    
    /**
     * Busca sesiones expiradas
     */
    @Query("SELECT s FROM SesionUsuario s WHERE s.fechaExpiracion < :fechaActual")
    List<SesionUsuario> findSesionesExpiradas(@Param("fechaActual") LocalDateTime fechaActual);
    
    /**
     * Elimina sesiones expiradas de un usuario
     */
    @Modifying
    @Query("DELETE FROM SesionUsuario s WHERE s.usuario = :usuario AND s.fechaExpiracion < :fechaActual")
    void deleteSesionesExpiradasByUsuario(@Param("usuario") Usuario usuario, @Param("fechaActual") LocalDateTime fechaActual);
    
    /**
     * Elimina todas las sesiones de un usuario
     */
    void deleteByUsuario(Usuario usuario);
    
    /**
     * Elimina una sesión específica por token
     */
    void deleteByToken(String token);
    
    /**
     * Cuenta sesiones activas de un usuario
     */
    @Query("SELECT COUNT(s) FROM SesionUsuario s WHERE s.usuario = :usuario AND s.fechaExpiracion > :fechaActual")
    long countSesionesActivasByUsuario(@Param("usuario") Usuario usuario, @Param("fechaActual") LocalDateTime fechaActual);
}



