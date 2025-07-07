package org.project.resumeboost.security;

import java.util.Arrays;

import org.project.resumeboost.security.filter.JWTCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig { // security 설정

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(cs -> cs.disable());

    http.authorizeHttpRequests(authorize -> authorize
        .anyRequest().permitAll());

    http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.cors(HttpSecurityCorsConfigure -> {
      HttpSecurityCorsConfigure.configurationSource(configurationSource());
    });

    http.formLogin(login -> login
        .loginPage("/member/login")
        .usernameParameter("userEmail")
        .passwordParameter("userPw")
        .loginProcessingUrl("/member/login")
        .successHandler(customAuthenticationSuccessHandler())
        .failureHandler(customAuthenticationFailureHandler())
        .failureUrl("/main"));

    http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 체크

    http.logout(out -> out
        .logoutRequestMatcher(new AntPathRequestMatcher("/memeber/logout"))
        .logoutSuccessUrl("/")

    );

    return http.build();
  }

  @Bean
  public CorsConfigurationSource configurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(
        Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  private AuthenticationFailureHandler customAuthenticationFailureHandler() {

    return new CustomAuthenticationFailureHandler();
  }

  private AuthenticationSuccessHandler customAuthenticationSuccessHandler() {

    return new CustomAuthenticationSuccessHandler();
  }

}
