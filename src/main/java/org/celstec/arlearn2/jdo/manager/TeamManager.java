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
import org.celstec.arlearn2.jdo.classes.TeamEntity;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;

public class TeamManager {
	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	public static Team addTeam(Long runId, String teamId, String name) {

		TeamEntity team = new TeamEntity();
		team.setTeamId(teamId);
		team.setName(name);
		team.setRunId(runId);
		datastore.put(team.toEntity());

		return team.toBean();
	}

	public static TeamList getTeams(Long runId) {
		TeamList tl = new TeamList();
		Query q = new Query(TeamEntity.KIND)
				.setFilter(new Query.FilterPredicate(TeamEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			tl.addTeam(new TeamEntity(result).toBean());
		}
		return tl;
	}

	public static Team getTeam(String teamId) {

		Key key = KeyFactory.createKey(TeamEntity.KIND,teamId);
		Entity result = null;
		try {
			result = datastore.get(key);
		} catch (EntityNotFoundException e) {
			return null;
		}
		return new TeamEntity(result).toBean();
	}

	public static void deleteTeam(String teamId) {
		Key key = KeyFactory.createKey(TeamEntity.KIND,teamId);
		datastore.delete(key);
	}
}
