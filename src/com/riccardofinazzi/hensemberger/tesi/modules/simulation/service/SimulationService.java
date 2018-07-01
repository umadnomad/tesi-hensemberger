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
package com.riccardofinazzi.hensemberger.tesi.modules.simulation.service;

import java.util.List;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.annotation.WebService;

@WebService
/**
 * @author rfinazzi
 * interface SimulationService
*/
public interface SimulationService {
	
	void start();
	List<SimulationCashierDto> getCashiers();
	void resetCashiers();
	int getTotal();
}