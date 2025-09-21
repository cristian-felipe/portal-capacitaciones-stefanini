package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.RolEntity;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.entity.UsuarioRol;
import com.stefanini.portal.capacitaciones.repository.RolRepository;
import com.stefanini.portal.capacitaciones.repository.UsuarioRolRepository;
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
public class RolService {
    
    private static final Logger logger = LoggerFactory.getLogger(RolService.class);
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    
    /**
     * Crea un nuevo rol
     */
    public RolEntity crearRol(String nombre, String descripcion) {
        logger.info("Creando nuevo rol: {}", nombre);
        
        if (rolRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + nombre);
        }
        
        RolEntity rol = new RolEntity(nombre, descripcion);
        return rolRepository.save(rol);
    }
    
    /**
     * Busca un rol por su nombre
     */
    @Transactional(readOnly = true)
    public Optional<RolEntity> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
    
    /**
     * Busca un rol por su ID
     */
    @Transactional(readOnly = true)
    public Optional<RolEntity> buscarPorId(UUID id) {
        return rolRepository.findById(id);
    }
    
    /**
     * Obtiene todos los roles
     */
    @Transactional(readOnly = true)
    public List<RolEntity> obtenerTodosLosRoles() {
        return rolRepository.findAllOrderByNombre();
    }
    
    /**
     * Actualiza un rol
     */
    public RolEntity actualizarRol(UUID id, String nombre, String descripcion) {
        logger.info("Actualizando rol con ID: {}", id);
        
        RolEntity rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe en otro rol
        if (!rol.getNombre().equals(nombre) && rolRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + nombre);
        }
        
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        
        return rolRepository.save(rol);
    }
    
    /**
     * Elimina un rol
     */
    public void eliminarRol(UUID id) {
        logger.info("Eliminando rol con ID: {}", id);
        
        RolEntity rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
        
        // Verificar si hay usuarios con este rol
        long usuariosConRol = usuarioRolRepository.countByRolId(id);
        if (usuariosConRol > 0) {
            throw new IllegalStateException("No se puede eliminar el rol porque hay " + usuariosConRol + " usuarios asignados");
        }
        
        rolRepository.delete(rol);
    }
    
    /**
     * Asigna un rol a un usuario
     */
    public void asignarRolAUsuario(Usuario usuario, RolEntity rol) {
        logger.info("Asignando rol {} al usuario {}", rol.getNombre(), usuario.getCorreoElectronico());
        
        // Verificar si ya tiene el rol
        if (usuarioRolRepository.existsByUsuarioIdAndRolId(usuario.getId(), rol.getId())) {
            logger.warn("El usuario {} ya tiene el rol {}", usuario.getCorreoElectronico(), rol.getNombre());
            return;
        }
        
        UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);
        usuarioRolRepository.save(usuarioRol);
    }
    
    /**
     * Remueve un rol de un usuario
     */
    public void removerRolDeUsuario(Usuario usuario, RolEntity rol) {
        logger.info("Removiendo rol {} del usuario {}", rol.getNombre(), usuario.getCorreoElectronico());
        
        usuarioRolRepository.deleteByUsuarioIdAndRolId(usuario.getId(), rol.getId());
    }
    
    /**
     * Obtiene todos los roles de un usuario
     */
    @Transactional(readOnly = true)
    public List<UsuarioRol> obtenerRolesDeUsuario(UUID usuarioId) {
        return usuarioRolRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Verifica si un usuario tiene un rol específico
     */
    @Transactional(readOnly = true)
    public boolean usuarioTieneRol(UUID usuarioId, String nombreRol) {
        return !usuarioRolRepository.findByUsuarioIdAndRolNombre(usuarioId, nombreRol).isEmpty();
    }
    
    /**
     * Inicializa los roles básicos del sistema
     */
    public void inicializarRolesBasicos() {
        logger.info("Inicializando roles básicos del sistema");
        
        // Crear rol admin si no existe
        if (!rolRepository.existsByNombre("admin")) {
            crearRol("admin", "Acceso total al sistema");
        }
        
        // Crear rol instructor si no existe
        if (!rolRepository.existsByNombre("instructor")) {
            crearRol("instructor", "Puede crear y gestionar cursos");
        }
        
        // Crear rol usuario si no existe
        if (!rolRepository.existsByNombre("usuario")) {
            crearRol("usuario", "Acceso básico a cursos");
        }
        
        logger.info("Roles básicos inicializados correctamente");
    }
}


