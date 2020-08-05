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
import org.celstec.arlearn2.beans.run.ActionList;
//import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ActionEntity;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class ActionManager {


	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}



    private static final int ACTIONS_IN_LIST = 20;

//	public static List<Action> getActions(Long runId, String action, String userEmail, String generalItemId, String generalItemType) {
//		ArrayList<Action> returnProgressDefinitions = new ArrayList<Action>();
//
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Iterator<ActionJDO> it = getActionsJDO(pm, runId,action, userEmail, generalItemId, generalItemType).iterator();
//		while (it.hasNext()) {
//			returnProgressDefinitions.add(toBean((ActionJDO) it.next()));
//		}
//		return returnProgressDefinitions;
//
//	}
	
	
	public static Long addAction(Long runId, String action, String userEmail, Long generalItemId, String generalItemType, Long time) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActionEntity actionJDO = new ActionEntity();
		actionJDO.setAction(action);
		actionJDO.setGeneralItemId(generalItemId);
		actionJDO.setGeneralItemType(generalItemType);
		actionJDO.setRunId(runId);
		actionJDO.setTime(time);
		actionJDO.setUserId(userEmail);
		Entity entity = actionJDO.toEntity();
		datastore.put(entity);
		return new ActionEntity(entity).getId();

//		try {
//			pm.makePersistent(actionJDO);
//            return actionJDO.getId();
//		} finally {
//			pm.close();
//		}
	}

	public static ActionList runActions(Long runId) {
		ActionList returnList = new ActionList();
		Query q = new Query(ActionEntity.KIND)
				.setFilter(new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			returnList.addAction(new ActionEntity(result).toBean());
		}
		return returnList;

	}


	public static void deleteActions(Long runId) {
		Query q = new Query(ActionEntity.KIND)
				.setFilter(new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}
	
	public static void deleteActions(Long runId, String userId) {
		Query.CompositeFilter filter;

			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ActionEntity.COL_USERID, Query.FilterOperator.EQUAL, userId)
			);

		Query q = new Query(ActionEntity.KIND)
				.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}


    public static ActionList getActions(Long runId, Long from, Long until, String cursorString) {
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit(ACTIONS_IN_LIST);
			if (cursorString != null) {
				fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
			}
			Query.CompositeFilter filter;
			if (from == null) {
				filter = Query.CompositeFilterOperator.and(
						new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
						new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
				);
			} else if (until == null) {
				filter = Query.CompositeFilterOperator.and(
						new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
						new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
				);
			} else {
				filter = Query.CompositeFilterOperator.and(
						new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
						new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
						new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
				);
			}
			Query q = new Query(ActionEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
			q.setFilter(filter);
			PreparedQuery pq = datastore.prepare(q);

			ActionList returnList = new ActionList();
			QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
			for (Entity result : results) {
				ActionEntity object = new ActionEntity(result);
				returnList.addAction(object.toBean());
			}
			if (results.size() == ACTIONS_IN_LIST) {
				returnList.setResumptionToken(results.getCursor().toWebSafeString());
			}
			returnList.setServerTime(System.currentTimeMillis());
			return returnList;

    }

	public static ActionList getActions(Long runId, String user, Long from, Long until, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(ACTIONS_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ActionEntity.COL_USERID, Query.FilterOperator.EQUAL, user),
					new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ActionEntity.COL_USERID, Query.FilterOperator.EQUAL, user),
					new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ActionEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ActionEntity.COL_USERID, Query.FilterOperator.EQUAL, user),
					new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(ActionEntity.COL_TIME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}
		Query q = new Query(ActionEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		System.out.println("query is "+q);
		PreparedQuery pq = datastore.prepare(q);

		ActionList returnList = new ActionList();
		QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ActionEntity object = new ActionEntity(result);
			returnList.addAction(object.toBean());
		}
		if (results.size() == ACTIONS_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

	}
}
