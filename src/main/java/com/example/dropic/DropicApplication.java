package com.example.dropic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(basePackages= {"services", "presentation.controllers"})
@EnableJpaRepositories("repositories")
@EntityScan("repositories.models")
public class DropicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DropicApplication.class, args);
    }

}
