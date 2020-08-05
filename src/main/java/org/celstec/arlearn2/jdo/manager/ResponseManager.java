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
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.jdo.classes.ResponseEntity;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;

//import org.celstec.arlearn2.jdo.classes.UserJDO;

public class ResponseManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

//	public static final String params[] = new String[]{"runId",  "generalItemId", "userEmail", "timeStamp", "revoked"};
//	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "userEmailParam", "timeStampParam", "revokedParam"};
//	private static final String types[] = new String[]{"Long", "Long", "String", "String", "Long", "Boolean"};

	private static final int RESPONSES_IN_LIST = 10;
	
	public static long addResponse(Long generalItemId, String responseValue, Long runId, String userEmail, Long timeStamp, Double lat, Double lng) {
		ResponseEntity responseRecord = new ResponseEntity();
		responseRecord.setGeneralItemId(generalItemId);
		responseRecord.setResponseValue(responseValue);
		responseRecord.setRunId(runId);
		responseRecord.setUserId(userEmail);
		responseRecord.setTimeStamp(timeStamp);
		responseRecord.setLastModificationDate(System.currentTimeMillis());
		responseRecord.setRevoked(false);
        if (lat != null) responseRecord.setLat(lat);
        if (lng != null) responseRecord.setLng(lng);
		Entity toStore = responseRecord.toEntity();
		System.out.println("userid is "+responseRecord.getUserId());
        datastore.put(toStore);
        return toStore.getKey().getId();
//		try {
//			pm.makePersistent(responseRecord);
//		} finally {
//			pm.close();
//		}
//        return responseRecord.getId();
	}
	
	public static Response revokeResponse(Long runId, Long generalItemId, String userEmail, Long timestamp){
		Query.CompositeFilter	filter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
				new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, generalItemId),
				new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.EQUAL, timestamp),
				new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, userEmail)
		);
		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			ResponseEntity resultE = new ResponseEntity(result);

			resultE.setRevoked(true);
			datastore.put(resultE.toEntity());
			return resultE.toBean();

		}
		Response r = new Response();
		r.setError("could not retrieve response from database, hence revoking is not possible");
		return r;

	}

    public static Response revokeResponse(Long responseId, String userId) {

		Key key = KeyFactory.createKey(ResponseEntity.KIND, responseId);

		ResponseEntity result = null;
		try {
			System.out.println("key is "+key);
			result = new ResponseEntity(datastore.get(key));
			System.out.println("result is "+result);
			if (result.getUserId().equals(userId)) {
				System.out.println("in equals "+result);
				result.setRevoked(true);
				datastore.delete(key);
			}

//			datastore.put(result.toEntity());
			return result.toBean();
		} catch (EntityNotFoundException e) {

			Response r = new Response();
			r.setError("could not retrieve response from database, hence revoking is not possible");
			return r;
		}

    }
	
	public static List<Response> getResponse(Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {

		Query.CompositeFilter	filter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
				new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, generalItemId),
				new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.EQUAL, timestamp),
				new Query.FilterPredicate(ResponseEntity.COL_REVOKED, Query.FilterOperator.EQUAL, revoked),
				new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, userEmail)

		);
		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		ResponseList returnList = new ResponseList();
		for (Entity result : pq.asIterable()) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}

		return returnList.getResponses();
	}

	public static List<Response> getResponseForRunId(Long runId) {

		Query.CompositeFilter	filter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
				new Query.FilterPredicate(ResponseEntity.COL_REVOKED, Query.FilterOperator.EQUAL, false)

		);
		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		ResponseList returnList = new ResponseList();
		for (Entity result : pq.asIterable()) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}

		return returnList.getResponses();
	}

	public static ResponseList getResponse(Long runId, Long from, Long until, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(RESPONSES_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}

		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		ResponseList returnList = new ResponseList();
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}
		if (results.size() == RESPONSES_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

	}

	public static ResponseList getResponse(Long runId, Long itemId, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(RESPONSES_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;

			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId)
			);


		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		ResponseList returnList = new ResponseList();
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}
		if (results.size() == RESPONSES_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

	}

    public static ResponseList getResponse(Long runId, Long itemId, Long from, Long until, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(RESPONSES_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}

		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		ResponseList returnList = new ResponseList();
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}
		if (results.size() == RESPONSES_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

    }

    public static ResponseList getResponse(Long runId, Long itemId, String fullId, Long from, Long until, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(RESPONSES_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, fullId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, fullId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_GENERATLITEMID, Query.FilterOperator.EQUAL, itemId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, fullId),
					new Query.FilterPredicate(ResponseEntity.COL_TIMESTAMP, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}

		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		ResponseList returnList = new ResponseList();
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ResponseEntity object = new ResponseEntity(result);
			returnList.addResponse(object.toBean());
		}
		if (results.size() == RESPONSES_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

    }
	
//	private static List<ResponseJDO> getResponse(PersistenceManager pm, Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {
//		Query query = pm.newQuery(ResponseJDO.class);
//		Object args [] = new Object[]{runId, generalItemId, userEmail, timestamp, revoked};
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//		return ((List<ResponseJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
//	}


	public static void deleteResponses(Long runId, String email) {

		Query.CompositeFilter	filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ResponseEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
					new Query.FilterPredicate(ResponseEntity.COL_USERID, Query.FilterOperator.EQUAL, email)
			);
		Query q = new Query(ResponseEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}

}
