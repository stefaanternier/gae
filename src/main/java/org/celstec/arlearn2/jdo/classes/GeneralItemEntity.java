package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import javax.jdo.annotations.Persistent;

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
public class GeneralItemEntity {

    public static String KIND = "GeneralItemJDO";

    public static String COL_DELETED = "deleted";
    public static String COL_LASTMODIFICATIONDATE = "lastModificationDate";


    public static String COL_DEPENDSON = "dependsOn";
    public static String COL_DESCRIPTION = "description";
    public static String COL_GAMEID = "gameId";
    public static String COL_ICONURL = "iconUrl";
    public static String COL_LAT = "lat";
    public static String COL_LNG = "lng";
    public static String COL_NAME = "name";
    public static String COL_PAYLOAD = "payload";
    public static String COL_RADIUS = "radius";
    public static String COL_SCOPE = "scope";
    public static String COL_SHOWATTIMESTAMP = "showAtTimeStamp";
    public static String COL_TIMESTAMP = "timeStamp";
    public static String COL_TYPE = "type";


    public GeneralItemEntity() {

    }
    public GeneralItemEntity(Entity entity) {
        this.itemId = entity.getKey();
        this.gameId = ((Long) entity.getProperty(COL_GAMEID));
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.name = (String) entity.getProperty(COL_NAME);
        this.description = (Text) entity.getProperty(COL_DESCRIPTION);
        this.dependsOn = (Text) entity.getProperty(COL_DEPENDSON);
        this.payload = (Text) entity.getProperty(COL_PAYLOAD);
        this.type = (String) entity.getProperty(COL_TYPE);

        if (entity.getProperty(COL_RADIUS) != null){
            this.radius = ((Long) entity.getProperty(COL_RADIUS)).intValue();
        }

        this.scope = (String) entity.getProperty(COL_SCOPE);
        this.showAtTimeStamp = ((Long) entity.getProperty(COL_SHOWATTIMESTAMP));
        this.lat = (Double) entity.getProperty(COL_LAT);
        this.lng = (Double) entity.getProperty(COL_LNG);
        this.timeStamp = ((Long) entity.getProperty(COL_TIMESTAMP));


        this.iconUrl = (String) entity.getProperty(COL_ICONURL);

    }

    public Entity toEntity() {
        Entity result = null;
        if (this.itemId == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.itemId.getId());
        }
        result.setProperty(COL_DELETED,this.deleted);
        result.setProperty(COL_GAMEID,this.gameId);
        result.setProperty(COL_LASTMODIFICATIONDATE,this.lastModificationDate);
        result.setProperty(COL_NAME,this.name);
        result.setProperty(COL_DESCRIPTION,this.description);
        result.setProperty(COL_DEPENDSON,this.dependsOn);
        result.setProperty(COL_PAYLOAD,this.payload);
        result.setProperty(COL_TYPE,this.type);
        result.setProperty(COL_RADIUS,this.radius);
        result.setProperty(COL_SCOPE,this.scope);
        result.setProperty(COL_SHOWATTIMESTAMP, this.showAtTimeStamp );
        result.setProperty(COL_LAT,this.lat);
        result.setProperty(COL_LNG,this.lng);
        result.setProperty(COL_TIMESTAMP,this.timeStamp);
        result.setProperty(COL_ICONURL,this.iconUrl);
        return result;

    }

    public  GeneralItem toGeneralItem() {

        if (getType() == null) return null;
        JsonBeanDeserializer jbd;
        GeneralItem gi = new GeneralItem();


        Class artifactClass;
        try {
            jbd = new JsonBeanDeserializer(getPayload().getValue());
            gi = (GeneralItem) jbd.deserialize(Class.forName(getType()));
//			artifactClass = Class.forName(jdo.getType());
//			gi = (GeneralItem) artifactClass.getConstructor(String.class).newInstance(jdo.getPayload().getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            if (getDependsOn() != null) {
                jbd = new JsonBeanDeserializer(getDependsOn().getValue());
                gi.setDependsOn((Dependency) jbd.deserialize(Dependency.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gi.setDescription(getDescription().getValue());
        gi.setId(getIdentier());
        gi.setLat(getLat());
        gi.setLng(getLng());
        gi.setGameId(getGameId());
        gi.setName(getName());
        gi.setRadius(getRadius());
        gi.setScope(getScope());
        gi.setType(getType());
        gi.setIconUrl(getIconUrl());
        gi.setDeleted(getDeleted());
        gi.setLastModificationDate(getLastModificationDate());
        return gi;
    }

    //from GameClass
    private Key itemId;
    private Long gameId;
    protected Boolean deleted;
    private Long lastModificationDate;

    private String name;
    private Text description;
    private Text dependsOn;
    private Text payload;
    private String type;
    private Integer radius;
    private String scope;
    private Long showAtTimeStamp;
    private Double lng;
    private Double lat;
    private Long timeStamp;
    private String iconUrl;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public Text getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(Text dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Long getShowAtTimeStamp() {
        return showAtTimeStamp;
    }

    public void setShowAtTimeStamp(Long showAtTimeStamp) {
        this.showAtTimeStamp = showAtTimeStamp;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setIdentifier(Long id) {
        if (id != null)
            this.itemId = KeyFactory.createKey(KIND, id);
    }

    public Long getIdentier() {
        return itemId.getId();
    }

    public Text getPayload() {
        return payload;
    }

    public void setPayload(Text payload) {
        this.payload = payload;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public Long getLastModificationDate() {
        return lastModificationDate;
    }
    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}
