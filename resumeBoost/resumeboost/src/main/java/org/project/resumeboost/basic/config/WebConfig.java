package org.project.resumeboost.basic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000/**", "*", "https://online-payment.kakaopay.com/**",
            "http://ec2-13-125-236-223.ap-northeast-2.compute.amazonaws.com/**", "http://localhost/**")
        .allowedMethods("GET", "POST", "DELETE", "PUT")
        .maxAge(300)
        .allowedHeaders("Authorization", "Cache-Controll", "Content-Type");
  }
}
