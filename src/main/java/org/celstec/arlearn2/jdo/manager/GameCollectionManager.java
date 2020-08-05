package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.GameCollection;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.GameCollectionJDO;

//import javax.jdo.PersistenceManager;

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
public class GameCollectionManager {
//    private static DatastoreService datastore;
//    static {
//        datastore = DatastoreServiceFactory.getDatastoreService();
//    }
//
//
//    public static GameCollection addGameCollection(String owner, GameCollection bean){
//        PersistenceManager pm = PMF.get().getPersistenceManager();
//
//        GameCollectionJDO gameCollectionJDO = new GameCollectionJDO();
//        gameCollectionJDO.setDeleted(false);
//        gameCollectionJDO.setOwner(owner);
//        JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
//        gameCollectionJDO.setPayload(new Text(jbs.serialiseToJson().toString()));
//        try {
//            pm.makePersistent(gameCollectionJDO);
//            bean.setId(gameCollectionJDO.getId());
//            return bean;
//        } finally {
//            pm.close();
//        }
//    }
//
//    public static GameCollection getGameCollection(Long gameCollectionId){
//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        try{
//            return toBean(pm.getObjectById(GameCollectionJDO.class,
//                    KeyFactory.createKey(GameCollectionJDO.class.getSimpleName(), gameCollectionId)));
//        } finally {
//            pm.close();
//        }
//    }
//
//    private static GameCollection toBean(GameCollectionJDO jdo) {
//        if (jdo == null)
//            return null;
//        GameCollection gc = new GameCollection();
//        try {
//            JsonBeanDeserializer jbd = new JsonBeanDeserializer(jdo.getPayload().getValue());;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        gc.setDeleted(jdo.getDeleted());
//        gc.setOwner(jdo.getOwner());
//        gc.setId(jdo.getId());
//
//        return gc;
//    }

}
