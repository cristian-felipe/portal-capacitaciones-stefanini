package com.stefanini.portal.capacitaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CoursesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursesServiceApplication.class, args);
    }
}


