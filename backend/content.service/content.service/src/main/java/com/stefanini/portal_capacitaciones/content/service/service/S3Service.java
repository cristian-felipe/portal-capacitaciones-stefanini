package com.stefanini.portal_capacitaciones.content.service.service;

import com.stefanini.portal_capacitaciones.content.service.config.AwsS3Config;
import com.stefanini.portal_capacitaciones.content.service.config.CoursesServiceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    
    private final S3Client s3Client;
    private final AwsS3Config s3Config;
    private final CoursesServiceConfig coursesServiceConfig;
    
    /**
     * Construye la ruta completa del archivo basándose en la ruta relativa
     * @param relativePath Ruta relativa del archivo
     * @return Ruta completa del archivo
     */
    public String buildFullFilePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        
        // Si ya es una ruta absoluta, usarla tal como está
        if (Paths.get(relativePath).isAbsolute()) {
            return relativePath;
        }
        
        // Construir la ruta completa usando la configuración del courses service
        return coursesServiceConfig.buildFullFilePath(relativePath);
    }
    
    /**
     * Sube un archivo a S3
     * @param filePath Ruta del archivo local (puede ser relativa o absoluta)
     * @param originalFileName Nombre original del archivo
     * @return La clave S3 del archivo subido
     */
    public String uploadFile(String filePath, String originalFileName) throws IOException {
        try {
            // Construir la ruta completa del archivo
            String fullFilePath = buildFullFilePath(filePath);
            log.debug("Ruta original: {}, Ruta completa: {}", filePath, fullFilePath);
            
            Path path = Paths.get(fullFilePath);
            if (!Files.exists(path)) {
                throw new IOException("El archivo no existe: " + fullFilePath);
            }
            
            // Generar clave única para S3
            String s3Key = generateS3Key(originalFileName);
            
            // Leer el archivo
            byte[] fileContent = Files.readAllBytes(path);
            
            // Crear la solicitud de subida
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(s3Key)
                    .contentType(getContentType(originalFileName))
                    .contentLength((long) fileContent.length)
                    .build();
            
            // Subir el archivo
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileContent));
            
            log.info("Archivo subido exitosamente a S3: {} -> {}", originalFileName, s3Key);
            return s3Key;
            
        } catch (S3Exception e) {
            log.error("Error al subir archivo a S3: {}", e.getMessage(), e);
            throw new IOException("Error al subir archivo a S3: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si un archivo existe en S3
     * @param s3Key Clave del archivo en S3
     * @return true si el archivo existe
     */
    public boolean fileExists(String s3Key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.error("Error al verificar existencia del archivo en S3: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Elimina un archivo de S3
     * @param s3Key Clave del archivo en S3
     */
    public void deleteFile(String s3Key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Archivo eliminado de S3: {}", s3Key);
        } catch (S3Exception e) {
            log.error("Error al eliminar archivo de S3: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Genera una URL presignada para acceder al archivo
     * @param s3Key Clave del archivo en S3
     * @param expirationMinutes Minutos de expiración de la URL
     * @return URL presignada
     */
    public String generatePresignedUrl(String s3Key, int expirationMinutes) {
        // TODO: Implementar cuando se agregue la dependencia del presigner
        log.warn("URL presignada no implementada - S3Key: {}", s3Key);
        return null;
    }
    
    /**
     * Genera una clave única para S3
     * @param originalFileName Nombre original del archivo
     * @return Clave S3 única
     */
    private String generateS3Key(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);
        
        return String.format("materials/%s/%s%s", timestamp, uuid, extension);
    }
    
    /**
     * Obtiene la extensión del archivo
     * @param fileName Nombre del archivo
     * @return Extensión con punto
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
    
    /**
     * Descarga un archivo desde S3
     * @param s3Key Clave del archivo en S3
     * @return Contenido del archivo como array de bytes
     */
    public byte[] downloadFile(String s3Key) {
        try {
            log.info("Descargando archivo desde S3: {}", s3Key);
            
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            byte[] fileContent = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
            
            log.info("Archivo descargado exitosamente desde S3: {} ({} bytes)", s3Key, fileContent.length);
            return fileContent;
            
        } catch (Exception e) {
            log.error("Error descargando archivo desde S3: {}", s3Key, e);
            throw new RuntimeException("Error descargando archivo desde S3: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el tipo de contenido basado en la extensión del archivo
     * @param fileName Nombre del archivo
     * @return Tipo de contenido MIME
     */
    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        return switch (extension) {
            case ".pdf" -> "application/pdf";
            case ".doc" -> "application/msword";
            case ".docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls" -> "application/vnd.ms-excel";
            case ".xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case ".ppt" -> "application/vnd.ms-powerpoint";
            case ".pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case ".txt" -> "text/plain";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".mp4" -> "video/mp4";
            case ".mp3" -> "audio/mpeg";
            case ".zip" -> "application/zip";
            default -> "application/octet-stream";
        };
    }
}
