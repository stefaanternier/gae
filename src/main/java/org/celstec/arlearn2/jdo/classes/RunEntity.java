package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Run;
import org.codehaus.jettison.json.JSONException;

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
public class RunEntity {

    public static String KIND = "RunJDO";

    public static String COL_DELETED= "deleted";
    public static String COL_GAMEID= "gameId";
    public static String COL_LASTMODIFICATIONDATE= "lastModificationDate";
    public static String COL_OWNER= "owner";
    public static String COL_PAYLOAD= "payload";
    public static String COL_RUNID= "runId";
    public static String COL_SERVERCREATIONTIME= "serverCreationTime";
    public static String COL_STARTTIME= "startTime";
    public static String COL_TAGID= "tagId";
    public static String COL_TITLE= "title";

    public RunEntity(){

    }

    public RunEntity(Entity entity){
        this.id = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.owner = (String) entity.getProperty(COL_OWNER);
        this.payload = (Text) entity.getProperty(COL_PAYLOAD);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.serverCreationTime = (Long) entity.getProperty(COL_SERVERCREATIONTIME);
        this.startTime = (Long) entity.getProperty(COL_STARTTIME);
        this.tagId = (String) entity.getProperty(COL_TAGID);
        this.title = (String) entity.getProperty(COL_TITLE);
    }

    public Entity toEntity() {
        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_GAMEID, this.gameId);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_OWNER, this.owner);
        result.setProperty(COL_PAYLOAD, this.payload);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_SERVERCREATIONTIME, this.serverCreationTime);
        result.setProperty(COL_STARTTIME, this.startTime);
        result.setProperty(COL_TAGID, this.tagId);
        result.setProperty(COL_TITLE, this.title);
        return result;
    }

    public Run toBean() {
        Run run;
        if (getPayload() != null) {
            try {
                run = (Run) JsonBeanDeserializer.deserialize(getPayload().getValue());
            } catch (JSONException e) {
                run = new Run();
            }
        } else {
            run = new Run();
        }
        run.setRunId(getRunId());
        run.setTitle(getTitle());
        run.setGameId(getGameId());
        run.setOwner(getOwner());
        run.setTagId(getTagId());
        run.setStartTime(getStartTime());
        run.setDeleted(getDeleted());
        run.setServerCreationTime(getServerCreationTime());
        run.setLastModificationDate(getLastModificationDate());
        return run;
    }

    //from run
    public Key getKey(){
        return id;
    }
    protected Key id;
    private Long runId;
    private Boolean deleted;

    public Long getId() {
        return id.getId();
    }

    public Boolean getDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public Boolean getDeletedBis() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    // from runJDO
    private Long gameId;
    private String title;

    private String owner;

    private String tagId;
    private Long startTime;
    private Long serverCreationTime;
    private Long lastModificationDate;
    private Text payload;

    public Long getRunId() {
        return id.getId();
    }

    public void setRunId(Long runId) {
        if (runId != null)
            setRunId(KeyFactory.createKey(KIND, runId));
    }

    public void setRunId(Key runId) {
        this.id = runId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getServerCreationTime() {
        return serverCreationTime;
    }

    public void setServerCreationTime(Long serverCreationTime) {
        this.serverCreationTime = serverCreationTime;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Text getPayload() {
        return payload;
    }

    public void setPayload(Text payload) {
        this.payload = payload;
    }



}
