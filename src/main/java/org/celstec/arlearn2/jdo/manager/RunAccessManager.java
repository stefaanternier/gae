package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.jdo.classes.RunAccessEntity;

import java.util.*;


public class RunAccessManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	public static RunAccessEntity addRunAccess(String localID, int accountType, long runId, long gameId, int accesRights) {
		RunAccessEntity runAccess = new RunAccessEntity();
		runAccess.setAccessRights(accesRights);
		runAccess.setLocalId(localID);
		runAccess.setAccountType(accountType);
		runAccess.setRunId(runId);
		runAccess.setUniqueId();
		runAccess.setLastModificationDateRun(System.currentTimeMillis());
		runAccess.setGameId(gameId);
		datastore.put(runAccess.toEntity());
		return runAccess;
	}

	//todo testnewimplementation
	public static void resetGameAccessLastModificationDate(long runId) {
		long lastModifiation = System.currentTimeMillis();

		ArrayList<Game> raList = new ArrayList<Game>();
		Query q = new Query(RunAccessEntity.KIND)
				.setFilter(new Query.FilterPredicate(RunAccessEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			result.setProperty(RunAccessEntity.COL_LASTMODIFICATIONDATERUN, lastModifiation);
			datastore.put(result);
		}
	}

	public static List<RunAccess> getRunList(int accountType, String localId, Long from, Long until) {
		ArrayList<RunAccess> accessDefinitions = new ArrayList<RunAccess>();
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(RunAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(RunAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(RunAccessEntity.COL_LASTMODIFICATIONDATERUN, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(RunAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(RunAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(RunAccessEntity.COL_LASTMODIFICATIONDATERUN, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(RunAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(RunAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(RunAccessEntity.COL_LASTMODIFICATIONDATERUN, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from),
					new Query.FilterPredicate(RunAccessEntity.COL_LASTMODIFICATIONDATERUN, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		}

		Query q = new Query(RunAccessEntity.KIND)
				.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			accessDefinitions.add(new RunAccessEntity(result).toBean());
		}
		return accessDefinitions;
	}

	public static List<RunAccess> getRunList(int accountType, String localId, Long gameId) {
		ArrayList<RunAccess> accessDefinitions = new ArrayList<RunAccess>();
		Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(RunAccessEntity.COL_LOCALID, Query.FilterOperator.EQUAL, localId),
					new Query.FilterPredicate(RunAccessEntity.COL_ACCOUNTTYPE, Query.FilterOperator.EQUAL, accountType),
					new Query.FilterPredicate(RunAccessEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId)

			);
		Query q = new Query(RunAccessEntity.KIND)
				.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			accessDefinitions.add(new RunAccessEntity(result).toBean());
		}
		return accessDefinitions;
	}


	public static List<RunAccess> getRunAccessList(long runId) {
		ArrayList<RunAccess> accessDefinitions = new ArrayList<RunAccess>();
		Query q = new Query(RunAccessEntity.KIND)
				.setFilter(new Query.FilterPredicate(RunAccessEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			accessDefinitions.add(new RunAccessEntity(result).toBean());
		}
		return accessDefinitions;
	}


	//todo test new implementation
	public static RunAccessEntity getAccessById(String accessId) {
		Key key = KeyFactory.createKey(RunAccessEntity.KIND, accessId);
		try {
			return new RunAccessEntity(datastore.get(key));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}
}
