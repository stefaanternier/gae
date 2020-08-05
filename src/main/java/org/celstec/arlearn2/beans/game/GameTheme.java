package org.celstec.arlearn2.beans.game;

import com.google.appengine.api.datastore.Entity;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameTheme extends Bean {
    private Long themeId;

    private String primaryColor;
    private String secondaryColor;

    private boolean global;
    private String fullAccount;
    private String category;
    private String name;

    private String iconPath;
    private String backgroundPath;
    private String correctPath;
    private String wrongPath;

    public static GameTheme from(Entity entity) {
        GameTheme theme = new GameTheme();
        theme.setThemeId(entity.getKey().getId());
        theme.setPrimaryColor((String) entity.getProperty("primaryColor"));
        theme.setSecondaryColor((String) entity.getProperty("secondaryColor"));

        theme.setGlobal((boolean) entity.getProperty("global"));
        theme.setFullAccount((String) entity.getProperty("fullAccount"));
        theme.setCategory((String) entity.getProperty("category"));
        theme.setName((String) entity.getProperty("name"));

        theme.setIconPath((String) entity.getProperty("iconPath"));
        theme.setBackgroundPath((String) entity.getProperty("backgroundPath"));
        theme.setCorrectPath((String) entity.getProperty("correctPath"));
        theme.setWrongPath((String) entity.getProperty("wrongPath"));
        return theme;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getFullAccount() {
        return fullAccount;
    }

    public void setFullAccount(String fullAccount) {
        this.fullAccount = fullAccount;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getCorrectPath() {
        return correctPath;
    }

    public void setCorrectPath(String correctPath) {
        this.correctPath = correctPath;
    }

    public String getWrongPath() {
        return wrongPath;
    }

    public void setWrongPath(String wrongPath) {
        this.wrongPath = wrongPath;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public GameTheme toBean(JSONObject object) {
            GameTheme bean = new GameTheme();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            GameTheme bean = (GameTheme) genericBean;
            if (object.has("themeId")) bean.setThemeId(object.getLong("themeId"));

            if (object.has("primaryColor")) bean.setPrimaryColor(object.getString("primaryColor"));
            if (object.has("secondaryColor")) bean.setSecondaryColor(object.getString("secondaryColor"));

            if (object.has("global")) bean.setGlobal(object.getBoolean("global"));
            if (object.has("fullAccount")) bean.setFullAccount(object.getString("fullAccount"));
            if (object.has("category")) bean.setCategory(object.getString("category"));
            if (object.has("name")) bean.setName(object.getString("name"));

            if (object.has("iconPath")) bean.setIconPath(object.getString("iconPath"));
            if (object.has("backgroundPath")) bean.setBackgroundPath(object.getString("backgroundPath"));
            if (object.has("correctPath")) bean.setCorrectPath(object.getString("correctPath"));
            if (object.has("wrongPath")) bean.setWrongPath(object.getString("wrongPath"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            GameTheme teamBean = (GameTheme) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (teamBean.getThemeId() != null) returnObject.put("themeId", teamBean.getThemeId());

                if (teamBean.getPrimaryColor() != null) returnObject.put("primaryColor", teamBean.getPrimaryColor());
                if (teamBean.getSecondaryColor() != null) returnObject.put("secondaryColor", teamBean.getSecondaryColor());

                returnObject.put("global", teamBean.isGlobal());
                if (teamBean.getFullAccount() != null) returnObject.put("fullAccount", teamBean.getFullAccount());
                if (teamBean.getCategory() != null) returnObject.put("category", teamBean.getCategory());
                if (teamBean.getName() != null) returnObject.put("name", teamBean.getName());

                if (teamBean.getIconPath() != null) returnObject.put("iconPath", teamBean.getIconPath());
                if (teamBean.getBackgroundPath() != null) returnObject.put("backgroundPath", teamBean.getBackgroundPath());
                if (teamBean.getCorrectPath() != null) returnObject.put("correctPath", teamBean.getCorrectPath());
                if (teamBean.getWrongPath() != null) returnObject.put("wrongPath", teamBean.getWrongPath());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}


