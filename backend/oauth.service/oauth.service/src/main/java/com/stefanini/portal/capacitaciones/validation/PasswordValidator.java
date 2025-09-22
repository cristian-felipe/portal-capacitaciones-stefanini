package com.stefanini.portal.capacitaciones.validation;

import com.stefanini.portal.capacitaciones.exception.PasswordValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    public boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordValidationException("La contraseña no puede estar vacía");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordValidationException("La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PasswordValidationException("La contraseña debe contener al menos una letra y un número");
        }
        
        return true;
    }
    
    public boolean isValidPasswordSilent(String password) {
        try {
            return isValidPassword(password);
        } catch (PasswordValidationException e) {
            return false;
        }
    }
    
    public String getValidationErrorMessage(String password) {
        try {
            isValidPassword(password);
            return null;
        } catch (PasswordValidationException e) {
            return e.getMessage();
        }
    }
    
    public void validatePasswordChange(String currentPassword, String newPassword) {
        if (currentPassword != null && currentPassword.equals(newPassword)) {
            throw new PasswordValidationException("La nueva contraseña debe ser diferente a la actual");
        }
        
        isValidPassword(newPassword);
    }
}
