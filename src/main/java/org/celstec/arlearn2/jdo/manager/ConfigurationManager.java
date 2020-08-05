package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.cache.ConfigurationCache;

import org.celstec.arlearn2.jdo.classes.ConfigurationJDO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ConfigurationManager {

	
	public static String getValue(String key) {
		return null;
//		String cacheKey = ConfigurationCache.getInstance().getValue(key);
//		if (cacheKey!= null) return cacheKey;
//		Key keyDB = KeyFactory.createKey(ConfigurationJDO.class.getSimpleName(), key);
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			ConfigurationJDO confJDO = pm.getObjectById(ConfigurationJDO.class, keyDB);
//			ConfigurationCache.getInstance().storeKeyValue(key, confJDO.getValue());
//			return confJDO.getValue();
//		} catch (Exception e) {
//			ConfigurationJDO jdo = new ConfigurationJDO();
//			jdo.setKey(key);
//			jdo.setValue("");
//			pm.makePersistent(jdo);
//			return null;
//		} finally {
//			pm.close();
//		}
	}
}
