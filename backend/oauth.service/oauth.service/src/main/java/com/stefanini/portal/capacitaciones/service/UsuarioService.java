package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Busca un usuario por su ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }
    
    /**
     * Busca un usuario por su correo electrónico
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico);
    }
    
    /**
     * Verifica si existe un usuario con el correo electrónico dado
     */
    @Transactional(readOnly = true)
    public boolean existePorCorreoElectronico(String correoElectronico) {
        return usuarioRepository.existsByCorreoElectronico(correoElectronico);
    }
    
    /**
     * Busca un usuario por su ID de OAuth y proveedor
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorOAuth(String proveedorOauth, String idOauth) {
        return usuarioRepository.findByProveedorOauthAndIdOauth(proveedorOauth, idOauth);
    }
    
    /**
     * Obtiene todos los usuarios activos
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findUsuariosActivos();
    }
    
    /**
     * Guarda un usuario
     */
    public Usuario guardar(Usuario usuario) {
        logger.info("Guardando usuario: {}", usuario.getCorreoElectronico());
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Actualiza un usuario
     */
    public Usuario actualizar(Usuario usuario) {
        logger.info("Actualizando usuario: {}", usuario.getCorreoElectronico());
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Elimina un usuario
     */
    public void eliminar(UUID id) {
        logger.info("Eliminando usuario con ID: {}", id);
        usuarioRepository.deleteById(id);
    }
    
    /**
     * Desactiva un usuario (soft delete)
     */
    public void desactivarUsuario(UUID id) {
        logger.info("Desactivando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
    
    /**
     * Activa un usuario
     */
    public void activarUsuario(UUID id) {
        logger.info("Activando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
    
    /**
     * Cuenta usuarios activos por rol
     */
    @Transactional(readOnly = true)
    public long contarUsuariosActivosPorRol(String rol) {
        return usuarioRepository.countByRolAndActivo(rol);
    }
    
    /**
     * Obtiene usuarios activos por rol
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivosPorRol(String rol) {
        return usuarioRepository.findByRolAndActivo(rol);
    }
}
