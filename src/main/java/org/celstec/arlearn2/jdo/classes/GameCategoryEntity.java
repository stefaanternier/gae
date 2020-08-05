package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.celstec.arlearn2.beans.store.GameCategory;

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
public class GameCategoryEntity {

    public static String KIND = "GameCategoryJDO";

    public static String COL_CATEGORYID = "categoryId";
    public static String COL_GAMEID= "gameId";
    public static String COL_DELETED = "deleted";

    protected Key id;
    private Long categoryId;
    private Long gameId;
    private Boolean deleted;


    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public GameCategoryEntity() {

    }

    public GameCategoryEntity(Entity entity) {
        this.id = entity.getKey();
        this.gameId = (Long) entity.getProperty(COL_GAMEID);
        this.categoryId = (Long) entity.getProperty(COL_CATEGORYID);
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);

    }

    public Entity toEntity() {

        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_GAMEID,this.gameId);
        result.setProperty(COL_CATEGORYID,this.categoryId);
        result.setProperty(COL_DELETED,this.deleted);
        return result;
    }

    public GameCategory toGameCategory(){
        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(getId().getId());
        gameCategory.setCategoryId(getCategoryId());
        gameCategory.setGameId(getGameId());
        gameCategory.setDeleted(getDeleted());
        return gameCategory;
    }
}
