package com.andre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition(
    info = @Info(title = "Courses API",
                version = "1.0",
                description = "Courses and participants API",
                contact = @Contact(
                        name = "Andre Carlos da Silva",
                        email = "andrecarlos@gmail.com")))
@EntityScan(basePackages = {"com.andre.course.model"})  // scan JPA entities
public class CoursesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursesApplication.class, args);
    }
}
