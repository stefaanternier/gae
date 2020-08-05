package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.GameBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.GameBeanSerializer;
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
public class GameFile extends GameBean {
    private String path;
    private String md5Hash;
    private Long id;
    private Long size;
    private String localRawRef;

    public GameFile() {
    }

    public String getLocalRawRef() {
        return localRawRef;
    }

    public void setLocalRawRef(String localRawRef) {
        this.localRawRef = localRawRef;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public static GameBeanDeserializer deserializer = new GameBeanDeserializer() {
        @Override
        public GameFile toBean(JSONObject object) {
            GameFile bean = new GameFile();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, GameFile bean) throws JSONException {
            super.initBean(object, bean);
            if (object.has("path")) bean.setPath(object.getString("path"));
            if (object.has("md5Hash")) bean.setMd5Hash(object.getString("md5Hash"));
            if (object.has("id")) bean.setId(object.getLong("id"));
            if (object.has("size")) bean.setSize(object.getLong("size"));
            if (object.has("localRawRef")) bean.setLocalRawRef(object.getString("localRawRef"));
        }
    };

    public static GameBeanSerializer serializer = new GameBeanSerializer() {
        @Override
        public JSONObject toJSON(Object bean) {
            GameFile gameFile = (GameFile) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameFile.getPath() != null) returnObject.put("path", gameFile.getPath());
                if (gameFile.getId() != null) returnObject.put("id", gameFile.getId());
                if (gameFile.getSize() != null) returnObject.put("size", gameFile.getSize());
                if (gameFile.getMd5Hash() != null) returnObject.put("md5Hash", gameFile.getMd5Hash());
                if (gameFile.getLocalRawRef() != null) returnObject.put("localRawRef", gameFile.getLocalRawRef());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
