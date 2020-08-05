package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.MultipleChoiceAnswerItemDeserializer;
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
public class ImageClickQuestionZone extends Bean {

    private Double relativeX;
    private Double relativeY;
    private Integer radius;
    private String id;

    public ImageClickQuestionZone(){}

    public Double getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(Double relativeX) {
        this.relativeX = relativeX;
    }

    public Double getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(Double relativeY) {
        this.relativeY = relativeY;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        ImageClickQuestionZone other = (ImageClickQuestionZone ) obj;
        return super.equals(obj) &&
                nullSafeEquals(getId(), other.getId()) &&
                nullSafeEquals(getRelativeX(), other.getRelativeX()) &&
                nullSafeEquals(getRelativeY(), other.getRelativeY()) &&
                nullSafeEquals(getRadius(), other.getRadius()) ;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public ImageClickQuestionZone toBean(JSONObject object) {
            ImageClickQuestionZone bean = new ImageClickQuestionZone();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            ImageClickQuestionZone bean = (ImageClickQuestionZone) genericBean;
            if (object.has("id")) bean.setId(object.getString("id"));
            if (object.has("relativeX")) bean.setRelativeX(object.getDouble("relativeX"));
            if (object.has("relativeY")) bean.setRelativeY(object.getDouble("relativeY"));
            if (object.has("radius")) bean.setRadius(object.getInt("radius"));
        }

    };

    public static BeanSerializer serializer = new  BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            ImageClickQuestionZone item = (ImageClickQuestionZone) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (item.getId() != null) returnObject.put("id", item.getId());
                if (item.getRelativeX() != null) returnObject.put("relativeX", item.getRelativeX());
                if (item.getRelativeY() != null) returnObject.put("relativeY", item.getRelativeY());
                if (item.getRadius() != null) returnObject.put("radius", item.getRadius());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
