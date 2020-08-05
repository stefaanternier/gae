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
package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.*;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescription;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.IOSDevicesRegistryJDO;

import com.google.appengine.api.blobstore.BlobKey;

public class IOSDevicesRegistryManager {


	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	public static String KIND = "IOSDevicesRegistryJDO";

	public static void addDevice(APNDeviceDescription deviceDes) {
		Entity entity = new Entity(KIND);
		entity.setProperty("account", deviceDes.getAccount());
		entity.setProperty("deviceToken", deviceDes.getDeviceToken());

		entity.setProperty("bundleIdentifier", deviceDes.getBundleIdentifier());
		entity.setProperty("deviceUniqueIdentifier", deviceDes.getDeviceUniqueIdentifier());
		datastore.put(entity);

	}
	
	public static List<DeviceDescription> getDeviceTokens(String account) {

		Query q = new Query(KIND)
				.setFilter(new com.google.appengine.api.datastore.Query.FilterPredicate("account", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account));
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<DeviceDescription> resultList = new ArrayList<DeviceDescription>();

		for (Entity result : pq.asIterable()) {

			APNDeviceDescription gcmReturn = new APNDeviceDescription();
			gcmReturn.setAccount((String)result.getProperty("account"));
			gcmReturn.setDeviceToken((String) result.getProperty("deviceToken"));
			gcmReturn.setBundleIdentifier((String)result.getProperty("bundleIdentifier"));
			gcmReturn.setDeviceUniqueIdentifier((String)result.getProperty("deviceUniqueIdentifier"));
			resultList.add(gcmReturn);
		}
		return resultList;




		
	}

	
}
