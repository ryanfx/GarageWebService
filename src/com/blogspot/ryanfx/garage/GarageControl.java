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

/**
 * Interface to allow controlling the garage door via different means.
 * I used the GarageDoorMemory to do this when I didn't have a fully configured
 * Raspberry Pi
 *
 */
public interface GarageControl {

	public static int OPEN=0;
	public static int OPENING=3;
	public static int CLOSED=1;
	public static int CLOSING=2;
	
	public void toggleDoor();
	public boolean closeDoor();
	public boolean isClosed();
	public boolean isOpen();
}