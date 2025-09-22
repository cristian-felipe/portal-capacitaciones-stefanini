package com.stefanini.portal.capacitaciones.exception;

/**
 * Excepción lanzada cuando las credenciales son inválidas
 */
public class InvalidCredentialsException extends AuthException {
    
    public InvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS");
    }
    
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, "INVALID_CREDENTIALS", cause);
    }
}
