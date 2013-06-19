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
package com.blogspot.ryanfx.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class UserDAO {

	public Set<String> getUsers() throws IOException{
		Set<String> users = new HashSet<String>();
		File usersFile = getDefaultUsersFile();
		if (!usersFile.exists()){
			boolean created = usersFile.createNewFile();
			Logger.getLogger(UserDAO.class.getName()).info("Users file " + usersFile + " missing.  Created? " + created);
		}
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(usersFile));
			String line;
			while ((line = br.readLine()) != null) {
				//Remove any whitespace
				line = line.trim();
				//If line is not a comment...
				if (!line.startsWith("#")){
					users.add(line);
				}
			}
		}
		//It's good practice to close your streams in finally blocks
		finally{
			br.close();	
		}
		return users;
	}
	
	public boolean isValidUser(String email) throws IOException{
		return getUsers().contains(email);
	}
	
	private File getDefaultUsersFile(){
		String userHome = System.getProperty( "user.home" );
		File usersFile = new File(userHome, "garage_users");
		return usersFile;
	}
}
