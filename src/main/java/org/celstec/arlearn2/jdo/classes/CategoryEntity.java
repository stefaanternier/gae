package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.store.Category;

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
public class CategoryEntity {

    public static String KIND = "CategoryJDO";

    public static String COL_CATEGORYID = "categoryId";
    public static String COL_TITLE= "title";
    public static String COL_LANG = "lang";
    public static String COL_DELETED = "deleted";




    protected Key id;
    private Long categoryId;
    private String title;
    private String lang;
    private Boolean deleted;

    public Long getId() {
        return id.getId();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public CategoryEntity() {

    }

    public CategoryEntity(Entity entity) {
        this.id = entity.getKey();
        this.title = (String) entity.getProperty(COL_TITLE);
        this.categoryId = (Long) entity.getProperty(COL_CATEGORYID);
        this.lang = (String) entity.getProperty(COL_LANG);
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);

    }

    public Entity toEntity() {

        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_TITLE,this.title);
        result.setProperty(COL_CATEGORYID,this.categoryId);
        result.setProperty(COL_LANG,this.lang);
        result.setProperty(COL_DELETED,this.deleted);
        return result;
    }

    public Category toCategory(){
        Category category = new Category();
        category.setLang(getLang());
        category.setTitle(getTitle());
        category.setId(getId());
        category.setDeleted(getDeleted());
        category.setCategoryId(getCategoryId());
        return category;
    }
}
