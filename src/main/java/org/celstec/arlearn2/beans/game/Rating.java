package org.celstec.arlearn2.beans.game;

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
public class Rating extends Bean {

    private Integer userProviderId;

    private String userId;

    private Integer rating;

    private Long gameId;

    private Long amount;

    public Integer getUserProviderId() {
        return userProviderId;
    }

    public void setUserProviderId(Integer userProviderId) {
        this.userProviderId = userProviderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public Rating toBean(JSONObject object) {
            Rating bean = new Rating();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            Rating bean = (Rating) genericBean;
            if (object.has("userProviderId")) bean.setUserProviderId(object.getInt("userProviderId"));
            if (object.has("userId")) bean.setUserId(object.getString("userId"));
            if (object.has("rating")) bean.setRating(object.getInt("rating"));
            if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
            if (object.has("amount")) bean.setAmount(object.getLong("amount"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            Rating ratingBean = (Rating) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ratingBean.getUserProviderId() != null) returnObject.put("userProviderId", ratingBean.getUserProviderId());
                if (ratingBean.getUserId() != null) returnObject.put("userId", ratingBean.getUserId());
                if (ratingBean.getRating() != null) returnObject.put("rating", ratingBean.getRating());
                if (ratingBean.getGameId() != null) returnObject.put("gameId", ratingBean.getGameId());
                if (ratingBean.getAmount() != null) returnObject.put("amount", ratingBean.getAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
