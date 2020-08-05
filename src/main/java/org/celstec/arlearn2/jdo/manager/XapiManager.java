package org.celstec.arlearn2.jdo.manager;


import com.google.appengine.api.datastore.*;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.XapiJDO;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;
import java.text.SimpleDateFormat;
import java.util.*;

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
public class XapiManager {
    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }
    public static String KIND = "XapiJDO";

    private final static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public static void addNewGeneralItemStatement(String actorId, GeneralItem item) {

        Entity e = new Entity(KIND);
        e.setProperty("actorId", actorId);
        e.setProperty("payLoad", new Text(item.toString()));
        e.setProperty("objectid", "https://streetlearn.appspot.com/#/games/"+item.getGameId()+"/messages/"+item.getId());
        e.setProperty("verbDisplay", "user modified this general item "+item.getId());
        e.setProperty("verbId", "http://activitystrea.ms/schema/1.0/author");
        e.setProperty("timestamp", System.currentTimeMillis());
        e.setProperty("gameId", item.getGameId());

        datastore.put(e);

//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        XapiJDO xapi = new XapiJDO();
//        xapi.setActorId(actorId);
//        xapi.setPayload(item.toString());
//        xapi.setObjectid("https://streetlearn.appspot.com/#/games/"+item.getGameId()+"/messages/"+item.getId());
//        xapi.setVerbDisplay("user modified this general item "+item.getId());
//        xapi.setVerbId("http://activitystrea.ms/schema/1.0/author");
//        xapi.setTimestamp(System.currentTimeMillis());
//        xapi.setGameId(item.getGameId());
//        try {
//            pm.makePersistent(xapi);
//        } finally {
//            pm.close();
//        }
    }

    public static String getActivityFeed(String actorId, String cursorString) {
//        PersistenceManager pm = PMF.get().getPersistenceManager();

        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(20);
        if (cursorString != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
        }
        Query q = new Query("")
                .setFilter(new Query.FilterPredicate("actorId", Query.FilterOperator.EQUAL, actorId));
        q.addSort("timestamp", Query.SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);
        QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
        String returnList = " {\n" +
                "    \"items\" : [";
        for (Entity result : results) {
//            ActionEntity object = new ActionEntity(result);
//            returnList.addAction(object.toBean());
            ISO8601DATEFORMAT.format(new Date((Long) result.getProperty("timestamp")));
            returnList += "{\n" +
                    "\t\"published\": \""+ISO8601DATEFORMAT.format(new Date((Long) result.getProperty("timestamp")))+"\",\n" +
                    "\t\"actor\": {\n" +
                    "\t\t\"objectType\": \"Agent\",\n" +
                    "\t\t\"account\": {\n" +
                    "\t\t\t\"homePage\": \"https://streetlearn.appspot.com/rest/account/"+result.getProperty("actorId")+"\",\n" +
                    "\t\t\t\"name\": \""+result.getProperty("actorId")+"\"\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\t\"verb\": {\n" +
                    "\t\t\"id\": \""+result.getProperty("verbId")+"\",\n" +
                    "\t\t\"display\": {\n" +
                    "\t\t\t\"en-US\": \""+result.getProperty("verbDisplay")+"\"\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\t\"object\": {\n" +
                    "\t\t\"objectType\": \"Activity\",\n" +
                    "\t\t\"id\": \""+result.getProperty("objectId")+"\"\n" +
                    "\n" +
                    "\t},\n" +
                    "\t\"arlearnBean\": "+((Text)result.getProperty("payload")).getValue()+"\n" +
                    "}";
        }
        return returnList+"] }";


//        String returnList = " {\n" +
//                "    \"items\" : [";
//        try {
//            Query query = pm.newQuery(XapiJDO.class);
//            if (cursorString != null) {
//
//                Cursor c = Cursor.fromWebSafeString(cursorString);
//                Map<String, Object> extendsionMap = new HashMap<String, Object>();
//                extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
//                query.setExtensions(extendsionMap);
//            }
//            query.setRange(0, 10);
//            String filter = null;
//            String params = null;
//            Object args[] = null;
//
//                filter = "actorId == actorIdParam ";
//                params = "String actorIdParam";
//                args = new Object[] { actorId };
//            query.setFilter(filter);
//            query.declareParameters(params);
//            query.setOrdering("timestamp desc");
//
//            List<XapiJDO> results = (List<XapiJDO>) query.executeWithArray(args);
//            Iterator<XapiJDO> it = (results).iterator();
//            int i = 0;
//            int size = 0;
//            while (it.hasNext()) {
//                if (i>0) returnList += ",";
//                i++;
//                size ++;
//                XapiJDO object = it.next();
////                returnList += object.getActorId() +" "+ object.getTimestamp()+ " "+object.getPayload() ;
//
//                ISO8601DATEFORMAT.format(new Date(object.getTimestamp()));
//                returnList += "{\n" +
//                        "\t\"published\": \""+ISO8601DATEFORMAT.format(new Date(object.getTimestamp()))+"\",\n" +
//                        "\t\"actor\": {\n" +
//                        "\t\t\"objectType\": \"Agent\",\n" +
//                        "\t\t\"account\": {\n" +
//                        "\t\t\t\"homePage\": \"https://streetlearn.appspot.com/rest/account/"+object.getActorId()+"\",\n" +
//                        "\t\t\t\"name\": \""+object.getActorId()+"\"\n" +
//                        "\t\t}\n" +
//                        "\t},\n" +
//                        "\t\"verb\": {\n" +
//                        "\t\t\"id\": \""+object.getVerbId()+"\",\n" +
//                        "\t\t\"display\": {\n" +
//                        "\t\t\t\"en-US\": \""+object.getVerbDisplay()+"\"\n" +
//                        "\t\t}\n" +
//                        "\t},\n" +
//                        "\t\"object\": {\n" +
//                        "\t\t\"objectType\": \"Activity\",\n" +
//                        "\t\t\"id\": \""+object.getObjectid()+"\"\n" +
//                        "\n" +
//                        "\t},\n" +
//                        "\t\"arlearnBean\": "+object.getPayload()+"\n" +
//                        "}";
//
//            }
//            Cursor c = JDOCursorHelper.getCursor(results);
//            cursorString = c.toWebSafeString();
////            if (size == 10) {
////                returnList+= "cursorstring: " + (cursorString);
////            }
//
//        }finally {
//            pm.close();
//        }
//        return returnList+"] }";
    }

    public static String getActivityFeedGame(Long gameId, String cursorString) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(20);
        if (cursorString != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
        }
        Query q = new Query("")
                .setFilter(new Query.FilterPredicate("gameId", Query.FilterOperator.EQUAL, gameId));
        q.addSort("timestamp", Query.SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);
        QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
        String returnList = " {\n" +
                "    \"items\" : [";
        for (Entity result : results) {
//            ActionEntity object = new ActionEntity(result);
//            returnList.addAction(object.toBean());
            ISO8601DATEFORMAT.format(new Date((Long) result.getProperty("timestamp")));
            returnList += "{\n" +
                    "\t\"published\": \""+ISO8601DATEFORMAT.format(new Date((Long) result.getProperty("timestamp")))+"\",\n" +
                    "\t\"actor\": {\n" +
                    "\t\t\"objectType\": \"Agent\",\n" +
                    "\t\t\"account\": {\n" +
                    "\t\t\t\"homePage\": \"https://streetlearn.appspot.com/rest/account/"+result.getProperty("actorId")+"\",\n" +
                    "\t\t\t\"name\": \""+result.getProperty("actorId")+"\"\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\t\"verb\": {\n" +
                    "\t\t\"id\": \""+result.getProperty("verbId")+"\",\n" +
                    "\t\t\"display\": {\n" +
                    "\t\t\t\"en-US\": \""+result.getProperty("verbDisplay")+"\"\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\t\"object\": {\n" +
                    "\t\t\"objectType\": \"Activity\",\n" +
                    "\t\t\"id\": \""+result.getProperty("objectId")+"\"\n" +
                    "\n" +
                    "\t},\n" +
                    "\t\"arlearnBean\": "+((Text)result.getProperty("payload")).getValue()+"\n" +
                    "}";
        }
        return returnList+"] }";
//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        String returnList = " {\n" +
//                "    \"items\" : [";
//        try {
//            Query query = pm.newQuery(XapiJDO.class);
//            if (cursorString != null) {
//
//                Cursor c = Cursor.fromWebSafeString(cursorString);
//                Map<String, Object> extendsionMap = new HashMap<String, Object>();
//                extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
//                query.setExtensions(extendsionMap);
//            }
//            query.setRange(0, 10);
//            String filter = null;
//            String params = null;
//            Object args[] = null;
//
//            filter = "gameId == gameIdParam ";
//            params = "String gameIdParam";
//            args = new Object[] { gameId };
//            query.setFilter(filter);
//            query.declareParameters(params);
//            query.setOrdering("timestamp desc");
//
//            List<XapiJDO> results = (List<XapiJDO>) query.executeWithArray(args);
//            Iterator<XapiJDO> it = (results).iterator();
//            int i = 0;
//            int size = 0;
//            while (it.hasNext()) {
//                if (i>0) returnList += ",";
//                i++;
//                size ++;
//                XapiJDO object = it.next();
//
//                ISO8601DATEFORMAT.format(new Date(object.getTimestamp()));
//                returnList += "{\n" +
//                        "\t\"published\": \""+ISO8601DATEFORMAT.format(new Date(object.getTimestamp()))+"\",\n" +
//                        "\t\"actor\": {\n" +
//                        "\t\t\"objectType\": \"Agent\",\n" +
//                        "\t\t\"account\": {\n" +
//                        "\t\t\t\"homePage\": \"https://streetlearn.appspot.com/rest/account/"+object.getActorId()+"\",\n" +
//                        "\t\t\t\"name\": \""+object.getActorId()+"\"\n" +
//                        "\t\t}\n" +
//                        "\t},\n" +
//                        "\t\"verb\": {\n" +
//                        "\t\t\"id\": \""+object.getVerbId()+"\",\n" +
//                        "\t\t\"display\": {\n" +
//                        "\t\t\t\"en-US\": \""+object.getVerbDisplay()+"\"\n" +
//                        "\t\t}\n" +
//                        "\t},\n" +
//                        "\t\"object\": {\n" +
//                        "\t\t\"objectType\": \"Activity\",\n" +
//                        "\t\t\"id\": \""+object.getObjectid()+"\"\n" +
//                        "\n" +
//                        "\t},\n" +
//                        "\t\"arlearnBean\": "+object.getPayload()+"\n" +
//                        "}";
//
//            }
//            Cursor c = JDOCursorHelper.getCursor(results);
//            cursorString = c.toWebSafeString();
////            if (size == 10) {
////                returnList+= "cursorstring: " + (cursorString);
////            }
//
//        }finally {
//            pm.close();
//        }
//        return returnList+"] }";
    }
}
