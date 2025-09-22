package com.stefanini.portal_capacitaciones.content.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Data
public class AwsS3Config {
    
    private String bucketName;
    private String region;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    
    @Bean
    public S3Client s3Client() {
        var builder = S3Client.builder()
                .region(Region.of(region));
        
        // Si se proporcionan credenciales, usarlas
        if (accessKey != null && !accessKey.isEmpty() && 
            secretKey != null && !secretKey.isEmpty()) {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            builder.credentialsProvider(StaticCredentialsProvider.create(credentials));
        }
        
        // Si se proporciona un endpoint personalizado (para S3-compatible services)
        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
            builder.forcePathStyle(true);
        }
        
        return builder.build();
    }
}
