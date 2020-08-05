package org.celstec.arlearn2.beans.generalItem;

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
public class SortQuestionItem extends Bean {

    private int correctPosition;
    private String text;
    private String id;

    public SortQuestionItem() {
    }

    public int getCorrectPosition() {
        return correctPosition;
    }

    public void setCorrectPosition(int correctPosition) {
        this.correctPosition = correctPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        SortQuestionItem other = (SortQuestionItem ) obj;
        return super.equals(obj) &&
                nullSafeEquals(getId(), other.getId()) &&
                nullSafeEquals(getText(), other.getText()) &&

                nullSafeEquals(getCorrectPosition(), other.getCorrectPosition()) ;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public SortQuestionItem toBean(JSONObject object) {
            SortQuestionItem bean = new SortQuestionItem();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            SortQuestionItem bean = (SortQuestionItem) genericBean;
            if (object.has("id")) bean.setId(object.getString("id"));
            if (object.has("text")) bean.setText(object.getString("text"));
            if (object.has("correctPosition")) bean.setCorrectPosition(object.getInt("correctPosition"));

        }

    };

    public static BeanSerializer serializer = new  BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            SortQuestionItem item = (SortQuestionItem) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (item.getId() != null) returnObject.put("id", item.getId());
                returnObject.put("correctPosition", item.getCorrectPosition());

                if (item.getText() != null) returnObject.put("text", item.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

}
