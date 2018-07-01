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
package com.riccardofinazzi.hensemberger.tesi.modules.simulation;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.RestMessageResponse;

/**
 * @author rfinazzi
 * class SimulationRestResponse<T>
*/
public class SimulationRestResponse<T> extends RestMessageResponse<T> {

	private int total;
	private boolean fullVault;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isFullVault() {
		return fullVault;
	}

	public void setFullVault(boolean fullVault) {
		this.fullVault = fullVault;
	}
}
