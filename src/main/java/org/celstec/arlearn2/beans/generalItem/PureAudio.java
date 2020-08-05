package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.AudioObjectDeserializer;
import org.celstec.arlearn2.beans.serializer.json.AudioObjectSerializer;
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
public class PureAudio extends AudioObject {

    private String imageUrl;

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        PureAudio other = (PureAudio ) obj;
        return
                nullSafeEquals(getImageUrl(), other.getImageUrl());

    }

    public static AudioObjectDeserializer deserializer = new AudioObjectDeserializer(){

        @Override
        public PureAudio toBean(JSONObject object) {
            PureAudio pureAudio = new PureAudio();
            try {
                initBean(object, pureAudio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pureAudio;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            PureAudio pureAudio = (PureAudio) genericBean;
            if (object.has("imageUrl")) pureAudio.setImageUrl(object.getString("imageUrl"));
        }
    };

    public static AudioObjectSerializer serializer = new AudioObjectSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            PureAudio mct = (PureAudio) bean;
            JSONObject returnObject = super.toJSON(bean);

            try {
                if (mct.getImageUrl() != null) returnObject.put("imageUrl", mct.getImageUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };

}
