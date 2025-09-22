package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.constants.AuthConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(AuthConstants.AUTH_OAUTH2_PATH)
public class OAuth2Controller {

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = Map.of(
            "message", AuthConstants.OAUTH2_LOGIN_ENDPOINT,
            "status", AuthConstants.STATUS_AVAILABLE
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success() {
        Map<String, String> response = Map.of(
            "message", AuthConstants.OAUTH2_LOGIN_SUCCESSFUL,
            "status", AuthConstants.STATUS_SUCCESS
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> failure() {
        Map<String, String> response = Map.of(
            "message", AuthConstants.OAUTH2_LOGIN_FAILED,
            "status", AuthConstants.STATUS_FAILURE
        );
        return ResponseEntity.badRequest().body(response);
    }
}

