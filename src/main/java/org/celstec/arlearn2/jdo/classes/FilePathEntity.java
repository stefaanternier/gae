package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.cloudstorage.*;
import org.celstec.arlearn2.beans.game.GameFile;

import java.io.IOException;

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
public class FilePathEntity {
    public static String KIND = "FilePathJDO";

    public static String COL_BLOBKEY= "blobKey";
    public static String COL_DELETED= "deleted";
    public static String COL_EMAIL= "email";
    public static String COL_GAMEID= "gameId";
    public static String COL_RUNID= "runId";
    public static String COL_FILENAME= "fileName";
    private static final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

    public FilePathEntity(){

    }
    public FilePathEntity(Entity entity){

        this.id = entity.getKey();
        this.blobKey = (BlobKey) entity.getProperty(COL_BLOBKEY);
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.email = (String) entity.getProperty(COL_EMAIL);
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.fileName = (String) entity.getProperty(COL_FILENAME);
    }
    public Entity toEntity() {
        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_BLOBKEY, this.blobKey);
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_EMAIL, this.email);
        result.setProperty(COL_GAMEID, this.gameId);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_FILENAME, this.fileName);


        return result;
    }
    private final String bucketName = "arlearn-eu-rundata";
    private BlobInfoFactory infoFactory = new BlobInfoFactory();
    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(
                    new RetryParams.Builder()
                            .initialRetryDelayMillis(10)
                            .retryMaxAttempts(10)
                            .totalRetryPeriodMillis(15000)
                            .build());
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    public  GameFile toBean() {
        GameFile gf = new GameFile();
        gf.setId(getId());
        gf.setPath(getFileName());

        GcsFilename fn = new GcsFilename(bucketName, "game/"+gameId+getFileName());
        try {
            GcsFileMetadata fileMetadata = gcsService.getMetadata(fn);
            if (fileMetadata!=null) gf.setSize(fileMetadata.getLength());
            BlobInfo info =blobInfoFactory.loadBlobInfo(getBlobKey());
            if (info != null) {
                gf.setMd5Hash(info.getMd5Hash());
                gf.setSize(info.getSize());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return gf;

    }

    public Key getKey(){
        return id;
    }
    protected Key id;
    private Long runId;
    private Boolean deleted;
    private String email;
    private Long gameId;
    private String fileName;
    private BlobKey blobKey;


    public Long getId() {
        return id.getId();
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Boolean getDeleted() {
        if (deleted == null) return false;
        return deleted;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BlobKey getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
