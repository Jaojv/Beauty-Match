package com.beauty.com.MatchBeauty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Classe de configuração web do sistema
// Configura como os recursos estáticos são servidos
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configura os handlers de recursos estáticos
    // Define como os arquivos de upload são acessados via HTTP
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar acesso aos arquivos de upload
        // Mapeia URLs /uploads/** para a pasta física uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
} 