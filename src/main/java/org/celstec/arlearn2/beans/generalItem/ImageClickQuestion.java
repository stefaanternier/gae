package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.List;
import java.util.Vector;

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
public class ImageClickQuestion extends GeneralItem {
    public static String zonesType = "org.celstec.arlearn2.beans.generalItem.ImageClickQuestionZone";

    private List<ImageClickQuestionZone> zones = new Vector();

    public List<ImageClickQuestionZone> getZones() {
        return zones;
    }
    public void setZones(List<ImageClickQuestionZone> zones) {
        this.zones = zones;
    }

    public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            ImageClickQuestion mct = (ImageClickQuestion) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {

                if (mct.getZones() != null) returnObject.put("zones", ListSerializer.toJSON(mct.getZones()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };


    public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

        @Override
        public ImageClickQuestion toBean(JSONObject object) {
            ImageClickQuestion mct = new ImageClickQuestion();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            ImageClickQuestion mctItem = (ImageClickQuestion) genericBean;
            if (object.has("zones")) mctItem.setZones(ListDeserializer.toBean(object.getJSONArray("zones"), ImageClickQuestionZone.class));
        };
    };
}
