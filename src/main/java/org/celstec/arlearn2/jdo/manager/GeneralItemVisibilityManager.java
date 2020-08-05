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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.run.GeneralItemVisibility;
import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GeneralItemVisibilityEntity;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;


public class GeneralItemVisibilityManager {
	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	protected static final Logger logger = Logger
			.getLogger(GeneralItemVisibilityManager.class.getName());

	public static final int VISIBLE_STATUS = 1;
	public static final int DISAPPEARED_STATUS = 2;

	private static final String params[] = new String[] { "runId",
			"generalItemId", "email", "status" };
	private static final String paramsNames[] = new String[] { "runIdParam",
			"generalItemIdParam", "emailParam", "statusParam" };
	private static final String types[] = new String[] { "Long", "Long",
			"String", "Integer" };

	public static void setItemVisible(Long generalItemId, Long runId,
			String email, Integer status, long timeStamp) {
		List<GeneralItemVisibilityEntity> listVisiblity = getGeneralitemVisibility(runId, generalItemId, email, status);
			if (listVisiblity == null || listVisiblity.isEmpty()) {

				VisibleGeneralItemsCache.getInstance().removeGeneralItemList(
						runId, email, status);
				GeneralItemVisibilityEntity visJdo = new GeneralItemVisibilityEntity();
				visJdo.setEmail(email);
				visJdo.setGeneralItemId(generalItemId);
				visJdo.setRunId(runId);
				visJdo.setStatus(status);
				visJdo.setTimeStamp(timeStamp);
				visJdo.setLastModificationDate(System.currentTimeMillis());
				datastore.put(visJdo.toEntity());
			} else {
				for (GeneralItemVisibilityEntity vis : listVisiblity) {
					if (vis.getTimeStamp() == null
							|| !vis.getTimeStamp().equals(timeStamp)) {
						vis.setTimeStamp(timeStamp);
						VisibleGeneralItemsCache.getInstance()
								.removeGeneralItemList(runId, email, status);
					}
				}
			}
	}

	public static void updateAccount(String fromAccount, String toAccount,
			Long runId) {
		//todo query
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			Query query = pm.newQuery(GeneralItemVisibilityEntity.class);
//			String filter = null;
//			String params = null;
//			Object args[] = null;
//			filter = "runId == runIdParam & email == emailParam";
//			params = "Long runIdParam, String emailParam";
//			args = new Object[] { runId, fromAccount };
//
//			query.setFilter(filter);
//			query.declareParameters(params);
//			Iterator<GeneralItemVisibilityJDO> it = ((List<GeneralItemVisibilityJDO>) query
//					.executeWithArray(args)).iterator();
//			while (it.hasNext()) {
//				it.next().setEmail(toAccount);
//			}
//		} finally {
//			pm.close();
//		}

	}

	public static HashMap<Long, Long> getVisibleItems(Long runId, String email) {
		return getItems(runId, email, VISIBLE_STATUS);
	}

	private static String cursorString = null;

//	public static void updateAll() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			Query query = pm.newQuery(GeneralItemVisibilityJDO.class);
//			if (cursorString != null) {
//				logger.severe("starting from " + cursorString);
//
//				Cursor c = Cursor.fromWebSafeString(cursorString);
//				Map<String, Object> extendsionMap = new HashMap<String, Object>();
//				extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
//				query.setExtensions(extendsionMap);
//			}
//			query.setRange(0, 100);
//			// query.setFilter("lastModificationDate == null");
//			// query.setOrdering("lastModificationDate desc");
//			List<GeneralItemVisibilityJDO> results = (List<GeneralItemVisibilityJDO>) query
//					.execute();
//			Iterator<GeneralItemVisibilityJDO> it = (results).iterator();
//			int i = 0;
//			logger.severe("hasNext" + it.hasNext());
//			while (it.hasNext()) {
//				i++;
//
//				GeneralItemVisibilityJDO object = it.next();
//				if (object != null && object.getLastModificationDate() == null) {
//					object.setLastModificationDate(System.currentTimeMillis());
//				}
//			}
//			Cursor c = JDOCursorHelper.getCursor(results);
//			cursorString = c.toWebSafeString();
//			logger.severe("cursorString" + cursorString);
//
//		} finally {
//			pm.close();
//		}
//	}


	public static  List<GeneralItemVisibility> getGeneralitemsFromUntil(Long runId, String account) { //, Long from, Long until

		com.google.appengine.api.datastore.Query.CompositeFilter filter;
//		if (from == null && until == null) {
			filter = com.google.appengine.api.datastore.Query.CompositeFilterOperator.and(
					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_EMAIL, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account),
					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_RUNID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, runId)
			);
//		} else if (from == null) {
//			filter = com.google.appengine.api.datastore.Query.CompositeFilterOperator.and(
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_EMAIL, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_RUNID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, runId),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_LASTMODIFICATIONDATE, com.google.appengine.api.datastore.Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
//			);
//		} else if (until == null) {
//			filter = com.google.appengine.api.datastore.Query.CompositeFilterOperator.and(
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_EMAIL, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_RUNID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, runId),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_LASTMODIFICATIONDATE, com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
//			);
//		} else {
//			filter = com.google.appengine.api.datastore.Query.CompositeFilterOperator.and(
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_EMAIL, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, account),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_RUNID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, runId),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_LASTMODIFICATIONDATE, com.google.appengine.api.datastore.Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
//					new com.google.appengine.api.datastore.Query.FilterPredicate(GeneralItemVisibilityEntity.COL_LASTMODIFICATIONDATE, com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
//			);
//		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(GeneralItemVisibilityEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<GeneralItemVisibility> returnProgressDefinitions = new ArrayList<GeneralItemVisibility>();
		Iterator<Entity> it = pq.asIterable().iterator();

		while (it.hasNext()) {
			returnProgressDefinitions.add(new GeneralItemVisibilityEntity(it
					.next()).toBean());
		}
		return returnProgressDefinitions;
	}

	public static List<GeneralItemVisibility> getGeneralitemsFromUntilOld(
			Long runId, String email, Long from, Long until) {
		ArrayList<GeneralItemVisibility> returnProgressDefinitions = new ArrayList<GeneralItemVisibility>();
		return returnProgressDefinitions;
		//todo query;
//		if (runId == null || email == null)
//			return returnProgressDefinitions;
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Query query = pm.newQuery(GeneralItemVisibilityJDO.class);
//		String filter = null;
//		String params = null;
//		Object args[] = null;
//		if (from == null) {
//			filter = "runId == runIdParam & email == emailParam & lastModificationDate <= untilParam";
//			params = "Long runIdParam, String emailParam, Long untilParam";
//			args = new Object[] { runId, email, until };
//		} else if (until == null) {
//			filter = "runId == runIdParam & email == emailParam & lastModificationDate >= fromParam";
//			params = "Long runIdParam, String emailParam, Long fromParam";
//			args = new Object[] { runId, email, from };
//		} else {
//			filter = "runId == runIdParam & email == emailParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
//			params = "Long runIdParam, String emailParam, Long fromParam, Long untilParam";
//			args = new Object[] { runId, email, from, until };
//		}
//
//		query.setFilter(filter);
//		query.declareParameters(params);
//		Iterator<GeneralItemVisibilityJDO> it = ((List<GeneralItemVisibilityJDO>) query
//				.executeWithArray(args)).iterator();
//		while (it.hasNext()) {
//			returnProgressDefinitions.add(toBean((GeneralItemVisibilityJDO) it
//					.next()));
//		}
//		return returnProgressDefinitions;
	}

	private static GeneralItemVisibility toBean(GeneralItemVisibilityEntity jdo) {

		if (jdo == null)
			return null;
		return jdo.toBean();
//		GeneralItemVisibility giv = new GeneralItemVisibility();
//		giv.setEmail(jdo.getEmail());
//		giv.setGeneralItemId(jdo.getGeneralItemId());
//		giv.setLastModificationDate(jdo.getLastModificationDate());
//		giv.setRunId(jdo.getRunId());
//		giv.setTimeStamp(jdo.getTimeStamp());
//		giv.setStatus(jdo.getStatus());
//		return giv;
	}

	public static HashMap<Long, Long> getItems(Long runId, String email,
			int status) {
		HashMap<Long, Long> returnProgressDefinitions = new HashMap<Long, Long>();
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Iterator<GeneralItemVisibilityJDO> it = getGeneralitemVisibility(pm,
//				runId, null, email, status).iterator();
//		while (it.hasNext()) {
//			GeneralItemVisibilityJDO gi = ((GeneralItemVisibilityJDO) it.next());
//			if (gi.getTimeStamp() == null)
//				gi.setTimeStamp(0l);
//			returnProgressDefinitions.put(gi.getGeneralItemId(),
//					gi.getTimeStamp());
//		}
		//todo query
		return returnProgressDefinitions;
	}

	private static List<GeneralItemVisibilityEntity> getGeneralitemVisibility(
			Long runId, Long generalItemId,
			String email, Integer status) {
		//todo query
//		Query query = pm.newQuery(GeneralItemVisibilityJDO.class);
//		Object args[] = { runId, generalItemId, email, status };
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args,
//				types, params, paramsNames));
//		return (List<GeneralItemVisibilityEntity>) query
//				.executeWithArray(ManagerUtil.filterOutNulls(args));
		return null;
	}

	public static void delete(Long runId, String email) {
		VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId,
				email, GeneralItemVisibilityManager.VISIBLE_STATUS);
		VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId,
				email, GeneralItemVisibilityManager.DISAPPEARED_STATUS);
		delete(runId, null, email, null);
	}

	public static void delete(Long runId, Long generalItemId, String email,
			Integer status) {
//todo query
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<GeneralItemVisibilityJDO> deleteList = getGeneralitemVisibility(
//					pm, runId, generalItemId, email, status);
//			pm.deletePersistentAll(deleteList);
//		} finally {
//			pm.close();
//		}
	}

}
