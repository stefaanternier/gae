package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.List;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;
import com.google.appengine.api.datastore.*;

import org.celstec.arlearn2.beans.notification.DeviceDescription;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
//import org.celstec.arlearn2.beans.store.CategoryList;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.CategoryEntity;
//import org.celstec.arlearn2.jdo.classes.GCMDevicesRegistryJDO;

public class GCMDevicesRegistryManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	public static String KIND = "GCMDevicesRegistryJDO";


	public static void addDevice(GCMDeviceDescription deviceDes) {

		Entity entity = new Entity(KIND);
		entity.setProperty("account", deviceDes.getAccount());
		entity.setProperty("registrationId", deviceDes.getRegistrationId());
		entity.setProperty("deviceId", deviceDes.getDeviceUniqueIdentifier());
		entity.setProperty("packageIdentifier", deviceDes.getPackageIdentifier());
		datastore.put(entity);
	}
	
	public static List<DeviceDescription> getDeviceTokens(String account) {
		Query q = new Query(KIND)
				.setFilter(new com.google.appengine.api.datastore.Query.FilterPredicate("account", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account));
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<DeviceDescription> resultList = new ArrayList<DeviceDescription>();

		for (Entity result : pq.asIterable()) {

			GCMDeviceDescription gcmReturn = new GCMDeviceDescription();
			gcmReturn.setAccount((String)result.getProperty("account"));
			gcmReturn.setRegistrationId((String) result.getProperty("registrationId"));
			gcmReturn.setPackageIdentifier((String)result.getProperty("packageIdentifier"));
			gcmReturn.setDeviceUniqueIdentifier((String)result.getProperty("deviceId"));
			resultList.add(gcmReturn);
		}
		return resultList;

//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			Query query = pm.newQuery(GCMDevicesRegistryJDO.class);
//			query.setFilter("account == accountParam");
//			query.declareParameters("String accountParam");
//			List<GCMDevicesRegistryJDO> list = (List<GCMDevicesRegistryJDO>) query.execute(account);
//			ArrayList<DeviceDescription> result = new ArrayList<DeviceDescription>(list.size());
//			for (GCMDevicesRegistryJDO jdo : list) {
//				result.add(toBean(jdo));
//			}
//			return result;
//		}finally {
//			pm.close();
//		}
		
	}
//
//	private static GCMDeviceDescription toBean(GCMDevicesRegistryJDO jdo) {
//		if (jdo == null) return null;
//		GCMDeviceDescription gcmReturn = new GCMDeviceDescription();
//		gcmReturn.setAccount(jdo.getAccount());
//		gcmReturn.setRegistrationId(jdo.getRegistrationId());
//        gcmReturn.setPackageIdentifier(jdo.getPackageIdentifier());
//		return gcmReturn;
//	}
}
