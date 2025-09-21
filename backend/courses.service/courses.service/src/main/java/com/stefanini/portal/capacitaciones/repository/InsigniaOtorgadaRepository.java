package com.stefanini.portal.capacitaciones.repository;

import com.stefanini.portal.capacitaciones.entity.InsigniaOtorgada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsigniaOtorgadaRepository extends JpaRepository<InsigniaOtorgada, Long> {
    
    // Buscar insignias por usuario
    @Query("SELECT io FROM InsigniaOtorgada io WHERE io.usuarioId = :usuarioId")
    List<InsigniaOtorgada> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Contar insignias por usuario
    @Query("SELECT COUNT(io) FROM InsigniaOtorgada io WHERE io.usuarioId = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Verificar si usuario ya tiene una insignia específica
    @Query("SELECT COUNT(io) > 0 FROM InsigniaOtorgada io WHERE io.usuarioId = :usuarioId AND io.insignia.id = :insigniaId")
    boolean existsByUsuarioIdAndInsigniaId(@Param("usuarioId") UUID usuarioId, @Param("insigniaId") Long insigniaId);
}

