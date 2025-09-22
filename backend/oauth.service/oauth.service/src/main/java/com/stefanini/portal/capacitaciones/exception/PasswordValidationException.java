package com.stefanini.portal.capacitaciones.exception;

/**
 * Excepción lanzada cuando la validación de contraseña falla
 */
public class PasswordValidationException extends AuthException {
    
    public PasswordValidationException(String message) {
        super(message, "PASSWORD_VALIDATION_ERROR");
    }
    
    public PasswordValidationException(String message, Throwable cause) {
        super(message, "PASSWORD_VALIDATION_ERROR", cause);
    }
}
