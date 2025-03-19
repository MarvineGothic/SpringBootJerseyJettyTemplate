package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ExampleApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
//        SpringApplication.run(ExampleApp.class);
        new ExampleApp().configure(new SpringApplicationBuilder(ExampleApp.class)).run(args);
    }
}