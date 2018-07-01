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

var interval;
var loginPin = 2;
var startPin = 3;

$(document).ready(function() {
	$.get("/tesi/simulation/resetCashiers");
	checkLoginPinState();
});

function checkLoginPinState() {
	$.ajax({
		url : "/tesi/gpio/isHigh",
		data : {
			"pinNumber" : loginPin
		}
	}).done(function(resp) {

		if (resp.status == 'KO')
			return errorPage();
			
		if (!resp.data) {
			console.log('login pin is low.');
			setTimeout(checkLoginPinState, 1000);
		} else {
			console.log('login pin is high.');
			ping();
		}

	}).fail(function() {
		setTimeout(checkLoginPinState, 5000);
	});
}

function checkStartPinState() {
	$.ajax({
		url : "/tesi/gpio/isHigh",
		data : {
			"pinNumber" : startPin
		}
	}).done(function(resp) {

		if (resp.status == 'KO')
			return errorPage();
		
		if (!resp.data) {
			console.log('start pin is low.');
			setTimeout(checkStartPinState, 1000);
		} else {
			console.log('start pin is high.');
			startSimulation();
		}
		
	}).fail(function() {
		setTimeout(checkStartPinState, 5000);
	});
}

function ping() {
	$.ajax({
		url : "/tesi/ping"
	}).done(function(resp) {

		if (resp == 'pong') {
			$('.jumbotron').fadeOut('slow');
			$('.cs-loader-inner').fadeOut('slow');
			setTimeout(function() {
				$('#jsonResponse').fadeIn('slow', function() {
					interval = setInterval(getCashiers, 100);
					checkStartPinState();
				});
			}, 600);
		} else {
			setTimeout(ping, 1000);
		}
	}).fail(function() {
		setTimeout(ping, 5000);
	});
}

function startSimulation() {
	$.ajax({
		method : 'post',
		url : "/tesi/simulation/start"
	}).fail(function() {
		setTimeout(startSimulation, 5000);
	});
}

function getCashiers() {
	$.get("/tesi/simulation/getCashiers", function(resp) {

		if (resp.status == 'KO')
			return errorPage();
		
		updateView(resp);

		if (resp.fullVault) {
			clearInterval(interval);
			console.log('getCashiers() has been stopped.')
		}
	});
}

function updateView(resp) {

	var tbody = $('#jsonResponse').find('tbody');

	tbody.empty();

	$.each(resp.data, function(k, v) {
		var row = '<tr>'
		// +'<th scope="row">'+ (k+1) + '</th>'
		+ '<td>' + v.name + '</td>' + '<td>' + v.cash + '</td>' + '</tr>';

		tbody.append(row);
	});

	$('#jsonResponse h1').text('Totale: ' + resp.total);
}

function errorPage() {
	window.location.replace('/tesi/static/error.html');
}