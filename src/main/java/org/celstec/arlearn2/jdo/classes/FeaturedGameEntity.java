package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.game.Game;

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
public class FeaturedGameEntity {

    public static String KIND = "FeaturedGameJDO";

    public static String COL_DELETED = "deleted";
    public static String COL_LANG = "lang";
    public static String COL_LASTMODIFICATIONDATE = "lastModificationDate";
    public static String COL_RANK = "rank";
    //public static String COL_ = "";


    //from GameClass
    private Key gameId;
    protected Boolean deleted;
    private Long lastModificationDate;
    public Long getGameId() {
        return gameId.getId();
    }
    public void setGameId(Long gameId) {
        this.gameId = KeyFactory.createKey(KIND, gameId);
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public Long getLastModificationDate() {
        return lastModificationDate;
    }
    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    //from FeaturedGameJDO
    private String lang;
    private int rank;
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public FeaturedGameEntity(Entity entity){
        this.gameId = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.lang = (String) entity.getProperty(COL_LANG);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.rank = ((Long) entity.getProperty(COL_RANK)).intValue();
    }

    public FeaturedGameEntity(){

    }


    public Entity toEntity() {
        Entity result = new Entity(KIND, this.gameId.getId());
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_LANG, this.lang);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_RANK, this.rank);
        return result;
    }

    public Game toGame(){
        Game game = new Game();
        game.setGameId(getGameId());
        game.setRank(getRank());
        game.setLanguage(getLang());
        return game;
    }
}
