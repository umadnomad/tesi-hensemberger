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
package com.riccardofinazzi.hensemberger.tesi.modules.gpio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.RestMessageResponse;

@RestController
/**
 * @author rfinazzi
 * class GpioController
 */
public class GpioController {
	
	@Autowired
	private GpioService gpioService;
	
//	private int i = 0;

	@GetMapping("/gpio/isHigh")
	public /*boolean*/ RestMessageResponse<Boolean> isHigh(@RequestParam Integer pinNumber) throws InvalidPinActionException {

//		int i = 0;
//		if (++i < 10)
//			return false;
//		else {
//			i = 0;
//			return true;
//		}
		
		RestMessageResponse<Boolean> resp = new RestMessageResponse<>();
		
		try {
			resp.setData(gpioService.isHigh(pinNumber));
			resp.setStatus("OK");
		} catch (Exception e) {
			resp.setStatus("KO");
			resp.setMessage(e.getClass().getCanonicalName() + ": " + e.getMessage());
		}
		
		return resp;
	}
}
