package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    /**
     * Busca un usuario por su correo electrónico
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    /**
     * Verifica si existe un usuario con el correo electrónico dado
     */
    boolean existsByCorreoElectronico(String correoElectronico);
    
    /**
     * Busca un usuario por su ID de OAuth y proveedor
     */
    Optional<Usuario> findByProveedorOauthAndIdOauth(String proveedorOauth, String idOauth);
    
    /**
     * Busca usuarios activos por rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    java.util.List<Usuario> findByRolAndActivo(@Param("rol") String rol);
    
    /**
     * Busca usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    java.util.List<Usuario> findUsuariosActivos();
    
    /**
     * Cuenta usuarios activos por rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    long countByRolAndActivo(@Param("rol") String rol);
}

