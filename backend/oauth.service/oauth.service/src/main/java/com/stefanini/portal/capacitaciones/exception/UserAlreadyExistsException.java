package com.stefanini.portal.capacitaciones.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe
 */
public class UserAlreadyExistsException extends AuthException {
    
    public UserAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS");
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, "USER_ALREADY_EXISTS", cause);
    }
}
