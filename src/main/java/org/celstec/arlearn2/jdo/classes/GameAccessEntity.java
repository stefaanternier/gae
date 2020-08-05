package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.game.GameAccess;

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
public class GameAccessEntity {
    public final static int OWNER = 1;
    public final static int CAN_EDIT = 2;
    public final static int CAN_VIEW = 3;

    public static String KIND = "GameAccessJDO";

    public static String COL_ACCESSRIGHTS = "accessRights";
    public static String COL_ACCOUNTTYPE = "accountType";
    public static String COL_GAMEID = "gameId";
    public static String COL_LASTMODIFICATIONDATEGAME = "lastModificationDateGame";
    public static String COL_LOCALID = "localId";

    public GameAccessEntity(){

    }

    public GameAccessEntity(Entity entity){
        this.uniqueId = entity.getKey();
        if ( entity.getProperty(COL_ACCESSRIGHTS) !=null )
            this.accessRights = ((Long) entity.getProperty(COL_ACCESSRIGHTS)).intValue();
        this.accountType = ((Long) entity.getProperty(COL_ACCOUNTTYPE)).intValue();
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.lastModificationDateGame = (Long) entity.getProperty(COL_LASTMODIFICATIONDATEGAME);
        this.localId = (String) entity.getProperty(COL_LOCALID);
    }

    public Entity toEntity() {
        Entity result = new Entity(KIND, this.uniqueId.getName());
        result.setProperty(COL_ACCESSRIGHTS, this.accessRights);
        result.setProperty(COL_ACCOUNTTYPE, this.accountType);
        result.setProperty(COL_GAMEID, this.gameId);
        result.setProperty(COL_LASTMODIFICATIONDATEGAME, this.lastModificationDateGame);
        result.setProperty(COL_LOCALID, this.localId);
        return result;
    }

    public GameAccess toBean() {

        GameAccess gameAccess = new GameAccess();
        gameAccess.setAccount(getAccountType()+":"+getLocalId());
        gameAccess.setAccessRights(getAccessRights());
        gameAccess.setGameId(getGameId());
        gameAccess.setTimestamp(getLastModificationDateGame());
        return gameAccess;
    }

    private Key uniqueId;
    private String localId;
    private Integer accountType;
    private Long gameId;
    private Integer accessRights;
    private Long lastModificationDateGame;


    public String getUniqueId() {
        return uniqueId.getName();
    }

    public void setUniqueId() {
        this.uniqueId = KeyFactory.createKey(KIND, getAccountType()+":"+getLocalId()+":"+getGameId());
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

    public Long getLastModificationDateGame() {
        return lastModificationDateGame;
    }

    public void setLastModificationDateGame(Long lastModificationDateGame) {
        this.lastModificationDateGame = lastModificationDateGame;
    }
}
