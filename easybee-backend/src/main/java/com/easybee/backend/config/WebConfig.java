package com.easybee.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Autorise toutes les routes
				.allowedOrigins("http://localhost:5173") // L'adresse de ton futur React
				.allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
	}
}