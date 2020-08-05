package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class GameFileList extends Bean {

    public static String GameFileType = "org.celstec.arlearn2.beans.game.GameFile";
    private List<GameFile> gameFiles = new ArrayList<GameFile>();
    private Long serverTime;

    public GameFileList(){}

    public List<GameFile> getGameFiles() {
        return gameFiles;
    }

    public void setGameFiles(List<GameFile> gameFiles) {
        this.gameFiles = gameFiles;
    }

    public void addGameFile(GameFile gameFile) {
        this.gameFiles.add(gameFile);
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public static BeanSerializer serializer = new BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            GameFileList gameFileList = (GameFileList) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameFileList.getServerTime() != null)
                    returnObject.put("serverTime", gameFileList.getServerTime());
                if (gameFileList.getGameFiles() != null)
                    returnObject.put("gameFiles", ListSerializer.toJSON(gameFileList.getGameFiles()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public GameFileList toBean(JSONObject object) {
            GameFileList tl = new GameFileList();
            try {
                initBean(object, tl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tl;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            GameFileList giList = (GameFileList) genericBean;
            if (object.has("serverTime"))
                giList.setServerTime(object.getLong("serverTime"));
            if (object.has("gameFiles"))
                giList.setGameFiles(ListDeserializer.toBean(object.getJSONArray("gameFiles"), GameFile.class));
        }
    };
}
