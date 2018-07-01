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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.RestMessageResponse;
import com.riccardofinazzi.hensemberger.tesi.modules.gpio.service.GpioService;
import com.riccardofinazzi.hensemberger.tesi.modules.simulation.service.SimulationCashierDto;
import com.riccardofinazzi.hensemberger.tesi.modules.simulation.service.SimulationService;

@RestController
@PropertySource("classpath:settings.properties")
/**
 * @author rfinazzi
 * class SimulationController
*/
public class SimulationController {
	
	@Autowired
	private SimulationService simService;
	
	@Autowired
	private GpioService gpioService;
	
	@Value("${total.cash.to.finish}")
	private long TOTAL_CASH_TO_FINISH;
	
	@PostMapping("/simulation/start")
	public void startSimulation() {
		
		simService.start();
	}
	
	@GetMapping("/simulation/getCashiers")
	public RestMessageResponse<List<SimulationCashierDto>> getCashiers() {
		
		SimulationRestResponse<List<SimulationCashierDto>> resp = new SimulationRestResponse<>();
		
		try {		
			int total = simService.getTotal();
			
			resp.setData(simService.getCashiers());		
			resp.setTotal(total);		
			
			if (total < TOTAL_CASH_TO_FINISH)
				resp.setFullVault(false);
			else {
				resp.setFullVault(true);
				gpioService.pulseState(4, 5000, false);
			}
			
			resp.setStatus("OK");
			
		} catch (Exception e) {
			resp.setStatus("KO");
			resp.setMessage(e.getClass().getCanonicalName() + ": " + e.getMessage());
		}
		return resp;
	}
	
	@GetMapping("/simulation/resetCashiers")
	public void resetCashiers() {
		
		simService.resetCashiers();
	}
}
