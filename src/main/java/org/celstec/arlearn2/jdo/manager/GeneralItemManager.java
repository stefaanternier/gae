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

import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import org.celstec.arlearn2.jdo.classes.GeneralItemEntity;

public class GeneralItemManager {
	private static final int ITEMS_IN_LIST = 10;
	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
//	private static final String params[] = new String[]{"gameId", "id", "type"};
//	private static final String paramsNames[] = new String[]{"gameParam", "generalItemIdParam", "typeParam"};
//	private static final String types[] = new String[]{"Long", "com.google.appengine.api.datastore.Key", "String"};


	public static Key addGeneralItem(GeneralItem bean) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		GeneralItemEntity gi = new GeneralItemEntity();
		gi.setGameId(bean.getGameId());
		if (bean.getId() != null) gi.setIdentifier(bean.getId());
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean.getDependsOn());
		if (bean.getDependsOn() != null) gi.setDependsOn(new Text(jbs.serialiseToJson().toString()));
		if (bean.getDescription() == null) bean.setDescription("");
		gi.setDescription(new Text(bean.getDescription()));
		gi.setScope(bean.getScope());
		gi.setLat(bean.getLat());
		gi.setLng(bean.getLng());
		gi.setName(bean.getName());
		gi.setRadius(bean.getRadius());

		gi.setType(bean.getType());
		gi.setIconUrl(bean.getIconUrl());
		gi.setDeleted(false);
        if (bean.getDeleted() != null) gi.setDeleted(bean.getDeleted());
		gi.setLastModificationDate(System.currentTimeMillis());
		jbs = new JsonBeanSerialiser(bean);
		gi.setPayload(new Text(jbs.serialiseToJson().toString()));
		return datastore.put(gi.toEntity());
	}

    public static GeneralItem getGeneralItem(Long itemId) {
		return getGeneralItem(KeyFactory.createKey(GeneralItemEntity.KIND,itemId));
    }

	public static GeneralItem getGeneralItem(Key key) {

		Entity result = null;
		try {
			result = datastore.get(key);
		} catch (EntityNotFoundException e) {

			return null;
		}
		return new GeneralItemEntity(result).toGeneralItem();
	}

	public static List<GeneralItem> getGeneralitems(Long gameId) {
		ArrayList<GeneralItem> itemsResult = new ArrayList<GeneralItem>();
		Query q = new Query(GeneralItemEntity.KIND)
				.setFilter(new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);
		//todo implementatie cursor
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(250));
		Iterator<Entity> it = results.iterator();
		while (it.hasNext()) {
			itemsResult.add(new GeneralItemEntity(it.next()).toGeneralItem());
		}
		return itemsResult;
	}


	public static GeneralItemList getGeneralitems(Long gameId, String cursorString) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(ITEMS_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}


		Query q = new Query(GeneralItemEntity.KIND)
				.setFilter(new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);


		GeneralItemList gil = new GeneralItemList();
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			gil.addGeneralItem(new GeneralItemEntity(result).toGeneralItem());
		}

		if (results.size() == ITEMS_IN_LIST) {
			gil.setResumptionToken(results.getCursor().toWebSafeString());
		}
		gil.setServerTime(System.currentTimeMillis());
		return gil;
	}

	
	public static List<GeneralItem> getGeneralitemsFromUntil(Long gameId, Long from, Long until) {
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
					new Query.FilterPredicate(GeneralItemEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
					new Query.FilterPredicate(GeneralItemEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);

		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
					new Query.FilterPredicate(GeneralItemEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(GeneralItemEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);

		}
		ArrayList<GeneralItem> itemsResult = new ArrayList<GeneralItem>();

		Query q = new Query(GeneralItemEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> it = pq.asIterator();

		while (it.hasNext()) {
			itemsResult.add(new GeneralItemEntity(it.next()).toGeneralItem());
		}

		return itemsResult;
	}	


	public static void deleteGeneralItem(long gameId) {

		Query q = new Query(GeneralItemEntity.KIND).setFilter(new Query.FilterPredicate(GeneralItemEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);

		Iterator<Entity> it = pq.asIterable().iterator();
		while (it.hasNext()) {
			datastore.delete(it.next().getKey());

		}

	}
	
	public static GeneralItem setStatusDeleted(long gameId, String itemId) {
		Key key = KeyFactory.createKey(GeneralItemEntity.KIND,itemId);
		Entity result = null;
		try {
			result = datastore.get(key);
			GeneralItemEntity jdo = new GeneralItemEntity(result);
			jdo.setDeleted(true);
			jdo.setLastModificationDate(System.currentTimeMillis());
			datastore.put(jdo.toEntity());
			return jdo.toGeneralItem();
		} catch (EntityNotFoundException e) {

			return null;
		}
	}

	public static void delete(Long itemId) {
		Key key =KeyFactory.createKey(GeneralItemEntity.KIND,itemId);
		datastore.delete(key);
	}
	
//	public static void deleteGeneralItem(long gameId, String itemId) {
//		delete(gameId, itemId, null);
//	}
	

//	private static String cursorString = null;
//
//	public static void updateAll() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//		Query query = pm.newQuery(GeneralItemJDO.class);
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
//		List<GeneralItemJDO> results = (List<GeneralItemJDO>) query.execute();
//		Iterator<GeneralItemJDO> it = (results).iterator();
//		int i = 0;
//		while (it.hasNext()) {
//			i++;
//			GeneralItemJDO object = it.next();
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
