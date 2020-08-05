package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
public class TopGamesEntity {
    public static String KIND = "TopGames";

    public static String COL_AMOUNTOFUSERS = "amountOfUsers";
    public static String COL_GENERATETIMESTAMP = "generateTimestamp";
    public static String COL_SHARING = "sharing";
    public static String COL_LANGUAGE= "language";
    public static String COL_TITLE = "title";
    public static String COL_DELETED = "deleted";



    public TopGamesEntity(){

    }

    public TopGamesEntity(Entity entity){
        this.id = entity.getKey();
        this.amountOfUsers = (Long) entity.getProperty(COL_AMOUNTOFUSERS);
        this.generateTimestamp = (Long) entity.getProperty(COL_GENERATETIMESTAMP);
        if ( entity.getProperty(COL_SHARING) !=null )
            this.sharing = ((Long) entity.getProperty(COL_SHARING)).intValue();
        this.language = (String) entity.getProperty(COL_LANGUAGE);
        this.title = (String) entity.getProperty(COL_TITLE);
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);


    }

    public Entity toEntity() {
        Entity result = new Entity(KIND, this.id.getName());
        result.setProperty(COL_AMOUNTOFUSERS, this.amountOfUsers);
        result.setProperty(COL_GENERATETIMESTAMP, this.generateTimestamp);
        result.setProperty(COL_SHARING, this.sharing);
        result.setProperty(COL_LANGUAGE, this.language);
        result.setProperty(COL_TITLE, this.title);
        result.setProperty(COL_DELETED, this.deleted);
        return result;
    }

    protected Key id;
    private Long amountOfUsers;
    private Long generateTimestamp;
    private Integer sharing;
    private String language;
    private String title;
    private boolean deleted;

    public Long getGameId() {
        return id.getId();
    }

    public void setGameId(Long gameId) {
        if (gameId != null)
            setGameId(KeyFactory.createKey(TopGamesEntity.KIND, gameId));
    }

    public void setGameId(Key gameId) {
        this.id = gameId;
    }

    public Long getAmountOfUsers() {
        return amountOfUsers;
    }

    public void setAmountOfUsers(Long amountOfUsers) {
        this.amountOfUsers = amountOfUsers;
    }

    public Long getGenerateTimestamp() {
        return generateTimestamp;
    }

    public void setGenerateTimestamp(Long generateTimestamp) {
        this.generateTimestamp = generateTimestamp;
    }

    public Integer getSharing() {
        return sharing;
    }

    public void setSharing(Integer sharing) {
        this.sharing = sharing;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
