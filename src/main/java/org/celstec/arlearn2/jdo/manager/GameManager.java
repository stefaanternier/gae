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

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountEntity;
import org.celstec.arlearn2.jdo.classes.GameEntity;

public class GameManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

//	private static final String params[] = new String[] { "id", "creatorEmail", "owner", "feedUrl", "title" };
//	private static final String paramsNames[] = new String[] { "gameParam", "creatorEmailParam", "ownerEmailParam", "feedUrlParam", "titleParam" };
//	private static final String types[] = new String[] { "Long", "String", "String", "String", "String" };

//	@Deprecated
//	public static Long addGame(String title, String owner, String creatorEmail, String feedUrl, Long gameId, Config config, Double lat, Double lng) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		GameJDO gameJdo = new GameJDO();
//		gameJdo.setGameId(gameId);
//		gameJdo.setCreatorEmail(creatorEmail);
//		gameJdo.setOwner(owner);
//		gameJdo.setFeedUrl(feedUrl);
//		gameJdo.setTitle(title);
//        gameJdo.setLat(lat);
//        gameJdo.setLng(lng);
//		gameJdo.setLastModificationDate(System.currentTimeMillis());
//		if (config != null)  {
//			gameJdo.setConfig(config.toString());
//		}
//		try {
//			GameJDO persistentGame = pm.makePersistent(gameJdo);
//			return persistentGame.getGameId();
//
//		} finally {
//			pm.close();
//		}
//	}
	
	public static Long addGame(Game game, String myAccount) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameEntity gameJdo = new GameEntity();
		gameJdo.setGameId(game.getGameId());
		gameJdo.setCreatorEmail(game.getCreator());
		gameJdo.setOwner(myAccount);
		gameJdo.setFeedUrl(game.getFeedUrl());
		gameJdo.setTitle(game.getTitle());
		gameJdo.setSplashScreen(game.getSplashScreen());
		gameJdo.setSharing(game.getSharing());
		gameJdo.setDescription(game.getDescription());
        gameJdo.setLat(game.getLat());
        gameJdo.setLng(game.getLng());
        gameJdo.setLanguage(game.getLanguage());
        gameJdo.setTheme(game.getTheme());
		if (game.getLicenseCode() !=null) gameJdo.setLicenseCode(game.getLicenseCode());
		gameJdo.setLastModificationDate(System.currentTimeMillis());
		if (game.getConfig() != null)  {
			gameJdo.setConfig(game.getConfig().toString());
		}
		return datastore.put(gameJdo.toEntity()).getId();
//		try {
//			GameJDO persistentGame = pm.makePersistent(gameJdo);
//			return persistentGame.getGameId();
//
//		} finally {
//			pm.close();
//		}
	}
	
	public static Long addGame(Game game) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();

		GameEntity gameJdo = new GameEntity();
		gameJdo.setGameId(game.getGameId());
		gameJdo.setTitle(game.getTitle());
		gameJdo.setSplashScreen(game.getSplashScreen());
		gameJdo.setSharing(game.getSharing());
		gameJdo.setDescription(game.getDescription());
        gameJdo.setLat(game.getLat());
        gameJdo.setLng(game.getLng());
        gameJdo.setLanguage(game.getLanguage());
        gameJdo.setTheme(game.getTheme());
		gameJdo.setAppStoreUrl(game.getAppStoreUrl());
		gameJdo.setGooglePlayUrl(game.getGooglePlayUrl());
		if (game.getDeleted() != null) gameJdo.setDeleted(game.getDeleted());
		if (game.getLicenseCode() !=null) gameJdo.setLicenseCode(game.getLicenseCode());

		gameJdo.setLastModificationDate(System.currentTimeMillis());
		if (game.getConfig() != null)  {
			gameJdo.setConfig(game.getConfig().toString());
		}
		gameJdo.setPrivateMode(game.getPrivateMode());
		return datastore.put(gameJdo.toEntity()).getId();

//		try {
//			GameJDO persistentGame = pm.makePersistent(gameJdo);
//			return persistentGame.getGameId();
//
//		} finally {
//			pm.close();
//		}
	}

	public static List<Game> getGames(Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
		ArrayList<Game> returnGames = new ArrayList<Game>();
		Query q = new Query(GameEntity.KIND);
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			Iterator<GameJDO> it = getGames(pm, gameId, creatorEmail, owner, feedUrl, title).iterator();
//			while (it.hasNext()) {
//				returnGames.add(toBean((GameJDO) it.next()));
//			}
			return returnGames;
//		} finally {
//			pm.close();
//		}

	}

//	@SuppressWarnings("unchecked")
//	public static List<GameEntity> getGames(PersistenceManager pm, Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
//		Query query = pm.newQuery(GameJDO.class);
//		Object args[] = { gameId, creatorEmail, owner, feedUrl, title };
//		if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
//			query.setFilter("deleted == null");
//			return (List<GameJDO>) query.execute();
//		}
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//		return ((List<GameJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
//	}

	public static void deleteGame(Long gameId) {
		Key key = KeyFactory.createKey(GameEntity.KIND,gameId);
		datastore.delete(key);
	}

    public static Game getGame(Long gameId){
		Key key = KeyFactory.createKey(GameEntity.KIND,gameId);
		Entity result = null;
		try {
			result = datastore.get(key);
		} catch (EntityNotFoundException e) {
			System.out.println("error ");
			return null;
		}
		return new GameEntity(result).toGame();
    }


    public static void makeGameFeatured(Long gameId, boolean featured) {
		Key key = KeyFactory.createKey(GameEntity.KIND,gameId);

		GameEntity result = null;
		try {
			result = new GameEntity(datastore.get(key));
			result.setFeatured(featured);
			datastore.put(result.toEntity());
		} catch (EntityNotFoundException e) {


		}
    }

    public static List<Game> getFeaturedGames() {
        ArrayList<Game> featuredGamesList = new ArrayList<Game>();
		Query.CompositeFilter featuredFilter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(GameEntity.COL_FEATURED, Query.FilterOperator.EQUAL, true),
				new Query.FilterPredicate(GameEntity.COL_SHARING, Query.FilterOperator.EQUAL, 3)
		);
		Query q = new Query(GameEntity.KIND).setFilter(featuredFilter);

		PreparedQuery pq = datastore.prepare(q);
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(5));
		Iterator<Entity> it = results.iterator();
		while (it.hasNext()) {
			featuredGamesList.add(new GameEntity(it.next()).toGame());
		}
        return featuredGamesList;
    }


}
