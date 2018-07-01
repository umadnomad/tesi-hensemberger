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
package com.riccardofinazzi.hensemberger.tesi.common.spring.root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.riccardofinazzi.hensemberger.tesi.common.spring.root.annotation.RootEntity;
import com.riccardofinazzi.hensemberger.tesi.common.spring.root.annotation.RootRepo;
import com.riccardofinazzi.hensemberger.tesi.common.spring.root.annotation.RootService;

@Configuration
@ComponentScan(
	basePackages = { "com.riccardofinazzi.hensemberger.tesi.modules" },
	includeFilters = @Filter({ RootService.class, RootRepo.class, RootEntity.class })
)
/**
 * @author rfinazzi
 * class RootConfig
 */
public class RootConfig {

	@Bean
	public GpioController gpioDefault() {
		return GpioFactory.getInstance();
	}
}
