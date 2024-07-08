package org.example.mosaic_bot.config;

import org.example.mosaic_bot.web.MosaicWeb;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Value("${app.code-generator-url}")
    private String baseUrlForCodeGeneratorService;
    @Value("${app.admin-login}")
    private String adminUsername;
    @Value("${app.admin-password}")
    private String adminPassword;

    @Bean
    public MosaicWeb mosaicWeb(){
        return new MosaicWeb(baseUrlForCodeGeneratorService, adminUsername, adminPassword);
    }
}
