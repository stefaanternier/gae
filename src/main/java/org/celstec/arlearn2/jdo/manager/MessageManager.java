package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.MessageList;
//import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.MessageEntity;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;
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
public class MessageManager {

    private static final String params[] = new String[] { "threadId", "runId" };
    private static final String paramsNames[] = new String[] { "threadIdParam", "runIdParam" };
    private static final String types[] = new String[] { "Long", "Long" };

    private static final int MESSAGES_IN_LIST = 20;

    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }


    public static Message createMessage(Message message) {
//        PersistenceManager pm = PMF.get().getPersistenceManager();
        MessageEntity messageJDO = new MessageEntity();
        messageJDO.setDeleted(message.getDeleted());
        messageJDO.setThreadId(message.getThreadId());
        messageJDO.setMessageId(message.getMessageId());
        messageJDO.setSubject(message.getSubject());
        messageJDO.setBody(message.getBody());
        messageJDO.setDate(message.getDate());
        messageJDO.setRunId(message.getRunId());
        messageJDO.setSenderId(message.getSenderId());
        messageJDO.setSenderProviderId(message.getSenderProviderId());
        messageJDO.setLastModificationDate(System.currentTimeMillis());
        Entity entity = messageJDO.toEntity();
        datastore.put(entity);
        return new MessageEntity(entity).toBean();

//        try {
//            pm.makePersistent(messageJDO);
//            return toBean(messageJDO);
//        } finally {
//            pm.close();
//        }
    }

    public static MessageList getMessagesByThreadId(long threadId) {
        MessageList ml = new MessageList();
        Query q = new Query(MessageEntity.KIND)
                .setFilter(new Query.FilterPredicate(MessageEntity.COL_THREADID, Query.FilterOperator.EQUAL, threadId));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            ml.addMessage(new MessageEntity(result).toBean());
        }
        return ml;
    }

//    private static List<MessageJDO> getMessages(PersistenceManager pm, Long threadId, Long runId) {
//        Query query = pm.newQuery(MessageJDO.class);
//        query.setOrdering("lastModificationDate asc");
//        Object args[] = {threadId, runId};
//        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//        return (List<MessageJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
//
//    }

    public static MessageList getMessagesByThreadId(long threadId, Long from, Long until, String cursorString) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(MESSAGES_IN_LIST);
        if (cursorString != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
        }
        Query.CompositeFilter filter;

        if (from == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(MessageEntity.COL_THREADID, Query.FilterOperator.EQUAL, threadId),
                    new Query.FilterPredicate(MessageEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
            );
        } else if (until == null) {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(MessageEntity.COL_RUNID, Query.FilterOperator.EQUAL, threadId),
                    new Query.FilterPredicate(MessageEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        } else {
            filter = Query.CompositeFilterOperator.and(
                    new Query.FilterPredicate(MessageEntity.COL_RUNID, Query.FilterOperator.EQUAL, threadId),
                    new Query.FilterPredicate(MessageEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
                    new Query.FilterPredicate(MessageEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
            );
        }
        Query q = new Query(MessageEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
        q.setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);

        MessageList returnList = new MessageList();
        QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
        for (Entity result : results) {
            MessageEntity object = new MessageEntity(result);
            returnList.addMessage(object.toBean());
        }
        if (results.size() == MESSAGES_IN_LIST) {
            returnList.setResumptionToken(results.getCursor().toWebSafeString());
        }
        returnList.setServerTime(System.currentTimeMillis());
        return returnList;

    }

    public static void updateDate(Long identifier) {
        try {
            Entity e = datastore.get(KeyFactory.createKey(MessageEntity.KIND, identifier));
            e.setProperty(MessageEntity.COL_LASTMODIFICATIONDATE, System.currentTimeMillis());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        MessageJDO messageJDO = pm.getObjectById(MessageJDO.class, KeyFactory.createKey(MessageJDO.class.getSimpleName(), identifier));
//        System.out.println("to update message "+messageJDO);

    }

    public static MessageList getRecentMessagesByThreadId(Long threadId, int amount) {
        MessageList ml = new MessageList();
        Query q = new Query(MessageEntity.KIND)
                .setFilter(new Query.FilterPredicate(MessageEntity.COL_THREADID, Query.FilterOperator.EQUAL, threadId));
        q.addSort(MessageEntity.COL_LASTMODIFICATIONDATE, Query.SortDirection.ASCENDING);

        PreparedQuery pq = datastore.prepare(q);
        int i=0;
        for (Entity result : pq.asIterable()) {
            ml.addMessage(new MessageEntity(result).toBean());
            i++;
            if (i ==amount)break;
        }
        return ml;

//        PersistenceManager pm = PMF.get().getPersistenceManager();
//
//        try {
//            Query query = pm.newQuery(MessageJDO.class);
//            query.setOrdering("lastModificationDate desc");
//
//            query.setRange(0, amount);
//            String filter = null;
//            String params = null;
//            Object args[] = null;
//
//                filter = "threadId == threadIdParam";
//                params = "Long threadIdParam";
//                args = new Object[] { threadId};
//
//            query.setFilter(filter);
//            query.declareParameters(params);
//            List<MessageJDO> results = (List<MessageJDO>) query.executeWithArray(args);
//            Iterator<MessageJDO> it = (results).iterator();
//            int i = 0;
//            while (it.hasNext()) {
//                i++;
//                MessageJDO object = it.next();
//                returnList.addMessage(toBean(object));
//
//            }
//            returnList.setServerTime(System.currentTimeMillis());
//
//        }finally {
//            pm.close();
//        }
//        return returnList;
    }

    public static Message getMessageById(Long messageId) {
        try {
            Entity e = datastore.get(KeyFactory.createKey(MessageEntity.KIND, messageId));
            return new MessageEntity(e).toBean();
            } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
