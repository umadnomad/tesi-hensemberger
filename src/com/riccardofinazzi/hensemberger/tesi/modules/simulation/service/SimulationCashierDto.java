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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author rfinazzi
 * class SimulationCashierDto
 */
public class SimulationCashierDto {

	private String	name;
	private int		cash;
	
	@JsonIgnore
	private double	skill = Math.random();

	public SimulationCashierDto(String name, int cash) {
		this.name = name;
		this.cash = cash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public double getSkill() {
		return skill;
	}

	@Override
	public String toString() {
		return String.format("SimulationCashierDto [name=%s, cash=%s]", name, cash);
	}
}
