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

//import java.util.List;
//
//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.game.GameFile;
import org.celstec.arlearn2.beans.game.GameFileList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FilePathEntity;


import com.google.appengine.api.blobstore.BlobKey;


import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.List;

public class FilePathManager {

    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }


//	private static final String params[] = new String[]{"email", "runId", "gameId", "fileName"};
//	private static final String paramsNames[] = new String[]{ "emailParam", "runIdParam", "gameIdParam", "fileNameParam"};
//	private static final String types[] = new String[]{"String",  "Long", "Long", "String"};

    private static final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

	public static void addFile(Long runId, String email, String fileName, BlobKey blobkey) {
        addFile(runId, null, email, fileName,blobkey);
	}

    public static void addFile(Long runId, Long gameId, String email, String fileName, BlobKey blobkey) {
//        PersistenceManager pm = PMF.get().getPersistenceManager();
        FilePathEntity filePathJDO = new FilePathEntity();
        filePathJDO.setRunId(runId);
        filePathJDO.setGameId(gameId);
        filePathJDO.setEmail(email);
        filePathJDO.setFileName(fileName);
        filePathJDO.setBlobKey(blobkey);
        datastore.put(filePathJDO.toEntity());
//
    }

    public static boolean clone(Long oldGameId, Long newGameId, String oldFileName, String newFileName){
        List<FilePathEntity> files = getFilePathEntityByGameId(oldGameId, oldFileName);
        if (files.isEmpty()) return false;
        List<FilePathEntity> newFiles = getFilePathEntityByGameId(newGameId, newFileName);
        for (FilePathEntity newFile:newFiles) {
            datastore.delete(newFile.getKey());
        }
        FilePathEntity oldFilePathJDO = files.get(0);
        FilePathEntity filePathJDO = new FilePathEntity();
        filePathJDO.setGameId(newGameId);
        filePathJDO.setFileName(newFileName);
        filePathJDO.setBlobKey(oldFilePathJDO.getBlobKey());
        datastore.put(filePathJDO.toEntity());
        return true;

//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        try {
//            List<FilePathJDO> files = getFilePathJDOs(pm, null, null, oldGameId, oldFileName);
//            if (files.isEmpty()) return false;
//            List<FilePathJDO> newFiles = getFilePathJDOs(pm, null, null, newGameId, newFileName);
//            for (FilePathJDO newFile:newFiles) {
//                pm.deletePersistent(newFile);
//            }
//            FilePathJDO oldFilePathJDO = files.get(0);
//            FilePathJDO filePathJDO = new FilePathJDO();
//            filePathJDO.setGameId(newGameId);
//            filePathJDO.setFileName(newFileName);
//            filePathJDO.setBlobKey(oldFilePathJDO.getBlobKey());
//            pm.makePersistent(filePathJDO);
//            return true;
//        } finally {
//            pm.close();
//        }
    }

    public static GameFile deleteFilePath(long gameId, String filePath) {
        GameFile gameFile= null;

        Query.CompositeFilter filter  = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(FilePathEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
                new Query.FilterPredicate(FilePathEntity.COL_FILENAME, Query.FilterOperator.EQUAL, filePath)
        );
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            FilePathEntity fpe = new FilePathEntity(result);
            gameFile = toBean(fpe);
            datastore.delete(fpe.getKey());
        }
        return gameFile;


//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        GameFile gameFile= null;
//        try {
//            List<FilePathJDO> newFiles = getFilePathJDOs(pm, null, null, gameId, filePath);
//            for (FilePathJDO newFile:newFiles) {
//                gameFile = toBean(newFile);
//                pm.deletePersistent(newFile);
//                gameFile.setDeleted(true);
//            }
//        } finally {
//            pm.close();
//        }
//        return gameFile;
    }

//	public static BlobKey getBlobKey(String email, Long runId,  String fileName){
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<FilePathJDO> files = getFilePathJDOs(pm, email, runId, null, fileName);
//			if (!files.isEmpty()) return files.get(0).getBlobKey();
//		} finally {
//			pm.close();
//		}
//		return null;
//	}


	public static List<FilePathEntity> getFilePathJDOs(String email, Long runId, Long gameId, String fileName) {

        Query.CompositeFilter filter  = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(FilePathEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
                new Query.FilterPredicate(FilePathEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
                new Query.FilterPredicate(FilePathEntity.COL_FILENAME, Query.FilterOperator.EQUAL, fileName),
                new Query.FilterPredicate(FilePathEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email)
        );
        Query q = new Query(FilePathEntity.KIND);//.addSort("name", SortDirection.ASCENDING);
        q.setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        List<FilePathEntity> resultList = new ArrayList<FilePathEntity>();
        for (Entity result : pq.asIterable()) {
            resultList.add(new FilePathEntity(result));
        }
        return resultList;
	}


    private static GameFile toBean(FilePathEntity filePathJDO) {
        GameFile gf = new GameFile();
        gf.setId(filePathJDO.getId());
        gf.setPath(filePathJDO.getFileName());
        gf.setMd5Hash(blobInfoFactory.loadBlobInfo(filePathJDO.getBlobKey()).getMd5Hash());
        gf.setSize(blobInfoFactory.loadBlobInfo(filePathJDO.getBlobKey()).getSize());
        return gf;

    }

	public static void delete(BlobKey bk) {
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(new Query.FilterPredicate(FilePathEntity.COL_BLOBKEY, Query.FilterOperator.EQUAL, bk));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            datastore.delete(result.getKey());
        }
	}


    public static BlobKey getBlobKey(Long fileId) {
        Key k = KeyFactory.createKey(FilePathEntity.KIND, fileId);
        try {
            return new FilePathEntity(datastore.get(k)).getBlobKey();
        } catch (EntityNotFoundException e) {
            return null;
        }

    }

    public static GameFileList getFilePathJDOs(BlobKey bk) {
        GameFileList returnList = new GameFileList();
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(new Query.FilterPredicate(FilePathEntity.COL_BLOBKEY, Query.FilterOperator.EQUAL, bk));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            returnList.addGameFile(new FilePathEntity(result).toBean());
        }
        return returnList;
    }

    public static GameFileList getFilePathByGameId(long gameId) {
        GameFileList returnList = new GameFileList();
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(new Query.FilterPredicate(FilePathEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            returnList.addGameFile(new FilePathEntity(result).toBean());
        }
        return returnList;
    }

    public static  List<FilePathEntity>  getFilePathEntityByGameId(long gameId) {
        List<FilePathEntity> resultList = new ArrayList<FilePathEntity>();
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(new Query.FilterPredicate(FilePathEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.add(new FilePathEntity(result));
        }
        return resultList;
    }

    public static  List<FilePathEntity>  getFilePathEntityByGameId(long gameId, String fileName) {
        List<FilePathEntity> resultList = new ArrayList<FilePathEntity>();
        Query.CompositeFilter filter  = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(FilePathEntity.COL_GAMEID, Query.FilterOperator.EQUAL, gameId),
                new Query.FilterPredicate(FilePathEntity.COL_FILENAME, Query.FilterOperator.EQUAL, fileName)
        );
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.add(new FilePathEntity(result));
        }
        return resultList;
    }

    public static  List<FilePathEntity>  getFilePathEntityByAccount(String account, String fileName) {
        List<FilePathEntity> resultList = new ArrayList<FilePathEntity>();
        Query.CompositeFilter filter  = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(FilePathEntity.COL_EMAIL, Query.FilterOperator.EQUAL, account),
                new Query.FilterPredicate(FilePathEntity.COL_FILENAME, Query.FilterOperator.EQUAL, fileName)
        );
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.add(new FilePathEntity(result));
        }
        return resultList;
    }

    public static  List<FilePathEntity>  getFilePathEntityByAccount(String account, Long runId, String fileName) {
        List<FilePathEntity> resultList = new ArrayList<FilePathEntity>();
        Query.CompositeFilter filter  = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(FilePathEntity.COL_EMAIL, Query.FilterOperator.EQUAL, account),
                new Query.FilterPredicate(FilePathEntity.COL_RUNID, Query.FilterOperator.EQUAL, runId),
                new Query.FilterPredicate(FilePathEntity.COL_FILENAME, Query.FilterOperator.EQUAL, fileName)
        );
        Query q = new Query(FilePathEntity.KIND)
                .setFilter(filter);

        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.add(new FilePathEntity(result));
        }
        return resultList;
    }
}
