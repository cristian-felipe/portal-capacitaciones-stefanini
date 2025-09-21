package com.stefanini.portal.capacitaciones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth2 login endpoint");
        response.put("status", "available");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth2 login successful");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> failure() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth2 login failed");
        response.put("status", "failure");
        return ResponseEntity.badRequest().body(response);
    }
}

