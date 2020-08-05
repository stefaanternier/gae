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
public class Notification extends Bean {

    public static final int STROKEN_TYPE = 0;
    public static final int POPUP_TYPE = 1;

    private Integer notificationType;
    private String readMessage;
    private String message;

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public String getReadMessage() {
        return readMessage;
    }

    public void setReadMessage(String readMessage) {
        this.readMessage = readMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Deserializer extends BeanDeserializer {

        @Override
        public Notification toBean(JSONObject object) {
            Notification bean = new Notification();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            Notification bean = (Notification) genericBean;
            if (object.has("notificationType")) bean.setNotificationType(object.getInt("notificationType"));
            if (object.has("message")) bean.setMessage(object.getString("message"));
            if (object.has("readMessage")) bean.setReadMessage(object.getString("readMessage"));
        }
    }

    public static class Serializer extends BeanSerializer {
        @Override
        public JSONObject toJSON(Object bean) {
            Notification dep = (Notification) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (dep.getNotificationType() != null) returnObject.put("notificationType", dep.getNotificationType());
                if (dep.getMessage() != null) returnObject.put("message", dep.getMessage());
                if (dep.getReadMessage() != null) returnObject.put("readMessage", dep.getReadMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    }
}
