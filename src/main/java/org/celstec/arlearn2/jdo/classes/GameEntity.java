package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;

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
public class GameEntity {


    public static String KIND = "GameJDO";

    public static String COL_DELETED = "deleted";
    public static String COL_LASTMODIFICATIONDATE = "lastModificationDate";

    public static String COL_APPSTOREURL = "appStoreUrl";
    public static String COL_CONFIG = "config";
    public static String COL_CREATOREMAIL = "creatorEmail";
    public static String COL_DESCRIPTION = "description";
    public static String COL_FEATURED = "featured";
    public static String COL_FEEDURL = "feedUrl";

    public static String COL_GOOGLEPLAYURL = "googlePlayUrl";
    public static String COL_LANGUAGE = "language";

    public static String COL_LAT = "lat";
    public static String COL_LNG = "lng";
    public static String COL_LICENSECODE = "licenseCode";
    public static String COL_OWNER = "owner";
    public static String COL_SHARING = "sharing";
    public static String COL_THEME = "theme";
    public static String COL_TITLE = "title";
    public static String COL_SPLASHSCREEN = "splashScreen";
    public static String COL_PRIVATE_MODE = "privateMode";

    //from GameClass
    private Key gameId;
    protected Boolean deleted;
    private Long lastModificationDate;

    //    public Long getGameId() {
//        return gameId.getId();
//    }
//    public void setGameId(Long gameId) {
//        this.gameId = KeyFactory.createKey(KIND, gameId);
//    }
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

    //from Game
    private String title;
    private String splashScreen;
    private String owner;
    private String creatorEmail;
    private String feedUrl;
    private Text config;
    private Text description;
    private Integer sharing;
    private String licenseCode;
    private Double lat;
    private Double lng;
    private Boolean featured;
    private String language;
    private Long theme;
    private String googlePlayUrl;
    private String appStoreUrl;

    private Boolean privateMode;

    public Long getGameId() {
        return gameId.getId();
    }

    public void setGameId(Long gameId) {
        if (gameId != null)
            setGameId(KeyFactory.createKey(KIND, gameId));
    }

    public void setGameId(Key gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(String splashScreen) {
        this.splashScreen = splashScreen;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getConfig() {
        if (config == null) return null;
        return config.getValue();
    }

    public void setConfig(String config) {
        this.config = new Text(config);
    }

    public String getDescription() {
        if (description == null) return null;
        return description.getValue();
    }

    public void setDescription(String description) {
        if (description != null) this.description = new Text(description);
    }

    public Integer getSharing() {
        if (sharing == null) return Game.PRIVATE;
        return sharing;
    }

    public void setSharing(Integer sharing) {
        this.sharing = sharing;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getTheme() {
        return theme;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public String getGooglePlayUrl() {
        return googlePlayUrl;
    }

    public void setGooglePlayUrl(String googlePlayUrl) {
        this.googlePlayUrl = googlePlayUrl;
    }

    public String getAppStoreUrl() {
        return appStoreUrl;
    }

    public void setAppStoreUrl(String appStoreUrl) {
        this.appStoreUrl = appStoreUrl;
    }

    public Boolean getPrivateMode() {
        return privateMode;
    }

    public void setPrivateMode(Boolean privateMode) {
        this.privateMode = privateMode;
    }

    public GameEntity() {

    }

    public GameEntity(Entity entity) {
        this.gameId = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.title = (String) entity.getProperty(COL_TITLE);
        this.splashScreen = (String) entity.getProperty(COL_SPLASHSCREEN);
        this.owner = (String) entity.getProperty(COL_OWNER);
        this.creatorEmail = (String) entity.getProperty(COL_CREATOREMAIL);
        this.feedUrl = (String) entity.getProperty(COL_FEEDURL);
        this.config = (Text) entity.getProperty(COL_CONFIG);
        this.description = (Text) entity.getProperty(COL_DESCRIPTION);
        if (entity.getProperty(COL_SHARING) != null) {
            this.sharing = ((Long) entity.getProperty(COL_SHARING)).intValue();
        }

        this.licenseCode = (String) entity.getProperty(COL_LICENSECODE);
        this.lat = (Double) entity.getProperty(COL_LAT);
        this.lng = (Double) entity.getProperty(COL_LNG);
        this.featured = (Boolean) entity.getProperty(COL_FEATURED);
        this.language = (String) entity.getProperty(COL_LANGUAGE);
        if (entity.getProperty(COL_THEME) != null) {
            this.theme = ((Long) entity.getProperty(COL_THEME));
        }
        this.googlePlayUrl = (String) entity.getProperty(COL_GOOGLEPLAYURL);
        this.appStoreUrl = (String) entity.getProperty(COL_APPSTOREURL);
        this.privateMode = (Boolean) entity.getProperty(COL_PRIVATE_MODE);
    }

    public Entity toEntity() {
        Entity result = null;
        if (this.gameId == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.gameId.getId());
        }

        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_APPSTOREURL, this.appStoreUrl);
        result.setProperty(COL_CONFIG, this.config);
        result.setProperty(COL_CREATOREMAIL, this.creatorEmail);
        result.setProperty(COL_DESCRIPTION, this.description);
        result.setProperty(COL_FEATURED, this.featured);
        result.setProperty(COL_FEEDURL, this.feedUrl);
        result.setProperty(COL_GOOGLEPLAYURL, this.googlePlayUrl);
        result.setProperty(COL_LANGUAGE, this.language);
        result.setProperty(COL_LAT, this.lat);
        result.setProperty(COL_LNG, this.lng);
        result.setProperty(COL_LICENSECODE, this.licenseCode);
        result.setProperty(COL_OWNER, this.owner);
        result.setProperty(COL_SHARING, this.sharing);
        result.setProperty(COL_THEME, this.theme);
        result.setProperty(COL_TITLE, this.title);
        result.setProperty(COL_SPLASHSCREEN, this.splashScreen);
        result.setProperty(COL_PRIVATE_MODE, this.privateMode);
        return result;

    }

    public Game toGame() {
        Game game = new Game();
        game.setCreator(getCreatorEmail());
        game.setTitle(getTitle());
        game.setSplashScreen(getSplashScreen());
        game.setFeedUrl(getFeedUrl());
        game.setGameId(getGameId());
        game.setOwner(getOwner());
        game.setDescription(getDescription());
        game.setSharing(getSharing());
        game.setLng(getLng());
        game.setLat(getLat());
        game.setLanguage(getLanguage());
        game.setAppStoreUrl(getAppStoreUrl());
        game.setGooglePlayUrl(getGooglePlayUrl());
        if (getTheme() != null) game.setTheme(getTheme());
        if (getLicenseCode() != null) game.setLicenseCode(getLicenseCode());
        if (getLastModificationDate() != null) {
            game.setLastModificationDate(getLastModificationDate());
        }
        if (getConfig() != null && !"".equals(getConfig())) {
            JsonBeanDeserializer jbd;
            try {
                jbd = new JsonBeanDeserializer(getConfig());
                Config config = (Config) jbd.deserialize(Config.class);
//				config.setBoundingBoxSouth(5.5d);
                game.setConfig(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (getDeleted() == null) {
            game.setDeleted(false);
        } else {
            game.setDeleted(getDeleted());
        }
        if (getPrivateMode() == null) {
            game.setPrivateMode(false);
        } else {
            game.setPrivateMode(getPrivateMode());
        }
        return game;
    }


}
