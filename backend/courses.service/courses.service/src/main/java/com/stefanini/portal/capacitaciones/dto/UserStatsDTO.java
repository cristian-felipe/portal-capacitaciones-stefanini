package com.stefanini.portal.capacitaciones.dto;

public class UserStatsDTO {
    
    private int totalCursos;
    private int cursosIniciados;
    private int cursosCompletados;
    private double progresoPromedio;
    private int tiempoTotalEstudio;
    private int totalInsignias;
    private int leccionesCompletadas;
    private int leccionesEnProgreso;
    
    // Constructores
    public UserStatsDTO() {}
    
    public UserStatsDTO(int totalCursos, int cursosIniciados, int cursosCompletados, 
                       double progresoPromedio, int tiempoTotalEstudio, int totalInsignias,
                       int leccionesCompletadas, int leccionesEnProgreso) {
        this.totalCursos = totalCursos;
        this.cursosIniciados = cursosIniciados;
        this.cursosCompletados = cursosCompletados;
        this.progresoPromedio = progresoPromedio;
        this.tiempoTotalEstudio = tiempoTotalEstudio;
        this.totalInsignias = totalInsignias;
        this.leccionesCompletadas = leccionesCompletadas;
        this.leccionesEnProgreso = leccionesEnProgreso;
    }
    
    // Getters y Setters
    public int getTotalCursos() {
        return totalCursos;
    }
    
    public void setTotalCursos(int totalCursos) {
        this.totalCursos = totalCursos;
    }
    
    public int getCursosIniciados() {
        return cursosIniciados;
    }
    
    public void setCursosIniciados(int cursosIniciados) {
        this.cursosIniciados = cursosIniciados;
    }
    
    public int getCursosCompletados() {
        return cursosCompletados;
    }
    
    public void setCursosCompletados(int cursosCompletados) {
        this.cursosCompletados = cursosCompletados;
    }
    
    public double getProgresoPromedio() {
        return progresoPromedio;
    }
    
    public void setProgresoPromedio(double progresoPromedio) {
        this.progresoPromedio = progresoPromedio;
    }
    
    public int getTiempoTotalEstudio() {
        return tiempoTotalEstudio;
    }
    
    public void setTiempoTotalEstudio(int tiempoTotalEstudio) {
        this.tiempoTotalEstudio = tiempoTotalEstudio;
    }
    
    public int getTotalInsignias() {
        return totalInsignias;
    }
    
    public void setTotalInsignias(int totalInsignias) {
        this.totalInsignias = totalInsignias;
    }
    
    public int getLeccionesCompletadas() {
        return leccionesCompletadas;
    }
    
    public void setLeccionesCompletadas(int leccionesCompletadas) {
        this.leccionesCompletadas = leccionesCompletadas;
    }
    
    public int getLeccionesEnProgreso() {
        return leccionesEnProgreso;
    }
    
    public void setLeccionesEnProgreso(int leccionesEnProgreso) {
        this.leccionesEnProgreso = leccionesEnProgreso;
    }
}

