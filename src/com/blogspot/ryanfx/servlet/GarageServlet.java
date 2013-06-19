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
package com.blogspot.ryanfx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.blogspot.ryanfx.garage.GarageControl;
import com.blogspot.ryanfx.garage.GarageDoorGPIO;

public class GarageServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Logger.getLogger(getClass().getName()).info("Incoming get request...");
		//Gets / creates a session, will create a cookie and send to client.
		req.getSession();
		PrintWriter writer = resp.getWriter();
		String pathInfo = req.getPathInfo();
		//If the query is for the state of the garage...
		if (pathInfo.equals("/state")){
			String state = getState();
			writer.write(state);
		}	
	}

	private String getState() {
		Logger.getLogger(getClass().getName()).info("Getting state of garage");
		//Set default response
		String response = "error";
		GarageControl garageControl = GarageDoorGPIO.getInstance();
		boolean closedState = garageControl.isClosed();
		if (closedState){
			response = "closed";
		}else {
			response = "open";
		}
		return response;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Logger.getLogger(getClass().getName()).info("Incoming post request...");
		//Gets / creates a session, will create a cookie and send to client.
		req.getSession();
		GarageControl garageControl = GarageDoorGPIO.getInstance();
		String pathInfo = req.getPathInfo();
		PrintWriter writer = resp.getWriter();
		if (pathInfo.equals("/toggle")){
			Logger.getLogger(getClass().getName()).info("Toggling Garage");
			garageControl.toggleDoor();
		}
		else if (pathInfo.equals("/close")){
			Logger.getLogger(getClass().getName()).info("Closing Garage");
			boolean closing = garageControl.closeDoor();
			if (closing){
				writer.write("closing");
			}else {
				writer.write("closed");
			}
		}
	}
}