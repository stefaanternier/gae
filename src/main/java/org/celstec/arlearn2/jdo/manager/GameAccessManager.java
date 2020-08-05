package org.celstec.arlearn2.jdo.manager;

import java.util.*;


import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.jdo.classes.GameAccessEntity;

public class GameAccessManager {
	private static final int ACCESS_IN_LIST = 5;
	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	public static GameAccess addGameAccess(String localID, int accountType, long gameId, int accesRights) {
		GameAccessEntity gameAccess = new GameAccessEntity();
		gameAccess.setAccessRights(accesRights);
		gameAccess.setLocalId(localID);
		gameAccess.setAccountType(accountType);
		gameAccess.setGameId(gameId);
		gameAccess.setUniqueId();
		gameAccess.setLastModificationDateGame(System.currentTimeMillis());
		datastore.put(gameAccess.toEntity());
		return gameAccess.toBean();
	}
	
	public static void resetGameAccessLastModificationDate(long gameId) {
		long lastModifiation = System.currentTimeMillis();

		Query q = new Query(GameAccessEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(new Query.FilterPredicate(GameAccessEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);
		List<GameAccess> returnList = new ArrayList<GameAccess>();
		for (Entity result : pq.asIterable()) {
			GameAccessEntity object = new GameAccessEntity(result);
			object.setLastModificationDateGame(lastModifiation);
			datastore.put(object.toEntity());
		}
	}


	public static GameAccessList getGameList(int accountType, String localId, String cursorString, long from) {
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(ACCESS_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		GameAccessList returnList = new GameAccessList();
		Query q = new Query(GameAccessEntity.KIND);
		Query.CompositeFilter accountFilter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(GameAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
				new Query.FilterPredicate(GameAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
				new Query.FilterPredicate(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
		);
		q.setFilter(accountFilter);
		q.addSort(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			GameAccessEntity object = new GameAccessEntity(result);
			returnList.addGameAccess(object.toBean());
		}
		if (results.size() == ACCESS_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;
	}

	public static List<GameAccess> getGameList(int accountType, String localId, Long from, Long until) {
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GameAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(GameAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GameAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(GameAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(GameAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(GameAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(GameAccessEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}
		Query q = new Query(GameAccessEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		List<GameAccess> returnList = new ArrayList<GameAccess>();

		for (Entity result : pq.asIterable()) {
			GameAccessEntity object = new GameAccessEntity(result);
			returnList.add(object.toBean());
		}
		return returnList;
	}
	
	public static List<GameAccess> getGameList(long gameId) {
		Query q = new Query(GameAccessEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(new Query.FilterPredicate(GameAccessEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);
		List<GameAccess> returnList = new ArrayList<GameAccess>();
		for (Entity result : pq.asIterable()) {
			GameAccessEntity object = new GameAccessEntity(result);
			returnList.add(object.toBean());
		}
		return returnList;
	}

	


	public static void removeGameAccess(String localID, int accountType, Long gameIdentifier) {
			Key key = KeyFactory.createKey(GameAccessEntity.KIND, accountType+":"+localID+":"+gameIdentifier);
			datastore.delete(key);
	}
	
	public static GameAccessEntity getAccessById(String accessId) {
		Key key = KeyFactory.createKey(GameAccessEntity.KIND, accessId);
		try {
			return new GameAccessEntity(datastore.get(key));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

    public static void deleteGame(Long gameId) {
		Query q = new Query(GameAccessEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
		q.setFilter(new Query.FilterPredicate(GameAccessEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
    }
}
