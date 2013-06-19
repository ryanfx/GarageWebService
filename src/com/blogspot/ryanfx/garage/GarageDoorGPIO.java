/**
 * Copyright 2013 Ryan Shaw (ryanfx1@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.blogspot.ryanfx.garage;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GarageDoorGPIO implements GarageControl {
    
	//Do not lazy load, we'll set pins on boot of server. (Now!)
	private static GarageDoorGPIO instance = new GarageDoorGPIO();
	private final long ONE_SECOND = 1000;
	
	private GpioController controller;
	private GpioPinDigitalOutput garageTogglePin;
	private GpioPinDigitalInput garageSensorPin;
	
	private GarageDoorGPIO(){
		controller = GpioFactory.getInstance();
		garageTogglePin = controller.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.HIGH);
		//Pull down resistor used with the magnetic sensor which is normally open.
		//When the door closes, the switch is closed.
		garageSensorPin = controller.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
	}
	
	public static GarageDoorGPIO getInstance(){
		return instance;
	}

	@Override
	public synchronized void toggleDoor() {
		try {
			//Pull the garageTogglePin low, which activates the relay
			garageTogglePin.low();
			//Sleep for one second to ensure that the garage opener accepts it as a press
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			//Set it back to high, which disables the relay
			garageTogglePin.high();
		}
	}

	@Override
	public synchronized boolean isClosed() {
		PinState state = garageSensorPin.getState();
		//If state is low, the garage is open (due to pull down resistor and open circuit)
		//If state is high, the garage is closed.
		return state.isHigh();
	}

	@Override
	public synchronized boolean isOpen() {
		return !isClosed();
	}

	/**
	 * Returns true if the door will begin closing, false if the door is already closed
	 */
	@Override
	public synchronized boolean closeDoor() {
		if (isOpen()){
			toggleDoor();
			return true;
		}
		return false;
	}
}
