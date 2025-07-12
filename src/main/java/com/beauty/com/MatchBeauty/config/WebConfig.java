package com.beauty.com.MatchBeauty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar acesso aos arquivos de upload
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
} 