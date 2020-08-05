package org.celstec.arlearn2.beans.store;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
public class Category extends Bean {

    private Long id;
    private Long categoryId;
    private String title;
    private String lang;
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public Category toBean(JSONObject object) {
            Category bean = new Category();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            Category bean = (Category) genericBean;
            if (object.has("id")) bean.setId(object.getLong("id"));
            if (object.has("categoryId")) bean.setCategoryId(object.getLong("categoryId"));
            if (object.has("title")) bean.setTitle(object.getString("title"));
            if (object.has("lang")) bean.setLang(object.getString("lang"));
            if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));

        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            Category categoryBean = (Category) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (categoryBean.getId() != null) returnObject.put("id", categoryBean.getId());
                if (categoryBean.getCategoryId() != null) returnObject.put("categoryId", categoryBean.getCategoryId());
                if (categoryBean.getTitle() != null) returnObject.put("title", categoryBean.getTitle());
                if (categoryBean.getLang() != null) returnObject.put("lang", categoryBean.getLang());
                if (categoryBean.getDeleted() != null) returnObject.put("deleted", categoryBean.getDeleted());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
