/*
 * Copyright (C) 2018 - Riccardo Finazzi, ITIS P.Hensemberger

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 
 */
package com.riccardofinazzi.hensemberger.tesi.common.spring.web;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.annotation.WebEntity;
import com.riccardofinazzi.hensemberger.tesi.common.spring.web.annotation.WebRepo;
import com.riccardofinazzi.hensemberger.tesi.common.spring.web.annotation.WebService;

@Configuration
@EnableWebMvc
@ComponentScan(
	basePackages = { "com.riccardofinazzi.hensemberger.tesi.modules" },
	includeFilters = @Filter({ Controller.class, RestController.class, WebService.class, WebRepo.class, WebEntity.class })
)
/**
 * @author rfinazzi
 * class WebConfig
 */
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public GlobalControllerExceptionHandler globalControllerExceptionHandler() {
		return new GlobalControllerExceptionHandler();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		/* resource handler le risorse statiche */
		registry
		.addResourceHandler("/static/**")
		.addResourceLocations("/WEB-INF/static/");
		
		/* resource handler per i logs di tomcat */
		StringBuffer catalina = new StringBuffer("file:");
		catalina
		.append(System.getProperty("catalina.base"))
		.append(File.separator).append("logs").append(File.separator);
		
		registry
		.addResourceHandler("/logs/**")
		.addResourceLocations(catalina.toString());
	}
}