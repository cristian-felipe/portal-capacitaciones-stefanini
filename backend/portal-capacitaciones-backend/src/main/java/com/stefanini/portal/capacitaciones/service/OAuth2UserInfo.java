package com.stefanini.portal.capacitaciones.service;

import java.util.Map;

/**
 * Clase que representa la información del usuario obtenida de un proveedor OAuth2
 */
public class OAuth2UserInfo {
    
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String provider;
    private String oauthId;
    private String picture;
    private String locale;
    private Map<String, Object> attributes;
    
    // Constructores
    public OAuth2UserInfo() {}
    
    public OAuth2UserInfo(String email, String firstName, String lastName, String provider, String oauthId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.provider = provider;
        this.oauthId = oauthId;
        this.fullName = firstName + " " + lastName;
    }
    
    /**
     * Crea OAuth2UserInfo desde un mapa de atributos (típico de proveedores OAuth2)
     */
    public static OAuth2UserInfo fromAttributes(Map<String, Object> attributes, String provider) {
        OAuth2UserInfo userInfo = new OAuth2UserInfo();
        userInfo.setProvider(provider);
        userInfo.setAttributes(attributes);
        
        // Mapear campos comunes de diferentes proveedores
        switch (provider.toLowerCase()) {
            case "google":
                userInfo.setEmail((String) attributes.get("email"));
                userInfo.setFirstName((String) attributes.get("given_name"));
                userInfo.setLastName((String) attributes.get("family_name"));
                userInfo.setFullName((String) attributes.get("name"));
                userInfo.setOauthId((String) attributes.get("sub"));
                userInfo.setPicture((String) attributes.get("picture"));
                userInfo.setLocale((String) attributes.get("locale"));
                break;
                
            case "microsoft":
            case "azure":
                userInfo.setEmail((String) attributes.get("mail"));
                if (userInfo.getEmail() == null) {
                    userInfo.setEmail((String) attributes.get("userPrincipalName"));
                }
                userInfo.setFirstName((String) attributes.get("givenName"));
                userInfo.setLastName((String) attributes.get("surname"));
                userInfo.setFullName((String) attributes.get("displayName"));
                userInfo.setOauthId((String) attributes.get("id"));
                break;
                
            case "github":
                userInfo.setEmail((String) attributes.get("email"));
                userInfo.setFullName((String) attributes.get("name"));
                if (userInfo.getFullName() != null) {
                    String[] nameParts = userInfo.getFullName().split(" ");
                    userInfo.setFirstName(nameParts[0]);
                    if (nameParts.length > 1) {
                        userInfo.setLastName(nameParts[1]);
                    }
                }
                userInfo.setOauthId(String.valueOf(attributes.get("id")));
                userInfo.setPicture((String) attributes.get("avatar_url"));
                break;
                
            default:
                // Mapeo genérico
                userInfo.setEmail((String) attributes.get("email"));
                userInfo.setFirstName((String) attributes.get("first_name"));
                userInfo.setLastName((String) attributes.get("last_name"));
                userInfo.setFullName((String) attributes.get("name"));
                userInfo.setOauthId((String) attributes.get("id"));
                break;
        }
        
        return userInfo;
    }
    
    /**
     * Crea OAuth2UserInfo para pruebas
     */
    public static OAuth2UserInfo createTestUser(String email, String name, String provider) {
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        return new OAuth2UserInfo(
            email,
            firstName,
            lastName,
            provider,
            provider + "-" + email.hashCode()
        );
    }
    
    // Getters y Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getOauthId() {
        return oauthId;
    }
    
    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public String toString() {
        return "OAuth2UserInfo{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", provider='" + provider + '\'' +
                ", oauthId='" + oauthId + '\'' +
                '}';
    }
}

