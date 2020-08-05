package org.celstec.arlearn2.mappers;

import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.tools.mapreduce.MapJob;
//import com.google.appengine.tools.mapreduce.MapOnlyMapper;
//import com.google.appengine.tools.mapreduce.MapSettings;
//import com.google.appengine.tools.mapreduce.MapSpecification;
//import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
//import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;

import java.io.Serializable;

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
public class UpdateUsersTable implements Serializable {

    public final static String MODULE_NAME= "default";
    public final static String WORKER_NAME= "default";

//    protected MapSettings getSettings() {
//        MapSettings settings = new MapSettings.Builder()
//                .setWorkerQueueName(WORKER_NAME)
//                .setModule(MODULE_NAME)
//                .build();
//        return settings;
//    }

    public void start() {
//        MapJob.start(getMapCreationJobSpec(100, 5, 5), new MapSettings.Builder(getSettings()).build());
    }

//    private MapSpecification<Entity, Entity, Void> getMapCreationJobSpec(int bytesPerEntity, int entities,
//                                                                         int shardCount) {
//        MapSpecification<Entity, Entity, Void> spec = new MapSpecification.Builder<Entity, Entity, Void>(
//                new DatastoreInput("UserJDO", 4 ),
//                new UpdateDeletedMapper(),
//                new DatastoreOutput())
//                .setJobName("Update deleted data of entities")
//                .build();
//        return spec;
//    }
//
//    class UpdateDeletedMapper extends MapOnlyMapper<Entity, Entity> {
//
//        public UpdateDeletedMapper(){}
//
//
//        @Override
//        public void map(Entity entity) {
//            if (!entity.hasProperty("deleted") || entity.getProperty("deleted")==null) {
//                entity.setProperty("deleted", false);
//                emit(entity);
//            }
//
//        }
//    }

}
