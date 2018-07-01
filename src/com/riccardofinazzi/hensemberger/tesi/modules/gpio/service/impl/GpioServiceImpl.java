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
package com.riccardofinazzi.hensemberger.tesi.modules.gpio.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.riccardofinazzi.hensemberger.tesi.common.spring.root.annotation.RootService;
import com.riccardofinazzi.hensemberger.tesi.modules.gpio.service.GpioService;
import com.riccardofinazzi.hensemberger.tesi.modules.gpio.service.InvalidPinActionException;

@RootService
/**
 * @author rfinazzi
 * class GpioServiceImpl
 * 
 * Integrazione con librerie pi4j.com
 * http://pi4j.com/usage.html
 */
public class GpioServiceImpl implements GpioService, InitializingBean {

	@Autowired
	private GpioController gpioService; // @formatter:off
										// ma perché quei mannaggia lo hanno
										// chiamato controller se è un servizio?
										// @formatter:on

	private static final Map<Integer, GpioPinDigital> map = new HashMap<>();

	@Override
	public void changeState(int pinNumber, boolean state) throws InvalidPinActionException {

		GpioPinDigital pin = parsePin(pinNumber);
		// explicitly set a state on the pin object
		if (pin instanceof GpioPinDigitalOutput)
			((GpioPinDigitalOutput) pin).setState(state);
		else
			throw new InvalidPinActionException("Il pin che si sta cercando di settare non è di input");
	}
	
	@Override
	public void pulseState(int pinNumber, long duration, boolean state) throws InvalidPinActionException {

		GpioPinDigital pin = parsePin(pinNumber);
		
		// use pulse method to set the pin to the HIGH state for
		// an explicit length of time in milliseconds

		// pin.pulse(1000);
		if (pin instanceof GpioPinDigitalOutput)
			((GpioPinDigitalOutput) pin).pulse(duration, parseState(state));
		else
			throw new InvalidPinActionException("Il pin che si sta cercando di pulsare non è di input");
	}

	@Override
	public boolean isHigh(int pinNumber) throws InvalidPinActionException {

		GpioPinDigital pin = parsePin(pinNumber);

		// get explicit state enumeration for the GPIO pin associated with the
		// button
		return pin.isHigh();
	}

	private GpioPinDigital parsePin(int i) throws InvalidPinActionException {

		GpioPinDigital result = map.get(i);

		if (null != result)
			return result;
		else
			throw new InvalidPinActionException("Pin non trovato");
	}
	
	private PinState parseState(boolean b) {

		if (b)
			return PinState.HIGH;
		else
			return PinState.LOW;
	}

	@Override
	/**
	 * Qui vengono impostati i pin
	 */
	public void afterPropertiesSet() throws Exception {

		/*
		 * @formatter:off
		 * 
         * provision gpio pin #02 as an input pin with its internal pull down resistor enabled
         * (configure pin edge to both rising and falling to get notified for HIGH and LOW state
         * changes)
        	GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(
        	RaspiPin.GPIO_02,				// PIN NUMBER
        	"MyButton",						// PIN FRIENDLY NAME (optional)
        	PinPullResistance.PULL_DOWN);	// PIN RESISTANCE (optional)	// maledizione
        																	// le resistenze
        																	// pulldown sono
        																	// probabilmente
        																	// già integrate
        																	// nel pcb di
        																	// raspberry
         * @formatter:off
         */
		
		GpioPinDigitalInput input2 =
	    		gpioService.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
		
		GpioPinDigitalInput input3 =
	    		gpioService.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
	    
	    GpioPinDigitalOutput output4 =
	    		gpioService.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.HIGH);
	    
	    map.put(2, input2);
	    map.put(3, input3);
	    map.put(4, output4);
	}
}