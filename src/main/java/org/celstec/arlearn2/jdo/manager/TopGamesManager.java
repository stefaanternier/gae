package org.celstec.arlearn2.jdo.manager;


import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;


import org.celstec.arlearn2.jdo.classes.TopGamesEntity;


/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */

public class TopGamesManager {

    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public static void addGame(long gameId, long amountOfUsers, Game game) {

        TopGamesEntity topGames = new TopGamesEntity();
        topGames.setGameId(gameId);
        topGames.setAmountOfUsers(amountOfUsers);
        topGames.setSharing(game.getSharing());
        topGames.setDeleted(game.getDeleted());
        topGames.setLanguage(game.getLanguage());
        topGames.setTitle(game.getTitle());
        Entity entity = topGames.toEntity();
        datastore.put(entity);
    }

    public static GamesList getTopGames(String lang) {

        GamesList resultList = new GamesList();
        Query.CompositeFilter filter;

        filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(TopGamesEntity.COL_LANGUAGE, Query.FilterOperator.EQUAL, lang),
                new Query.FilterPredicate(TopGamesEntity.COL_DELETED, Query.FilterOperator.EQUAL, false),
                new Query.FilterPredicate(TopGamesEntity.COL_SHARING, Query.FilterOperator.EQUAL, 3)
        );

        Query q = new Query(TopGamesEntity.KIND)
                .setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            TopGamesEntity r = new TopGamesEntity(result);
            Game game = new Game();
            game.setGameId(r.getGameId());
            game.setLanguage(r.getLanguage());
            game.setTitle(r.getTitle());
            resultList.addGame(game);

        }
        return resultList;

    }
}
