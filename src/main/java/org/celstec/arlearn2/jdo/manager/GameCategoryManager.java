package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.store.GameCategory;
import org.celstec.arlearn2.beans.store.GameCategoryList;

import org.celstec.arlearn2.jdo.classes.CategoryEntity;
import org.celstec.arlearn2.jdo.classes.GameCategoryEntity;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.List;

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
public class GameCategoryManager {
    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }
    public static GameCategory linkGameCategory(Long gameId, Long categoryId) {

        GameCategoryEntity gameCategoryJDO = new GameCategoryEntity();
        gameCategoryJDO.setCategoryId(categoryId);
        gameCategoryJDO.setGameId(gameId);
        gameCategoryJDO.setDeleted(false);
        Entity entity = gameCategoryJDO.toEntity();
        datastore.put(entity);
        return gameCategoryJDO.toGameCategory();
    }
    public static GameCategoryList getGames(Long categoryId) {

        GameCategoryList resultList = new GameCategoryList();
        Query q = new Query(GameCategoryEntity.KIND)
                .setFilter(new Query.FilterPredicate(GameCategoryEntity.COL_CATEGORYID, Query.FilterOperator.EQUAL, categoryId));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.addGameCategory(new GameCategoryEntity(result).toGameCategory());
        }
        return resultList;

    }

    public static GameCategoryList getCategories(Long gameId) {
        GameCategoryList resultList = new GameCategoryList();
        Query q = new Query(GameCategoryEntity.KIND)
                .setFilter(new Query.FilterPredicate(GameCategoryEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.addGameCategory(new GameCategoryEntity(result).toGameCategory());
        }
        return resultList;
    }

}
