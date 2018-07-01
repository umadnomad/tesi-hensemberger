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
package com.riccardofinazzi.hensemberger.tesi.modules.simulation.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.riccardofinazzi.hensemberger.tesi.common.spring.web.annotation.WebService;
import com.riccardofinazzi.hensemberger.tesi.modules.simulation.service.SimulationCashierDto;
import com.riccardofinazzi.hensemberger.tesi.modules.simulation.service.SimulationService;

@WebService
@PropertySource("classpath:settings.properties")
/**
 * @author rfinazzi
 * class SimulationServiceImpl
 */
public class SimulationServiceImpl implements SimulationService, InitializingBean {

	@Value("${cashiers}")
	private int CASHIERS;
	
	@Value("${differentiate.cashiers.ability}")
	private boolean IS_DIFFERENT_CASHIER_ABILITY;
	
	@Value("${single.cashier.starting.amount}")
	private int CASHIER_STARTING_AMOUNT;

	@Value("${total.cash.to.finish}")
	private long TOTAL_CASH_TO_FINISH;

	@Value("${seconds.to.finish}")
	private long SECONDS_TO_FINISH;

	@Value("${transaction.delay.millis}")
	private long TRANSACTION_DELAY;

	@Value("${transaction.amount}")
	private int TRANSACTION_AMOUNT;
	
	@Value("${transaction.randomize.amount}")
	private boolean IS_RANDOMIZED_AMOUNT;

	@Value("${transaction.index.delta}")
	private float TRANSACTION_DELTA;
	
	@Value("${final.slowdown}")
	private boolean IS_FINAL_SLOWDOWN;

	@Value("${final.slowdown.percentage.trigger}")
	private int SLOWDOWN_PERCENTAGE_TRIGGER;
	
	@Value("${final.slowdown.percentage}")
	private int SLOWDOWN_PERCENTAGE;

	private float	transactionSpeedIndex;
	private Instant	endTime;

	private List<SimulationCashierDto> cashiers;

	public List<SimulationCashierDto> getCashiers() {
		return cashiers;
	}

	@Override
	public void start() {

		transactionSpeedIndex = 1;
		endTime = Instant.now().plusSeconds(SECONDS_TO_FINISH);
		
		Runnable runnable;
		Thread thread;
		for (SimulationCashierDto i : cashiers) {
			
			i.setCash(CASHIER_STARTING_AMOUNT);
			
			runnable = new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(TRANSACTION_DELAY);
							i.setCash(i.getCash() + computeTransaction(i));
						} catch (InterruptedException e) {
							/*
							System.out.println(
								"Target raggiunto, il thread " + Thread.currentThread().getName()
								+ " è stato terminato con successo."
							);
							*/
							break;
						}
					}
				}
			};

			thread = new Thread(runnable);
			thread.setName(i.getName());
			thread.start();
		}			
	}

	private int computeTransaction(SimulationCashierDto dto) {

		int totalCashNow = this.getTotal();
		
		/* System.out.println("total cash : " + totalCashNow); */
		if (totalCashNow > TOTAL_CASH_TO_FINISH)
			Thread.currentThread().interrupt();
		
		/* 
		 * @formatter:off
		 * 
		 * proporzione
		 * (elapsedTime) : X = seconds.to.finish : total.cash.to.finish
		 * per determinare che quantitativo di soldi totali
		 * dovrebbero esserci in cassa per finire entro il tempo stabilito
		 * 
		 * @formatter:on
		 */
		Duration elapsedTime = Duration.between(Instant.now(), endTime);
		
		long estimatedTotalToHaveNowInOrderToFinishInTime =
				TOTAL_CASH_TO_FINISH -
				( (elapsedTime.getSeconds() * TOTAL_CASH_TO_FINISH) / SECONDS_TO_FINISH );

		if (totalCashNow > estimatedTotalToHaveNowInOrderToFinishInTime)
			transactionSpeedIndex -= TRANSACTION_DELTA;
		else
			transactionSpeedIndex += TRANSACTION_DELTA;
		
		int transactionAmount = (int) (TRANSACTION_AMOUNT
				* (IS_RANDOMIZED_AMOUNT ? (Math.random() * transactionSpeedIndex) : transactionSpeedIndex));
		
		if (IS_DIFFERENT_CASHIER_ABILITY)
			transactionAmount *= dto.getSkill();

		if (IS_FINAL_SLOWDOWN) {
			
			long cashToFinish = TOTAL_CASH_TO_FINISH - totalCashNow; 
			if (cashToFinish < (TOTAL_CASH_TO_FINISH / 100) * SLOWDOWN_PERCENTAGE_TRIGGER)
				if (Instant.now().isBefore(endTime))
					transactionAmount -= (transactionAmount * SLOWDOWN_PERCENTAGE) / 100;
		}
		
		return transactionAmount;
	}

	@Override
	public /*synchronized*/ int getTotal() {
		
		int result = 0;
		
		Iterator<SimulationCashierDto> it = cashiers.iterator();
		while (it.hasNext())
			result += it.next().getCash();
		
		return result;
	}


	@Override
	public void resetCashiers() {
		Iterator<SimulationCashierDto> it = cashiers.iterator();
		while (it.hasNext())
			it.next().setCash(CASHIER_STARTING_AMOUNT);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		cashiers = new ArrayList<>();
		
		String name;
		SimulationCashierDto cashier;
		for (int i = 1; i <= CASHIERS; i++) {
			name = "Cashier_" + i;
			cashier = new SimulationCashierDto(name, CASHIER_STARTING_AMOUNT);
			cashiers.add(cashier);
		}
	}
}