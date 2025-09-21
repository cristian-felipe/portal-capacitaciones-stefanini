package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.SesionUsuario;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.repository.SesionUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SesionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SesionService.class);
    
    @Autowired
    private SesionUsuarioRepository sesionUsuarioRepository;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    /**
     * Crea una nueva sesión para un usuario
     */
    public SesionUsuario createSession(Usuario usuario, String token) {
        try {
            // Calcular fecha de expiración
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(jwtExpiration / (1000 * 60 * 60));
            
            // Crear nueva sesión
            SesionUsuario sesion = new SesionUsuario(usuario, token, expirationTime);
            sesion = sesionUsuarioRepository.save(sesion);
            
            logger.info("Sesión creada para usuario: {} con ID: {}", usuario.getCorreoElectronico(), sesion.getId());
            
            // Limpiar sesiones expiradas del usuario
            cleanupExpiredSessionsForUser(usuario);
            
            return sesion;
            
        } catch (Exception e) {
            logger.error("Error al crear sesión para usuario {}: {}", usuario.getCorreoElectronico(), e.getMessage());
            throw new RuntimeException("Error al crear sesión", e);
        }
    }
    
    /**
     * Valida una sesión por token
     */
    public Optional<SesionUsuario> validateSession(String token) {
        try {
            Optional<SesionUsuario> sesionOpt = sesionUsuarioRepository.findByToken(token);
            
            if (sesionOpt.isPresent()) {
                SesionUsuario sesion = sesionOpt.get();
                
                // Verificar si la sesión ha expirado
                if (sesion.isExpirada()) {
                    logger.info("Sesión expirada para token: {}", token.substring(0, Math.min(20, token.length())) + "...");
                    deleteSession(token);
                    return Optional.empty();
                }
                
                logger.debug("Sesión válida encontrada para usuario: {}", sesion.getUsuario().getCorreoElectronico());
                return Optional.of(sesion);
            }
            
            return Optional.empty();
            
        } catch (Exception e) {
            logger.error("Error al validar sesión: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Elimina una sesión por token
     */
    public void deleteSession(String token) {
        try {
            sesionUsuarioRepository.deleteByToken(token);
            logger.info("Sesión eliminada para token: {}", token.substring(0, Math.min(20, token.length())) + "...");
        } catch (Exception e) {
            logger.error("Error al eliminar sesión: {}", e.getMessage());
        }
    }
    
    /**
     * Elimina todas las sesiones de un usuario
     */
    public void deleteAllUserSessions(Usuario usuario) {
        try {
            long count = sesionUsuarioRepository.countSesionesActivasByUsuario(usuario, LocalDateTime.now());
            sesionUsuarioRepository.deleteByUsuario(usuario);
            logger.info("Eliminadas {} sesiones para usuario: {}", count, usuario.getCorreoElectronico());
        } catch (Exception e) {
            logger.error("Error al eliminar sesiones del usuario {}: {}", usuario.getCorreoElectronico(), e.getMessage());
        }
    }
    
    /**
     * Obtiene todas las sesiones activas de un usuario
     */
    public List<SesionUsuario> getUserActiveSessions(Usuario usuario) {
        return sesionUsuarioRepository.findSesionesActivasByUsuario(usuario, LocalDateTime.now());
    }
    
    /**
     * Cuenta las sesiones activas de un usuario
     */
    public long countUserActiveSessions(Usuario usuario) {
        return sesionUsuarioRepository.countSesionesActivasByUsuario(usuario, LocalDateTime.now());
    }
    
    /**
     * Verifica si un usuario tiene sesiones activas
     */
    public boolean hasActiveSessions(Usuario usuario) {
        return countUserActiveSessions(usuario) > 0;
    }
    
    /**
     * Limpia sesiones expiradas de un usuario específico
     */
    private void cleanupExpiredSessionsForUser(Usuario usuario) {
        try {
            LocalDateTime now = LocalDateTime.now();
            sesionUsuarioRepository.deleteSesionesExpiradasByUsuario(usuario, now);
        } catch (Exception e) {
            logger.error("Error al limpiar sesiones expiradas para usuario {}: {}", usuario.getCorreoElectronico(), e.getMessage());
        }
    }
    
    /**
     * Limpia todas las sesiones expiradas del sistema
     * Se ejecuta automáticamente cada hora
     */
    @Scheduled(fixedRate = 3600000) // Cada hora
    public void cleanupAllExpiredSessions() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<SesionUsuario> expiredSessions = sesionUsuarioRepository.findSesionesExpiradas(now);
            
            if (!expiredSessions.isEmpty()) {
                sesionUsuarioRepository.deleteAll(expiredSessions);
                logger.info("Limpiadas {} sesiones expiradas del sistema", expiredSessions.size());
            }
            
        } catch (Exception e) {
            logger.error("Error al limpiar sesiones expiradas: {}", e.getMessage());
        }
    }
    
    /**
     * Obtiene estadísticas de sesiones
     */
    public SessionStats getSessionStats() {
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Contar sesiones activas (aproximado)
            long activeSessions = sesionUsuarioRepository.count();
            
            // Contar sesiones expiradas
            List<SesionUsuario> expiredSessions = sesionUsuarioRepository.findSesionesExpiradas(now);
            
            return new SessionStats(
                activeSessions,
                expiredSessions.size(),
                now
            );
            
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas de sesiones: {}", e.getMessage());
            return new SessionStats(0, 0, LocalDateTime.now());
        }
    }
    
    /**
     * Clase para estadísticas de sesiones
     */
    public static class SessionStats {
        private final long activeSessions;
        private final long expiredSessions;
        private final LocalDateTime timestamp;
        
        public SessionStats(long activeSessions, long expiredSessions, LocalDateTime timestamp) {
            this.activeSessions = activeSessions;
            this.expiredSessions = expiredSessions;
            this.timestamp = timestamp;
        }
        
        public long getActiveSessions() {
            return activeSessions;
        }
        
        public long getExpiredSessions() {
            return expiredSessions;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}



