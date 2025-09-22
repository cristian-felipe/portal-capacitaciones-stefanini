package com.stefanini.portal_capacitaciones.content.service.controller;

import com.stefanini.portal_capacitaciones.content.service.entity.Material;
import com.stefanini.portal_capacitaciones.content.service.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Slf4j
public class MaterialController {

    private final MaterialService materialService;

    private static final String ATTACHMENT_DISPOSITION = "attachment";
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller funcionando");
    }
    
    @GetMapping
    public ResponseEntity<?> getAllMaterials() {
        try {
            var materials = materialService.getAllMaterials();
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
        log.info("Descargando archivo para material ID: {}", id);
        
        try {
            MaterialService.FileDownloadResult result = materialService.getFileForDownload(id);
            HttpHeaders headers = buildDownloadHeaders(result);
            
            log.info("Archivo descargado exitosamente para material ID: {} ({} bytes)", id, result.getFileContent().length);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result.getFileContent());
            
        } catch (RuntimeException e) {
            log.warn("Error descargando archivo para material ID: {} - {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error inesperado descargando archivo para material ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    private HttpHeaders buildDownloadHeaders(MaterialService.FileDownloadResult result) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(result.getContentType()));
        headers.setContentLength(result.getFileContent().length);
        headers.setContentDispositionFormData(ATTACHMENT_DISPOSITION, result.getFileName());
        return headers;
    }
}