package org.project.resumeboost.openApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Work24AppConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
