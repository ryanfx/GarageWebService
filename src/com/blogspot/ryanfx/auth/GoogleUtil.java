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
package com.blogspot.ryanfx.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GoogleUtil {

	/**
	 * Returns an email address associated with an auth token, if it exists.
	 * @param token auth token
	 * @return
	 * @throws LoginException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String submitUserToken(String token) throws LoginException, IOException, ParseException{
		JSONObject jsonObject = null;
		String email = null;
		JSONParser parser = new JSONParser();
		String jsonResult = null;
		jsonResult = issueTokenGetRequest(token);
		if (jsonResult != null){
			jsonObject = (JSONObject) parser.parse(jsonResult);
			//Gets the email value from the json result
			email = (String) jsonObject.get("email");
			//Logs the request for our knowledge
			Logger.getLogger(GoogleUtil.class.getName()).severe("Garage request from user: " + email);
		}
		return email;
	}

	/**
	 * Sends the auth token to google and gets the json result.
	 * @param token auth token
	 * @return json result if valid request, null if invalid.
	 * @throws IOException
	 * @throws LoginException
	 */
	private static String issueTokenGetRequest(String token) throws IOException, LoginException {
		int timeout = 2000;
		URL u = new URL("https://www.googleapis.com/oauth2/v2/userinfo");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("Content-length", "0");
		c.setRequestProperty("Authorization", "OAuth " + token);
		c.setUseCaches(false);
		c.setAllowUserInteraction(false);
		c.setConnectTimeout(timeout);
		c.setReadTimeout(timeout);
		c.connect();
		int status = c.getResponseCode();
		if (status == HttpServletResponse.SC_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			return sb.toString();
		}else if (status == HttpServletResponse.SC_UNAUTHORIZED){
			Logger.getLogger(GoogleUtil.class.getName()).severe("Invalid token request: " + token);
		}
		return null;
	}
}
