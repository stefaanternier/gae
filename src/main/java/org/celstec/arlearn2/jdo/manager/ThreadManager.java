package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
//import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ThreadEntity;
//import org.celstec.arlearn2.jdo.classes.UserJDO;

import java.util.ArrayList;
import java.util.Iterator;
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
public class ThreadManager {

    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public static org.celstec.arlearn2.beans.run.Thread createThread(Thread thread, boolean isDefault) {
        ThreadEntity threadJDO = new ThreadEntity();
        threadJDO.setName(thread.getName());
        threadJDO.setRunId(thread.getRunId());
        threadJDO.setIsDefault(isDefault);
        threadJDO.setLastModificationDate(System.currentTimeMillis());
        Entity entity = threadJDO.toEntity();
        datastore.put(entity);
        return new ThreadEntity(entity).toBean();
    }

    public static List<Thread> getThreads(Long runId) {
        ArrayList<Thread> threadArrayList = new ArrayList<Thread>();
        Query q = new Query(ThreadEntity.KIND)
                .setFilter(new Query.FilterPredicate(ThreadEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            threadArrayList.add(new ThreadEntity(result).toBean());
        }
        return threadArrayList;
    }

    public static Thread getDefaultThread(Long runId) {
        Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(ThreadEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),

                new Query.FilterPredicate(ThreadEntity.COL_ISDEFAULT, Query.FilterOperator.EQUAL, true)

        );
        Query q = new Query(ThreadEntity.KIND)
                .setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            return new ThreadEntity(result).toBean();
        }

        return null;
    }

    public static Thread getThread(Long threadId) {
        Key key = KeyFactory.createKey(ThreadEntity.KIND,threadId);
        Entity result = null;
        try {
            result = datastore.get(key);
        } catch (EntityNotFoundException e) {
            return null;
        }
        return new ThreadEntity(result).toBean();
    }
}
