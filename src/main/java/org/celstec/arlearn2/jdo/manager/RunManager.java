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
import com.google.appengine.api.datastore.*;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.classes.RunEntity;
import org.codehaus.jettison.json.JSONException;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class RunManager {
	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	private static final String params[] = new String[] { "id", "gameId", "owner", "title", "tagId" };
	private static final String paramsNames[] = new String[] { "runParam", "gameParam", "ownerEmailParam", "titleParam", "tagIdParam" };
	private static final String types[] = new String[] { "Long", "Long", "String", "String", "String" };

	public static Long addRun(String title, String owner, Long gameId, Long runId, Long startTime, Long serverCreationTime, Run run) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunEntity runJdo = new RunEntity();
		runJdo.setGameId(gameId);
		runJdo.setRunId(runId);
		runJdo.setOwner(owner);
		runJdo.setTitle(title);
		runJdo.setStartTime(startTime);
		runJdo.setServerCreationTime(serverCreationTime);
		runJdo.setLastModificationDate(serverCreationTime);
		runJdo.setPayload(new Text(run.toString()));
//		if (run.getRunConfig() != null) {
//
//			runJdo.setTagId(run.getRunConfig().getSelfRegistration()+"");
//		}
		return datastore.put(runJdo.toEntity()).getId();
//		try {
//			return pm.makePersistent(runJdo).getRunId();
//		} finally {
//			pm.close();
//		}
	}
	
	public static Long addRun(Run run) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunEntity runJdo = new RunEntity();
		runJdo.setGameId(run.getGameId());
		runJdo.setRunId(run.getRunId());
		runJdo.setTitle(run.getTitle());
		runJdo.setOwner(run.getOwner());
		runJdo.setStartTime(run.getStartTime());
		runJdo.setServerCreationTime(run.getServerCreationTime());
		runJdo.setLastModificationDate(run.getServerCreationTime());
		runJdo.setPayload(new Text(run.toString()));
//		if (run.getRunConfig() != null) {
//			runJdo.setTagId(run.getRunConfig().getSelfRegistration()+"");
//		}
		return datastore.put(runJdo.toEntity()).getId();

//		try {
//			return pm.makePersistent(runJdo).getRunId();
//		} finally {
//			pm.close();
//		}
	}

	public static Run getRun(Long runId) {
		Key key = KeyFactory.createKey(RunEntity.KIND,runId);

		Entity result = null;
		try {
			result = datastore.get(key);
		} catch (EntityNotFoundException e) {

			return null;
		}
		return new RunEntity(result).toBean();
	}

//	private static List<RunEntity> getRuns(PersistenceManager pm, Long runId, Long gameId, String owner, String title, String tagId) {
//		Query query = pm.newQuery(RunJDO.class);
//		Object args[] = { runId, gameId, owner, title, tagId };
//		if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
//			// query.setFilter("deleted == null");
//			return (List<RunJDO>) query.execute();
//		}
//		String filter = ManagerUtil.generateFilter(args, params, paramsNames);
//		String paramDecl = ManagerUtil.generateDeclareParameters(args, types, params, paramsNames);
//		query.setFilter(filter);
//		query.declareParameters(paramDecl);
//		return (List<RunJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));

		//todo query
//		return new ArrayList<RunEntity>();
//	}

	public static List<Run> getRunsWithAccount(String owner) {
		ArrayList<Run> itemsResult = new ArrayList<Run>();
		Query q = new Query(RunEntity.KIND)
				.setFilter(new Query.FilterPredicate(RunEntity.COL_OWNER, Query.FilterOperator.EQUAL, owner));

		PreparedQuery pq = datastore.prepare(q);
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(5));
		Iterator<Entity> it = results.iterator();
		while (it.hasNext()) {
			itemsResult.add(new RunEntity(it.next()).toBean());
		}
		return itemsResult;

	}

	public static List<Run> getRunsWithTagId(String tagId) {
		ArrayList<Run> itemsResult = new ArrayList<Run>();
		Query q = new Query(RunEntity.KIND)
				.setFilter(new Query.FilterPredicate(RunEntity.COL_TAGID, Query.FilterOperator.EQUAL, tagId));

		PreparedQuery pq = datastore.prepare(q);
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(5));
		Iterator<Entity> it = results.iterator();
		while (it.hasNext()) {
			itemsResult.add(new RunEntity(it.next()).toBean());
		}
		return itemsResult;

	}

	public static List<Run> getRunsWithGameId(long gameId) {
		ArrayList<Run> itemsResult = new ArrayList<Run>();
		Query q = new Query(RunEntity.KIND)
				.setFilter(new Query.FilterPredicate(RunEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		System.out.println(q);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(50));
		Iterator<Entity> it = results.iterator();
		while (it.hasNext()) {
			System.out.println("in iterator");
			itemsResult.add(new RunEntity(it.next()).toBean());
		}
		return itemsResult;

	}

//	public static List<Run> getRuns(Long runId, Long gameId, String owner, String title, String tagId) {
//		ArrayList<Run> itemsResult = new ArrayList<Run>();
//		com.google.appengine.api.datastore.Query.CompositeFilter filter = com.google.appengine.api.datastore.Query.CompositeFilterOperator.and(
//				new com.google.appengine.api.datastore.Query.FilterPredicate(RunEntity.COL_RUNID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, runId),
//				new com.google.appengine.api.datastore.Query.FilterPredicate(RunEntity.COL_GAMEID, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, gameId),
//				new com.google.appengine.api.datastore.Query.FilterPredicate(RunEntity.COL_OWNER, com.google.appengine.api.datastore.Query.FilterOperator.LESS_THAN_OR_EQUAL, owner),
//				new com.google.appengine.api.datastore.Query.FilterPredicate(RunEntity.COL_TAGID, com.google.appengine.api.datastore.Query.FilterOperator.LESS_THAN_OR_EQUAL, title),
//				new com.google.appengine.api.datastore.Query.FilterPredicate(RunEntity.COL_TITLE, com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, tagId)
//		);
//		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(RunEntity.KIND).setFilter(filter);
//		PreparedQuery pq = datastore.prepare(q);
//		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(5));
//		Iterator<Entity> it = results.iterator();
//		while (it.hasNext()) {
//			itemsResult.add(new RunEntity(it.next()).toBean());
//		}
//		return itemsResult;
//
//	}

//	public static void deleteRun(Long runId) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> runsToDelete = getRuns(pm, runId, null, null, null, null);
//			pm.deletePersistentAll(runsToDelete);
//		} finally {
//			pm.close();
//		}
//todo query
//	}
	
//	public static void deleteRun(PersistenceManager pm, RunJDO runJDO) {
//		pm.deletePersistent(runJDO);
//
//	}

	public static void setStatusDeleted(long runId) {
		try {
			Entity result = datastore.get(KeyFactory.createKey(RunEntity.KIND,runId));
			result.setProperty(RunEntity.COL_DELETED, true);
			datastore.put(result);
		} catch (EntityNotFoundException e) {


		}
		//todo query

//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
//			for (RunJDO jdo : deleteList) {
//				jdo.setDeleted(true);
//				jdo.setLastModificationDate(System.currentTimeMillis());
//			}
//		} finally {
//			pm.close();
//		}
	}

	public static void setLastModificationDate(long runId, long timestamp) {
		//todo query

//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
//			for (RunJDO jdo : deleteList) {
//				jdo.setLastModificationDate(timestamp);
//			}
//		} finally {
//			pm.close();
//		}
	}

	public static void updateRun(long runId, Run run) {
		//todo query

//		run.setGame(null);
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> updateList = getRuns(pm, runId, null, null, null, null);
//			for (RunJDO jdo : updateList) {
//				jdo.setPayload(new Text(run.toString()));
//				jdo.setLastModificationDate(System.currentTimeMillis());
//				jdo.setTitle(run.getTitle());
//				jdo.setGameId(run.getGameId());
//				jdo.setOwner(run.getOwner());
//				jdo.setStartTime(run.getStartTime());
//				jdo.setServerCreationTime(run.getServerCreationTime());
//				jdo.setDeleted(false);
//				if (run.getRunConfig() != null) {
//					jdo.setTagId(run.getRunConfig().getNfcTag());
//				}
//			}
//		} finally {
//			pm.close();
//		}
	}

//	private static Run toBean(RunEntity jdo) {
//		if (jdo == null)
//			return null;
//		return jdo.toBean();
//		Run run;
//		if (jdo.getPayload() != null) {
//			try {
//				run = (Run) JsonBeanDeserializer.deserialize(jdo.getPayload().getValue());
//			} catch (JSONException e) {
//				run = new Run();
//			}
//		} else {
//			run = new Run();
//		}
//		run.setRunId(jdo.getRunId());
//		run.setTitle(jdo.getTitle());
//		run.setGameId(jdo.getGameId());
//		run.setOwner(jdo.getOwner());
//		run.setTagId(jdo.getTagId());
//		run.setStartTime(jdo.getStartTime());
//		run.setDeleted(jdo.getDeleted());
//		run.setServerCreationTime(jdo.getServerCreationTime());
//		run.setLastModificationDate(jdo.getLastModificationDate());
//		return run;
//	}

	private final static int LIMIT = 10;
//
//	public static List<RunJDO> listAllRuns(PersistenceManager pm, String cursorString) {
//		javax.jdo.Query query = pm.newQuery(RunJDO.class);
//		if (cursorString != null) {
//			Cursor cursor = Cursor.fromWebSafeString(cursorString);
//			Map<String, Object> extensionMap = new HashMap<String, Object>();
//			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
//			query.setExtensions(extensionMap);
//		}
//		query.setRange(0, LIMIT);
//		return (List<RunJDO>) query.execute();
//	}
	
//	private static String cursorString = null;
//	public static void updateAll() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//		Query query = pm.newQuery(RunJDO.class);
//		if (cursorString != null) {
//
//			Cursor c = Cursor.fromWebSafeString(cursorString);
//			Map<String, Object> extendsionMap = new HashMap<String, Object>();
//			extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
//			query.setExtensions(extendsionMap);
//		}
//		query.setRange(0, 100);
//
//
////		query.setFilter("lastModificationDate == null");
//		List<RunJDO> results = (List<RunJDO>) query.execute();
//		Iterator<RunJDO> it = (results).iterator();
//		int i = 0;
//		while (it.hasNext()) {
//			i++;
//			RunJDO object = it.next();
//			if (object != null &&object.getLastModificationDate() == null) {
//				object.setLastModificationDate(System.currentTimeMillis());
//
//			}
//		}
//		Cursor c = JDOCursorHelper.getCursor(results);
//		cursorString = c.toWebSafeString();
//		} finally {
//			pm.close();
//		}
//	}

	

	

}
