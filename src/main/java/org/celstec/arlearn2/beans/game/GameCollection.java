package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.GameBeanSerializer;
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
public class GameCollection extends Bean {

    private Long id;

    private Boolean deleted;
    private String owner;

    public static String gamesType = "org.celstec.arlearn2.beans.game.Game";
    private List<Game> games = new Vector();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }


    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
    public void addGame(Game game) {
        this.games.add(game);
    }


    public boolean isDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer() {
        @Override
        public GameCollection toBean(JSONObject object) {
            GameCollection bean = new GameCollection();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, GameCollection bean) throws JSONException {
            super.initBean(object, bean);
            if (object.has("id")) bean.setId(object.getLong("id"));
            if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));
            if (object.has("owner")) bean.setOwner(object.getString("owner"));
            if (object.has("games"))
                bean.setGames(ListDeserializer.toBean(object.getJSONArray("games"), Game.class));

        }
    };

    public static BeanSerializer serializer = new BeanSerializer() {
        @Override
        public JSONObject toJSON(Object bean) {
            GameCollection gameCollection = (GameCollection) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameCollection.getId() != null) returnObject.put("id", gameCollection.getId());
                if (gameCollection.getOwner() != null) returnObject.put("owner", gameCollection.getOwner());
                if (gameCollection.getDeleted() != null) returnObject.put("deleted", gameCollection.getDeleted());

                if (gameCollection.getGames() != null)
                    returnObject.put("games", ListSerializer.toJSON(gameCollection.getGames()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
