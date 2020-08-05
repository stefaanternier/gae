package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.run.RunAccess;

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
public class RunAccessEntity {

    public final static int OWNER = 1;
    public final static int CAN_EDIT = 2;
    public final static int CAN_VIEW = 3;

    public static String KIND = "RunAccessJDO";

    public static String COL_ACCESSRIGHTS = "accessRights";
    public static String COL_ACCOUNTTYPE = "accountType";
    public static String COL_GAMEID = "gameId";
    public static String COL_RUNID = "runId";
    public static String COL_LASTMODIFICATIONDATERUN = "lastModificationDateRun";
    public static String COL_LOCALID = "localId";

    public RunAccessEntity(){

    }

    public RunAccessEntity(Entity entity){
        this.uniqueId = entity.getKey();
        if ( entity.getProperty(COL_ACCESSRIGHTS) !=null ) this.accessRights = ((Long) entity.getProperty(COL_ACCESSRIGHTS)).intValue();
        if ( entity.getProperty(COL_ACCOUNTTYPE) !=null ) this.accountType = ((Long) entity.getProperty(COL_ACCOUNTTYPE)).intValue();
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.lastModificationDateRun = (Long) entity.getProperty(COL_LASTMODIFICATIONDATERUN);
        this.localId = (String) entity.getProperty(COL_LOCALID);

    }

    public Entity toEntity() {
        Entity result = new Entity(KIND, this.uniqueId.getName());
        result.setProperty(COL_ACCESSRIGHTS, this.accessRights);
        result.setProperty(COL_ACCOUNTTYPE, this.accountType);
        result.setProperty(COL_GAMEID, this.gameId);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_LASTMODIFICATIONDATERUN, this.lastModificationDateRun);
        result.setProperty(COL_LOCALID, this.localId);
        return result;
    }

    public RunAccess toBean() {

        RunAccess runAccess = new RunAccess();
        runAccess.setAccount(getAccountType()+":"+getLocalId());
        runAccess.setAccessRights(getAccessRights());
        runAccess.setRunId(getRunId());
        runAccess.setTimestamp(getLastModificationDateRun());
        return runAccess;
    }

    private Key uniqueId;
    private String localId;
    private Integer accountType;
    private Long runId;
    private Long gameId;
    private Integer accessRights;
    private Long lastModificationDateRun;

    public String getUniqueId() {
        return uniqueId.getName();
    }

    public void setUniqueId() {
        this.uniqueId = KeyFactory.createKey(KIND, getAccountType()+":"+getLocalId()+":"+getRunId());
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Integer accessRights) {
        this.accessRights = accessRights;
    }

    public Long getLastModificationDateRun() {
        return lastModificationDateRun;
    }

    public void setLastModificationDateRun(Long lastModificationDateRun) {
        this.lastModificationDateRun = lastModificationDateRun;
    }
}
