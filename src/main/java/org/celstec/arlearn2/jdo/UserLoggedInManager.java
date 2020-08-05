/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.jdo;

//import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import org.celstec.arlearn2.beans.run.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.jdo.classes.UserLoggedInEntity;

public class UserLoggedInManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	public static void submitUser(String email, String authToken) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserLoggedInEntity uli = new UserLoggedInEntity();
		uli.setKey(authToken.hashCode());
		uli.setUsername(User.normalizeEmail(email));
		datastore.put(uli.toEntity());
//		try {
//			pm.makePersistent(uli);
//		} finally {
//			pm.close();
//		}
	}
	
	public static void submitOauthUser(String id, String authToken) {
//		PersistenceManager	 pm = PMF.get().getPersistenceManager();
		UserLoggedInEntity uli = new UserLoggedInEntity();
		uli.setKey(authToken.hashCode());
		uli.setUsername(id);
        uli.setAuthToken(authToken);
		datastore.put(uli.toEntity());
//		try {
//			pm.makePersistent(uli);
//		} finally {
//			pm.close();
//		}
	}
	
	public static String getUser(String authToken) {
		if (authToken == null || authToken.equals("")) return null;
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (authToken.startsWith("GoogleLogin")) authToken = authToken.substring(authToken.indexOf("auth=")+5);
		Key key = KeyFactory.createKey(UserLoggedInEntity.KIND, authToken.hashCode());
		try {
			return new UserLoggedInEntity(datastore.get(key)).getUsername();
			//return ((UsersLoggedIn)pm.getObjectById(UsersLoggedIn.class, key)).getUsername();
		} catch (Exception e) {
			return null;
		}

	}
	
	

}
