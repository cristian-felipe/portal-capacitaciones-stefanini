package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.Programa;
import com.stefanini.portal.capacitaciones.entity.Unidad;
import com.stefanini.portal.capacitaciones.entity.Leccion;
import com.stefanini.portal.capacitaciones.repository.ProgramaRepository;
import com.stefanini.portal.capacitaciones.repository.UnidadRepository;
import com.stefanini.portal.capacitaciones.repository.LeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DatosPruebaService {
    
    @Autowired
    private ProgramaRepository programaRepository;
    
    @Autowired
    private UnidadRepository unidadRepository;
    
    @Autowired
    private LeccionRepository leccionRepository;
    
    public void insertarDatosPrueba() {
        // Verificar si ya existen datos
        if (programaRepository.count() > 0) {
            return; // Ya existen datos
        }
        
        // Crear programas de prueba
        Programa programa1 = crearProgramaFullstack();
        Programa programa2 = crearProgramaAPIs();
        Programa programa3 = crearProgramaCloud();
        Programa programa4 = crearProgramaDataEngineer();
        
        programaRepository.save(programa1);
        programaRepository.save(programa2);
        programaRepository.save(programa3);
        programaRepository.save(programa4);
    }
    
    private Programa crearProgramaFullstack() {
        Programa programa = new Programa();
        programa.setTitulo("Desarrollo Fullstack con Angular y Spring Boot");
        programa.setDescripcion("Curso completo de desarrollo fullstack utilizando Angular para el frontend y Spring Boot para el backend");
        programa.setAreaConocimiento("Fullstack");
        
        // Unidad 1: Fundamentos de Angular
        Unidad unidad1 = new Unidad();
        unidad1.setTitulo("Fundamentos de Angular");
        unidad1.setOrden(1);
        unidad1.setPrograma(programa);
        
        Leccion leccion1_1 = new Leccion();
        leccion1_1.setTitulo("Introducción a Angular");
        leccion1_1.setOrden(1);
        leccion1_1.setTipoMaterial("video");
        leccion1_1.setUrlMaterial("https://www.youtube.com/watch?v=example1");
        leccion1_1.setUnidad(unidad1);
        
        Leccion leccion1_2 = new Leccion();
        leccion1_2.setTitulo("Componentes y Directivas");
        leccion1_2.setOrden(2);
        leccion1_2.setTipoMaterial("pdf");
        leccion1_2.setUrlMaterial("https://example.com/componentes-angular.pdf");
        leccion1_2.setUnidad(unidad1);
        
        unidad1.getLecciones().add(leccion1_1);
        unidad1.getLecciones().add(leccion1_2);
        
        // Unidad 2: Spring Boot Backend
        Unidad unidad2 = new Unidad();
        unidad2.setTitulo("Spring Boot Backend");
        unidad2.setOrden(2);
        unidad2.setPrograma(programa);
        
        Leccion leccion2_1 = new Leccion();
        leccion2_1.setTitulo("Configuración de Spring Boot");
        leccion2_1.setOrden(1);
        leccion2_1.setTipoMaterial("video");
        leccion2_1.setUrlMaterial("https://www.youtube.com/watch?v=example2");
        leccion2_1.setUnidad(unidad2);
        
        Leccion leccion2_2 = new Leccion();
        leccion2_2.setTitulo("REST APIs con Spring");
        leccion2_2.setOrden(2);
        leccion2_2.setTipoMaterial("link");
        leccion2_2.setUrlMaterial("https://spring.io/guides/gs/rest-service/");
        leccion2_2.setUnidad(unidad2);
        
        unidad2.getLecciones().add(leccion2_1);
        unidad2.getLecciones().add(leccion2_2);
        
        programa.getUnidades().add(unidad1);
        programa.getUnidades().add(unidad2);
        
        return programa;
    }
    
    private Programa crearProgramaAPIs() {
        Programa programa = new Programa();
        programa.setTitulo("APIs e Integraciones - DataPower y IBM Bus");
        programa.setDescripcion("Curso especializado en integración de APIs utilizando DataPower y IBM Integration Bus");
        programa.setAreaConocimiento("APIs e Integraciones");
        
        // Unidad 1: DataPower
        Unidad unidad1 = new Unidad();
        unidad1.setTitulo("IBM DataPower Gateway");
        unidad1.setOrden(1);
        unidad1.setPrograma(programa);
        
        Leccion leccion1_1 = new Leccion();
        leccion1_1.setTitulo("Introducción a DataPower");
        leccion1_1.setOrden(1);
        leccion1_1.setTipoMaterial("video");
        leccion1_1.setUrlMaterial("https://www.youtube.com/watch?v=datapower1");
        leccion1_1.setUnidad(unidad1);
        
        Leccion leccion1_2 = new Leccion();
        leccion1_2.setTitulo("Configuración de Políticas");
        leccion1_2.setOrden(2);
        leccion1_2.setTipoMaterial("pdf");
        leccion1_2.setUrlMaterial("https://example.com/datapower-policies.pdf");
        leccion1_2.setUnidad(unidad1);
        
        unidad1.getLecciones().add(leccion1_1);
        unidad1.getLecciones().add(leccion1_2);
        
        // Unidad 2: IBM Integration Bus
        Unidad unidad2 = new Unidad();
        unidad2.setTitulo("IBM Integration Bus");
        unidad2.setOrden(2);
        unidad2.setPrograma(programa);
        
        Leccion leccion2_1 = new Leccion();
        leccion2_1.setTitulo("Arquitectura de IBM Bus");
        leccion2_1.setOrden(1);
        leccion2_1.setTipoMaterial("video");
        leccion2_1.setUrlMaterial("https://www.youtube.com/watch?v=ibm-bus1");
        leccion2_1.setUnidad(unidad2);
        
        unidad2.getLecciones().add(leccion2_1);
        
        programa.getUnidades().add(unidad1);
        programa.getUnidades().add(unidad2);
        
        return programa;
    }
    
    private Programa crearProgramaCloud() {
        Programa programa = new Programa();
        programa.setTitulo("Cloud Computing - AWS y Azure");
        programa.setDescripcion("Curso completo de computación en la nube con AWS y Microsoft Azure");
        programa.setAreaConocimiento("Cloud");
        
        // Unidad 1: AWS
        Unidad unidad1 = new Unidad();
        unidad1.setTitulo("Amazon Web Services");
        unidad1.setOrden(1);
        unidad1.setPrograma(programa);
        
        Leccion leccion1_1 = new Leccion();
        leccion1_1.setTitulo("Fundamentos de AWS");
        leccion1_1.setOrden(1);
        leccion1_1.setTipoMaterial("video");
        leccion1_1.setUrlMaterial("https://www.youtube.com/watch?v=aws-fundamentals");
        leccion1_1.setUnidad(unidad1);
        
        Leccion leccion1_2 = new Leccion();
        leccion1_2.setTitulo("EC2 y S3");
        leccion1_2.setOrden(2);
        leccion1_2.setTipoMaterial("link");
        leccion1_2.setUrlMaterial("https://aws.amazon.com/ec2/");
        leccion1_2.setUnidad(unidad1);
        
        unidad1.getLecciones().add(leccion1_1);
        unidad1.getLecciones().add(leccion1_2);
        
        programa.getUnidades().add(unidad1);
        
        return programa;
    }
    
    private Programa crearProgramaDataEngineer() {
        Programa programa = new Programa();
        programa.setTitulo("Data Engineering con Python y Spark");
        programa.setDescripcion("Curso de ingeniería de datos utilizando Python, Apache Spark y herramientas de Big Data");
        programa.setAreaConocimiento("Data Engineer");
        
        // Unidad 1: Python para Data
        Unidad unidad1 = new Unidad();
        unidad1.setTitulo("Python para Data Science");
        unidad1.setOrden(1);
        unidad1.setPrograma(programa);
        
        Leccion leccion1_1 = new Leccion();
        leccion1_1.setTitulo("Pandas y NumPy");
        leccion1_1.setOrden(1);
        leccion1_1.setTipoMaterial("video");
        leccion1_1.setUrlMaterial("https://www.youtube.com/watch?v=pandas-numpy");
        leccion1_1.setUnidad(unidad1);
        
        Leccion leccion1_2 = new Leccion();
        leccion1_2.setTitulo("Visualización con Matplotlib");
        leccion1_2.setOrden(2);
        leccion1_2.setTipoMaterial("pdf");
        leccion1_2.setUrlMaterial("https://example.com/matplotlib-guide.pdf");
        leccion1_2.setUnidad(unidad1);
        
        unidad1.getLecciones().add(leccion1_1);
        unidad1.getLecciones().add(leccion1_2);
        
        // Unidad 2: Apache Spark
        Unidad unidad2 = new Unidad();
        unidad2.setTitulo("Apache Spark");
        unidad2.setOrden(2);
        unidad2.setPrograma(programa);
        
        Leccion leccion2_1 = new Leccion();
        leccion2_1.setTitulo("Introducción a Spark");
        leccion2_1.setOrden(1);
        leccion2_1.setTipoMaterial("video");
        leccion2_1.setUrlMaterial("https://www.youtube.com/watch?v=spark-intro");
        leccion2_1.setUnidad(unidad2);
        
        unidad2.getLecciones().add(leccion2_1);
        
        programa.getUnidades().add(unidad1);
        programa.getUnidades().add(unidad2);
        
        return programa;
    }
}
