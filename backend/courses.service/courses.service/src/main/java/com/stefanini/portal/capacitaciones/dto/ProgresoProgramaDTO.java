package com.stefanini.portal.capacitaciones.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProgresoProgramaDTO {
    
    private Long programaId;
    private String tituloPrograma;
    private String descripcionPrograma;
    private String areaConocimiento;
    private String estadoPrograma; // 'inscrito', 'en_progreso', 'completado'
    private Double progresoGeneral; // Porcentaje promedio del programa
    private Integer totalLecciones;
    private Integer leccionesCompletadas;
    private Integer leccionesEnProgreso;
    private Integer leccionesInscritas;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActualizacion;
    
    // Constructores
    public ProgresoProgramaDTO() {}
    
    public ProgresoProgramaDTO(Long programaId, String tituloPrograma, String descripcionPrograma, 
                              String areaConocimiento, String estadoPrograma, Double progresoGeneral,
                              Integer totalLecciones, Integer leccionesCompletadas, 
                              Integer leccionesEnProgreso, Integer leccionesInscritas,
                              LocalDateTime fechaCreacion, LocalDateTime fechaUltimaActualizacion) {
        this.programaId = programaId;
        this.tituloPrograma = tituloPrograma;
        this.descripcionPrograma = descripcionPrograma;
        this.areaConocimiento = areaConocimiento;
        this.estadoPrograma = estadoPrograma;
        this.progresoGeneral = progresoGeneral;
        this.totalLecciones = totalLecciones;
        this.leccionesCompletadas = leccionesCompletadas;
        this.leccionesEnProgreso = leccionesEnProgreso;
        this.leccionesInscritas = leccionesInscritas;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }
    
    // Getters y Setters
    public Long getProgramaId() {
        return programaId;
    }
    
    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }
    
    public String getTituloPrograma() {
        return tituloPrograma;
    }
    
    public void setTituloPrograma(String tituloPrograma) {
        this.tituloPrograma = tituloPrograma;
    }
    
    public String getDescripcionPrograma() {
        return descripcionPrograma;
    }
    
    public void setDescripcionPrograma(String descripcionPrograma) {
        this.descripcionPrograma = descripcionPrograma;
    }
    
    public String getAreaConocimiento() {
        return areaConocimiento;
    }
    
    public void setAreaConocimiento(String areaConocimiento) {
        this.areaConocimiento = areaConocimiento;
    }
    
    public String getEstadoPrograma() {
        return estadoPrograma;
    }
    
    public void setEstadoPrograma(String estadoPrograma) {
        this.estadoPrograma = estadoPrograma;
    }
    
    public Double getProgresoGeneral() {
        return progresoGeneral;
    }
    
    public void setProgresoGeneral(Double progresoGeneral) {
        this.progresoGeneral = progresoGeneral;
    }
    
    public Integer getTotalLecciones() {
        return totalLecciones;
    }
    
    public void setTotalLecciones(Integer totalLecciones) {
        this.totalLecciones = totalLecciones;
    }
    
    public Integer getLeccionesCompletadas() {
        return leccionesCompletadas;
    }
    
    public void setLeccionesCompletadas(Integer leccionesCompletadas) {
        this.leccionesCompletadas = leccionesCompletadas;
    }
    
    public Integer getLeccionesEnProgreso() {
        return leccionesEnProgreso;
    }
    
    public void setLeccionesEnProgreso(Integer leccionesEnProgreso) {
        this.leccionesEnProgreso = leccionesEnProgreso;
    }
    
    public Integer getLeccionesInscritas() {
        return leccionesInscritas;
    }
    
    public void setLeccionesInscritas(Integer leccionesInscritas) {
        this.leccionesInscritas = leccionesInscritas;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }
    
    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }
}
