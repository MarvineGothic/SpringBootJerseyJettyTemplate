package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAsync
public class ExampleApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        new ExampleApp().configure(new SpringApplicationBuilder(ExampleApp.class)).run(args);
    }
}