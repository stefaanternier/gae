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



import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.oauth.OauthInfo;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationEntity;

public class OauthKeyManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}


	public static void addKey(int oauthProviderId, String client_id, String client_secret, String redirect_uri) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		OauthConfigurationEntity conf = new OauthConfigurationEntity();
		conf.setOauthProviderId(oauthProviderId);
		conf.setClient_id(client_id);
		conf.setClient_secret(client_secret);
		conf.setRedirect_uri(redirect_uri);
		datastore.put(conf.toEntity());


//		try {
//			pm.makePersistent(conf);
//		} finally {
//			pm.close();
//		}
	}

	public static OauthConfigurationEntity getConfigurationObject(int authProviderId) {


		Key key = KeyFactory.createKey("OauthConfigurationJDO", authProviderId);
		try {
			Entity result = datastore.get(key);
			return new OauthConfigurationEntity(result);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}


//		Key key = KeyFactory.createKey(OauthConfigurationJDO.class.getSimpleName(), authProviderId);
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			OauthConfigurationJDO confJDO = pm.getObjectById(OauthConfigurationJDO.class, key);
//			return confJDO;
//		} finally {
//			pm.close();
//		}
	}

	public static OauthInfoList getClientInformation() {
		//PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
			OauthInfoList resultList = new OauthInfoList();
			Query q = new Query("OauthConfigurationJDO");
			PreparedQuery pq = datastore.prepare(q);
			for (Entity result : pq.asIterable()) {
				OauthInfo info = new OauthInfo();
				info.setClientId((String) result.getProperty("client_id"));
				info.setProviderId((int) result.getKey().getId());
				info.setRedirectUri((String) result.getProperty("redirect_uri"));
				resultList.addOauthInfo(info);
			}

//			javax.jdo.Query query = pm.newQuery(OauthConfigurationJDO.class);
//
//			List<OauthConfigurationJDO> list = (List<OauthConfigurationJDO>) query.execute();
//			OauthInfoList resultList = new OauthInfoList();
//			for (OauthConfigurationJDO conf : list) {
//				OauthInfo info = new OauthInfo();
//				info.setClientId(conf.getClient_id());
//				info.setProviderId(conf.getOauthProviderId());
//				info.setRedirectUri(conf.getRedirect_uri());
//				resultList.addOauthInfo(info);
//			}
			return resultList;
//		} finally {
//			pm.close();
//		}
	}
}
