package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.User;

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
public class UserEntity {
    public static String KIND = "UserJDO";

    public static String COL_DELETED= "deleted";
    public static String COL_EMAIL= "email";
    public static String COL_GAMEID= "gameId";
    public static String COL_LASTMODIFICATIONDATE= "lastModificationDate";
    public static String COL_LASTMODIFICATIONDATEGAME= "lastModificationDateGame";
    public static String COL_NAME= "name";
    public static String COL_PAYLOAD= "payload";

    public static String COL_RUNID= "runId";
    public static String COL_TEAMID= "teamId";

    public UserEntity(){

    }

    public UserEntity(Entity entity){
        this.id = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.email = (String) entity.getProperty(COL_EMAIL);
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.lastModificationDateGame = (Long) entity.getProperty(COL_LASTMODIFICATIONDATEGAME);
        this.name = (String) entity.getProperty(COL_NAME);
        this.payload = (Text) entity.getProperty(COL_PAYLOAD);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.teamId = (String) entity.getProperty(COL_TEAMID);

    }
    public Entity toEntity() {
        Entity result = new Entity(KIND, this.id.getName());
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_EMAIL, this.email);
        result.setProperty(COL_GAMEID, this.gameId);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_LASTMODIFICATIONDATEGAME, this.lastModificationDateGame);
        result.setProperty(COL_NAME, this.name);
        result.setProperty(COL_PAYLOAD, this.payload);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_TEAMID, this.teamId);
        return result;
    }

    public User toBean() {

        User userBean = null;
        try {
            JsonBeanDeserializer jbd = new JsonBeanDeserializer(getPayload().getValue());
            userBean = (User) jbd.deserialize(User.class);
        } catch (Exception e) {
            e.printStackTrace();
            userBean = new User();
        }
        userBean.setRunId(getRunId());
        userBean.setTeamId(getTeamId());
        userBean.setFullIdentifier(getEmail());
        userBean.setDeleted(getDeleted());
        userBean.setGameId(getGameId());
        userBean.setLastModificationDateGame(getLastModificationDateGame());
        return userBean;
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

    public Boolean getDeletedBis() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    //from userjdo
    private String teamId;

    private String name;

    private String email;

    private Text payload;

    private Long lastModificationDate;


    private Long gameId;


    private Long lastModificationDateGame;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId() {
        if (getEmail() != null && getRunId() !=null) {
            this.id = KeyFactory.createKey(KIND, generateId(getEmail(), getRunId()));
        }
    }

    public static String generateId(String email, Long runId) {
        return runId +":"+email;
    }

    public Text getPayload() {
        return payload;
    }

    public void setPayload(Text payload) {
        this.payload = payload;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getLastModificationDateGame() {
        return lastModificationDateGame;
    }

    public void setLastModificationDateGame(Long lastModificationDateGame) {
        this.lastModificationDateGame = lastModificationDateGame;
    }
}
