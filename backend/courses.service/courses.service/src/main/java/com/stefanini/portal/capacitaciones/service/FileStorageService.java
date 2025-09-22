package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.Material;
import com.stefanini.portal.capacitaciones.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FileStorageService {
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;
    
    @Value("${app.file.max-size:104857600}")
    private long maxFileSize;
    
    @Value("${app.file.allowed-types:pdf,video,mp4,avi,mov,doc,docx,txt,jpg,jpeg,png,gif}")
    private String allowedTypes;
    
    @Value("${server.servlet.context-path:/courses-service}")
    private String contextPath;
    
    // Subir archivo
    @Transactional(rollbackFor = Exception.class)
    public Material subirArchivo(MultipartFile archivo, String descripcion) throws IOException {
        try {
            // Validaciones
            validarArchivo(archivo);
            
            // Crear directorio si no existe
            Path directorio = Paths.get(uploadDir);
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
            }
            
            // Generar nombre único para el archivo
            String nombreOriginal = archivo.getOriginalFilename();
            String extension = obtenerExtension(nombreOriginal);
            String nombreArchivo = generarNombreUnico(extension);
            
            // Guardar archivo
            Path rutaArchivo = directorio.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            
            // Determinar tipo de material
            String tipoMaterial = determinarTipoMaterial(extension);
            
            // Crear URL de acceso
            String urlAcceso = contextPath + "/uploads/" + nombreArchivo;
            
            // Crear entidad Material
            Material material = new Material();
            material.setNombreArchivo(nombreArchivo);
            material.setNombreOriginal(nombreOriginal);
            material.setTipoMaterial(tipoMaterial);
            material.setExtension(extension);
            material.setTamañoBytes(archivo.getSize());
            material.setRutaArchivo(rutaArchivo.toString());
            material.setUrlAcceso(urlAcceso);
            material.setDescripcion(descripcion);
            material.setFechaSubida(LocalDateTime.now());
            material.setActivo(true);
            
            // Guardar en base de datos
            Material materialGuardado = materialRepository.save(material);
            
            return materialGuardado;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al subir archivo: " + e.getMessage(), e);
        }
    }
    
    // Obtener material por ID
    @Transactional(readOnly = true)
    public Optional<Material> obtenerMaterialPorId(Integer id) {
        return materialRepository.findById(id);
    }
    
    // Obtener todos los materiales
    @Transactional(readOnly = true)
    public List<Material> obtenerTodosLosMateriales() {
        return materialRepository.findByActivoTrue();
    }
    
    // Obtener materiales por tipo
    @Transactional(readOnly = true)
    public List<Material> obtenerMaterialesPorTipo(String tipoMaterial) {
        return materialRepository.findByTipoMaterialAndActivoTrue(tipoMaterial);
    }
    
    // Eliminar material (soft delete)
    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarMaterial(Integer id) {
        return materialRepository.findById(id)
                .map(material -> {
                    material.setActivo(false);
                    materialRepository.save(material);
                    return true;
                })
                .orElse(false);
    }
    
    // Eliminar archivo físicamente
    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarArchivoFisicamente(Integer id) {
        return materialRepository.findById(id)
                .map(material -> {
                    try {
                        // Eliminar archivo físico
                        Path rutaArchivo = Paths.get(material.getRutaArchivo());
                        if (Files.exists(rutaArchivo)) {
                            Files.delete(rutaArchivo);
                        }
                        
                        // Eliminar de base de datos
                        materialRepository.delete(material);
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException("Error al eliminar archivo físico: " + e.getMessage(), e);
                    }
                })
                .orElse(false);
    }
    
    // Obtener estadísticas de almacenamiento
    @Transactional(readOnly = true)
    public Object[] obtenerEstadisticasAlmacenamiento() {
        Long totalEspacio = materialRepository.getTotalEspacioUsado();
        List<Object[]> materialesPorTipo = materialRepository.countByTipoMaterial();
        Long totalMateriales = materialRepository.count();
        
        return new Object[]{totalEspacio, materialesPorTipo, totalMateriales};
    }
    
    // Métodos de validación
    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        
        if (archivo.getSize() > maxFileSize) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido (" + 
                    (maxFileSize / 1024 / 1024) + " MB)");
        }
        
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        
        String extension = obtenerExtension(nombreOriginal);
        if (!esTipoPermitido(extension)) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Tipos permitidos: " + allowedTypes);
        }
    }
    
    private boolean esTipoPermitido(String extension) {
        String[] tiposPermitidos = allowedTypes.split(",");
        for (String tipo : tiposPermitidos) {
            if (tipo.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }
    
    private String generarNombreUnico(String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid + "." + extension;
    }
    
    private String determinarTipoMaterial(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "pdf";
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
            case "flv":
            case "webm":
                return "video";
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return "imagen";
            case "doc":
            case "docx":
            case "txt":
                return "documento";
            default:
                return "archivo";
        }
    }
    
    // Método para obtener la ruta del archivo
    @Transactional(readOnly = true)
    public Path obtenerRutaArchivo(Integer materialId) {
        return materialRepository.findById(materialId)
                .map(material -> Paths.get(material.getRutaArchivo()))
                .orElse(null);
    }
    
    // Método para verificar si el archivo existe
    @Transactional(readOnly = true)
    public boolean archivoExiste(Integer materialId) {
        return materialRepository.findById(materialId)
                .map(material -> Files.exists(Paths.get(material.getRutaArchivo())))
                .orElse(false);
    }
}
