package org.project.resumeboost.basic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigureClass implements WebMvcConfigurer {

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {

	// registry.addResourceHandler("/member/profile/**")
	// .addResourceLocations("file://192.168.23.211/손경락/saveFiles/member/profile/");
	// registry.addResourceHandler("/member/portfolio/**")
	// .addResourceLocations("file://192.168.23.211/손경락/saveFiles/member/portfolio/");
	// registry.addResourceHandler("/board/img/**")
	// .addResourceLocations("file://192.168.23.211/손경락/saveFiles/board/");
	// }
}