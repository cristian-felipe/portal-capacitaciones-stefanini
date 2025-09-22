package com.stefanini.portal.capacitaciones.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario
 */
public class UserNotFoundException extends AuthException {
    
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, "USER_NOT_FOUND", cause);
    }
}
