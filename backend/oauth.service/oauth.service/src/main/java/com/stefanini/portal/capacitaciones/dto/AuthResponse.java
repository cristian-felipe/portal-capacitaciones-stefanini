package com.stefanini.portal.capacitaciones.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefanini.portal.capacitaciones.constants.AuthConstants;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    
    private Boolean success;
    private String message;
    private String token;
    private UserResponse user;
    private Map<String, Object> additionalData;
    private LocalDateTime timestamp;
    
    public AuthResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public AuthResponse(Boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }
    
    public AuthResponse(Boolean success, String message, String token, UserResponse user) {
        this(success, message);
        this.token = token;
        this.user = user;
    }
    public static AuthResponse success(String message) {
        return new AuthResponse(true, message);
    }
    
    public static AuthResponse success(String message, String token, UserResponse user) {
        return new AuthResponse(true, message, token, user);
    }
    
    public static AuthResponse error(String message) {
        return new AuthResponse(false, message);
    }
    
    public static AuthResponse userRegistered(String token, UserResponse user) {
        return success(AuthConstants.USER_REGISTERED_SUCCESSFULLY, token, user);
    }
    
    public static AuthResponse userAuthenticated(String token, UserResponse user) {
        return success(AuthConstants.USER_AUTHENTICATED_SUCCESSFULLY, token, user);
    }
    
    public static AuthResponse passwordChanged() {
        return success(AuthConstants.PASSWORD_CHANGED_SUCCESSFULLY);
    }
    
    public static AuthResponse userAlreadyExists() {
        return error(AuthConstants.USER_ALREADY_EXISTS);
    }
    
    public static AuthResponse invalidCredentials() {
        return error(AuthConstants.INVALID_CREDENTIALS);
    }
    
    public static AuthResponse userDisabled() {
        return error(AuthConstants.USER_DISABLED);
    }
    
    public static AuthResponse userNotFound() {
        return error(AuthConstants.USER_NOT_FOUND);
    }
    
    public static AuthResponse invalidCurrentPassword() {
        return error(AuthConstants.INVALID_CURRENT_PASSWORD);
    }
    
    public static AuthResponse passwordValidationError() {
        return error(AuthConstants.PASSWORD_VALIDATION_ERROR);
    }
    
    public static AuthResponse newPasswordValidationError() {
        return error(AuthConstants.NEW_PASSWORD_VALIDATION_ERROR);
    }
    
    public static AuthResponse missingParameters() {
        return error(AuthConstants.MISSING_PARAMETERS);
    }
    
    public static AuthResponse registrationError(String error) {
        return error(AuthConstants.REGISTRATION_ERROR + error);
    }
    
    public static AuthResponse authenticationError(String error) {
        return error(AuthConstants.AUTHENTICATION_ERROR + error);
    }
    
    public static AuthResponse passwordChangeError(String error) {
        return error(AuthConstants.PASSWORD_CHANGE_ERROR + error);
    }
    
    public Boolean getSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserResponse getUser() {
        return user;
    }
    
    public void setUser(UserResponse user) {
        this.user = user;
    }
    
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }
    
    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public AuthResponse withAdditionalData(String key, Object value) {
        if (this.additionalData == null) {
            this.additionalData = new java.util.HashMap<>();
        }
        this.additionalData.put(key, value);
        return this;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", token='" + (token != null ? "[PROTECTED]" : null) + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                '}';
    }
}
