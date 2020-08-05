/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class NotificationBean extends Bean {
	
	public final static int GAME_CREATED = 1;
	public final static int GAME_DELETED = 2;
	public final static int GAME_ALTERED = 3;
	public final static int RUN_CREATED = 4;
	public final static int RUN_DELETED = 5;
	public final static int RUN_ALTERED = 6;
	
	public final static int GI_CREATED = 7;
	public final static int GI_DELETED = 8;
	public final static int GI_ALTERED = 9;
	public final static int GI_VISIBLE = 10;
	public final static int GI_DISAPPEARED = 11;

	public final static int TEAM_ALTERED = 20;

    private String alert;

	public void retainOnlyIdentifier() {
		
	}

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public static class NotificationBeanDeserializer extends BeanDeserializer{

        @Override
        public NotificationBean toBean(JSONObject object) {
            NotificationBean bean = new NotificationBean();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            NotificationBean bean = (NotificationBean) genericBean;
            if (object.has("alert")) bean.setAlert(object.getString("alert"));
        }
    };

    public static class NotificationBeanSerializer extends BeanSerializer {

        @Override
        public JSONObject toJSON(Object bean) {
            NotificationBean gameBean = (NotificationBean) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameBean.getAlert() != null) returnObject.put("alert", gameBean.getAlert());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
