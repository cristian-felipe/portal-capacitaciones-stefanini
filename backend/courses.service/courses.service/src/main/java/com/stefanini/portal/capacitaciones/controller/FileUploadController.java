package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.entity.Material;
import com.stefanini.portal.capacitaciones.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/archivos")
@CrossOrigin(origins = "*")
public class FileUploadController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/subir")
    public ResponseEntity<?> subirArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion) {
        
        try {
            Material material = fileStorageService.subirArchivo(archivo, descripcion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Archivo subido exitosamente");
            response.put("material", material);
            response.put("urlAcceso", material.getUrlAcceso());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de validación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al subir archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Integer id) {
        try {
            Optional<Material> materialOpt = fileStorageService.obtenerMaterialPorId(id);
            
            if (!materialOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Material material = materialOpt.get();
            if (!material.getActivo()) {
                return ResponseEntity.notFound().build();
            }
            
            Path rutaArchivo = fileStorageService.obtenerRutaArchivo(id);
            if (rutaArchivo == null || !fileStorageService.archivoExiste(id)) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new UrlResource(rutaArchivo.toUri());
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // Determinar tipo de contenido
            String contentType = determinarTipoContenido(material.getExtension());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + material.getNombreOriginal() + "\"")
                    .body(resource);
                    
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInformacionArchivo(@PathVariable Integer id) {
        Optional<Material> material = fileStorageService.obtenerMaterialPorId(id);
        
        if (!material.isPresent() || !material.get().getActivo()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(material.get());
    }
    
    @GetMapping
    public ResponseEntity<List<Material>> obtenerTodosLosArchivos() {
        List<Material> materiales = fileStorageService.obtenerTodosLosMateriales();
        return ResponseEntity.ok(materiales);
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Material>> obtenerArchivosPorTipo(@PathVariable String tipo) {
        List<Material> materiales = fileStorageService.obtenerMaterialesPorTipo(tipo);
        return ResponseEntity.ok(materiales);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArchivo(@PathVariable Integer id) {
        boolean eliminado = fileStorageService.eliminarMaterial(id);
        
        if (eliminado) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Archivo eliminado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}/fisico")
    public ResponseEntity<?> eliminarArchivoFisicamente(@PathVariable Integer id) {
        try {
            boolean eliminado = fileStorageService.eliminarArchivoFisicamente(id);
            
            if (eliminado) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Archivo eliminado físicamente");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasAlmacenamiento() {
        try {
            Object[] estadisticas = fileStorageService.obtenerEstadisticasAlmacenamiento();
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalEspacioUsado", estadisticas[0]);
            response.put("totalEspacioFormateado", formatearTamaño((Long) estadisticas[0]));
            response.put("materialesPorTipo", estadisticas[1]);
            response.put("totalMateriales", estadisticas[2]);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/subir-multiples")
    public ResponseEntity<?> subirMultiplesArchivos(
            @RequestParam("archivos") MultipartFile[] archivos,
            @RequestParam(value = "descripcion", required = false) String descripcion) {
        
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("archivosSubidos", 0);
            response.put("archivosConError", 0);
            response.put("materiales", new java.util.ArrayList<>());
            response.put("errores", new java.util.ArrayList<>());
            
            for (MultipartFile archivo : archivos) {
                try {
                    Material material = fileStorageService.subirArchivo(archivo, descripcion);
                    @SuppressWarnings("unchecked")
                    List<Material> materiales = (List<Material>) response.get("materiales");
                    materiales.add(material);
                    response.put("archivosSubidos", (Integer) response.get("archivosSubidos") + 1);
                } catch (Exception e) {
                    @SuppressWarnings("unchecked")
                    List<String> errores = (List<String>) response.get("errores");
                    errores.add("Error con archivo " + archivo.getOriginalFilename() + ": " + e.getMessage());
                    response.put("archivosConError", (Integer) response.get("archivosConError") + 1);
                }
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al subir archivos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Métodos auxiliares
    private String determinarTipoContenido(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/avi";
            case "mov":
                return "video/quicktime";
            case "wmv":
                return "video/x-ms-wmv";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }
    
    private String formatearTamaño(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
