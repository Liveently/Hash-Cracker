package ru.kosolap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Разрешаем все пути
                        .allowedOrigins("http://localhost:8081") // Разрешаем запросы с UI
                        .allowedMethods("GET", "POST", "PATCH") // Разрешаем нужные методы
                        .allowCredentials(true);
            }
        };
    }
}
