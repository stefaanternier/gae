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
import org.celstec.arlearn2.beans.GameIdentifierList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.classes.UserEntity;

import java.util.ArrayList;
import java.util.List;

;

public class UserManager {
    private static DatastoreService datastore;

    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }


    public static void addUser(User bean) {
        UserEntity user = new UserEntity();
        user.setTeamId(bean.getTeamId());
        user.setRunId(bean.getRunId());
        user.setEmail(bean.getFullId());
        user.setGameId(bean.getGameId());
        Long currentTime = System.currentTimeMillis();
        user.setLastModificationDate(currentTime);
        user.setLastModificationDateGame(currentTime);
        user.setDeleted(false);
        if (bean.getDeleted() != null && bean.getDeleted()) user.setDeleted(bean.getDeleted());

        user.setId();
        JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
        user.setPayload(new Text(jbs.serialiseToJson().toString()));
        datastore.put(user.toEntity());
    }

    public static void gameChanged(User u) {
        Key key = KeyFactory.createKey(UserEntity.KIND, UserEntity.generateId(u.getFullId(), u.getRunId()));

        try {
            UserEntity userEntity = new UserEntity(datastore.get(key));
            Long currentTime = System.currentTimeMillis();
            userEntity.setLastModificationDateGame(currentTime);
            datastore.put(userEntity.toEntity());

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        //TODO replace notification?
//		if (u.getGameId() != null) {
//			Game g =new Game();
//			g.setGameId(u.getGameId());
//			new NotificationDelegator().broadcast(g, u.getFullId());
//
//		}

    }

    public static List<User> getUserList(Long runId) {
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(new Query.FilterPredicate(UserEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result).toBean());
        }
        return userArrayList;
    }

    public static List<User> getUserListByTeamId(Long runId, String teamId) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(UserEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
                new Query.FilterPredicate(UserEntity.COL_TEAMID, Query.FilterOperator.EQUAL, teamId)
        );
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result).toBean());
        }
        return userArrayList;

    }

    public static GameIdentifierList getUserList(String email, String cursorString) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10);
        if (cursorString != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
        }
        Query q = new Query(UserEntity.KIND)
                .addSort(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.SortDirection.DESCENDING)
                .setFilter(new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email));
        PreparedQuery pq = datastore.prepare(q);
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
//        long[] ids = new long[results.size()];
        ArrayList<Long> ids = new ArrayList<Long>();
//        System.out.println("result size is " + results.size());
        int i = 0;
        for (Entity result : results) {
            if (!((Boolean) result.getProperty(UserEntity.COL_DELETED))) {
                ids.add((Long) result.getProperty(UserEntity.COL_GAMEID));
            }
        }
        String resumptionToken = null;
        if (results.size() == 10) {
            resumptionToken = results.getCursor().toWebSafeString();

        }
        return new GameIdentifierList(resumptionToken, ids.toArray(new Long[0]));

    }

    public static List<User> getUserList(String email) {
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .addSort(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.SortDirection.DESCENDING)
                .setFilter(new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email));
//        "message": "com.google.appengine.api.datastore.DatastoreNeedIndexException:
// no matching index found. recommended index is:\n- kind: UserJDO\n  properties:\n
// - name: email\n  - name: lastModificationDateGame\n
// direction: desc\n\nThe suggested index for this query is:\n    <datastore-index kind=\"UserJDO\" ancestor=\"false\" source=\"manual\">\n        <property name=\"email\" direction=\"asc\"/>\n
//  <property name=\"lastModificationDateGame\" direction=\"desc\"/>\n    </datastore-index>\n\n"
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result).toBean());
        }
        return userArrayList;

    }

    public static List<Long> getGameIdList(String fullId) {
        ArrayList<Long> userArrayList = new ArrayList<Long>();
        Query q = new Query(UserEntity.KIND)
                .addSort(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.SortDirection.DESCENDING)
                .setFilter(new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, fullId));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add((Long) result.getProperty(UserEntity.COL_GAMEID));
        }
        return userArrayList;

    }

    public static List<User> getUserList(Long runId, String email) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(UserEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
                new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email)
        );
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result).toBean());
        }
        return userArrayList;

    }

    private static List<UserEntity> getUserListAsEntity(Long runId, String email) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(UserEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
                new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email)
        );
        ArrayList<UserEntity> userArrayList = new ArrayList<UserEntity>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(filter);
        System.out.println("query is " + q);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result));
        }
        return userArrayList;

    }


    public static List<User> getUserList(String email, Long from, Long until) {
        Query.CompositeFilter filter;
        if (from == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
            );
        } else if (until == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        } else {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        }
        Query q = new Query(UserEntity.KIND);
        q.setFilter(filter);
        q.addSort(UserEntity.COL_LASTMODIFICATIONDATE, Query.SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);

        ArrayList<User> returnList = new ArrayList<User>();
        for (Entity result : pq.asIterable()) {
            UserEntity object = new UserEntity(result);
            returnList.add(object.toBean());
        }
        return returnList;
    }

    public static List<User> getUserListGameParticipate(String email, Long from, Long until) {


        Query.CompositeFilter filter;
        if (from == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
            );
        } else if (until == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        } else {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
                    new Query.FilterPredicate(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        }
        Query q = new Query(UserEntity.KIND);
        q.setFilter(filter);
        q.addSort(UserEntity.COL_LASTMODIFICATIONDATEGAME, Query.SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);

        ArrayList<User> returnList = new ArrayList<User>();
        for (Entity result : pq.asIterable()) {
            UserEntity object = new UserEntity(result);
            returnList.add(object.toBean());
        }
        return returnList;
    }

    public static User getUser(Long runId, String email) {
        return getUserJDO(runId, email).toBean();

    }

    public static List<User> getUserListByGameId(Long gameId, String email) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(UserEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
                new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email)
        );
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
//		System.out.println("user query ");
        for (Entity result : pq.asIterable()) {
            userArrayList.add(new UserEntity(result).toBean());
        }
        return userArrayList;
    }

    public static boolean userExists(Long gameId, String email) {
        return datastore
                .prepare(
                        new Query(UserEntity.KIND)
                                .setFilter(
                                        Query.CompositeFilterOperator.and(
                                                new Query.FilterPredicate(
                                                        UserEntity.COL_GAMEID,
                                                        Query.FilterOperator.EQUAL,
                                                        gameId),
                                                new Query.FilterPredicate(
                                                        UserEntity.COL_EMAIL,
                                                        Query.FilterOperator.EQUAL,
                                                        email)
                                        )
                                )
                )
                .countEntities(FetchOptions.Builder.withLimit(1)) > 0;
    }


    public static boolean hasUserListByGameId(Long gameId, String email) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(UserEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
                new Query.FilterPredicate(UserEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email)
        );
        ArrayList<User> userArrayList = new ArrayList<User>();
        Query q = new Query(UserEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        return pq.countEntities(FetchOptions.Builder.withLimit(1)) > 0;

    }

    private static UserEntity getUserJDO(Long runId, String email) {
        try {
            Key k = KeyFactory.createKey(UserEntity.KIND, UserEntity.generateId(email, runId));
            return new UserEntity(datastore.get(k));
        } catch (Exception e) {
            return null;
        }
    }

    public static void hardDeleteUser(Long runId, String email) {
        Key k = KeyFactory.createKey(UserEntity.KIND, UserEntity.generateId(email, runId));
        datastore.delete(k);
    }


    public static void setStatusDeleted(long runId, String email) {
//		getUserListAsEntity(runId, email);
        System.out.println("about to delete " + email + " for run " + runId);
        List<UserEntity> deleteList = getUserListAsEntity(runId, email);
        for (UserEntity jdo : deleteList) {
            jdo.setDeleted(true);
            jdo.setLastModificationDate(System.currentTimeMillis());
            datastore.put(jdo.toEntity());
        }
    }

}
