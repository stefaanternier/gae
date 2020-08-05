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

import java.util.List;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.Version;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.VersionJDO;
import org.codehaus.jettison.json.JSONException;

public class VersionManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	public static Version addVersion(Version v) {

		Entity e = new Entity("VersionJDO");
		e.setProperty("versionCode", v.getVersionCode());
		e.setProperty("payLoad", new Text(v.toString()));
		datastore.put(e);
		return toBean(e);
//		VersionJDO version = toJDO(v);
//		try {
//			return toBean(pm.makePersistent(version));
//		} finally{
//			pm.close();
//		}
	}
	
	public static Version getVersion(Integer versionCode){
		Query q = new Query("VersionJDO")
				.setFilter(new Query.FilterPredicate("versionCode", Query.FilterOperator.EQUAL, versionCode));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			return toBean(result);
		}
		return null;

	}

	private static Version toBean(Entity e) {
		Version v;
		try {
			v = (Version) JsonBeanDeserializer.deserialize(((Text)e.getProperty("payLoad")).getValue());

		} catch (JSONException ex) {
			v = new Version();
			v.setError(ex.getMessage());
			ex.printStackTrace();
		}
		v.setVersionCode(((Long)e.getProperty("versionCode")).intValue());
		return v;
	}

//	private static VersionJDO toJDO(Version v) {
//		VersionJDO jdo = new VersionJDO();
//		jdo.setVersionCode(v.getVersionCode());
//		jdo.setPayLoad(v.toString());
//		return jdo;
//	}
}
