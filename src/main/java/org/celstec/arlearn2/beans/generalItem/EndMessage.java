package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
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
public class EndMessage extends GeneralItem {

    private Boolean syncNecessary;
    private String syncButtonText;

    private Boolean showReset;
    private String resetText;

    private String title;
    private String richTextSyncReady;
    private String richTextSyncNotReady;



    public EndMessage() {
    }

    public Boolean getSyncNecessary() {
        return syncNecessary;
    }

    public void setSyncNecessary(Boolean syncNecessary) {
        this.syncNecessary = syncNecessary;
    }

    public String getSyncButtonText() {
        return syncButtonText;
    }

    public void setSyncButtonText(String syncButtonText) {
        this.syncButtonText = syncButtonText;
    }

    public Boolean getShowReset() {
        return showReset;
    }

    public void setShowReset(Boolean showReset) {
        this.showReset = showReset;
    }

    public String getResetText() {
        return resetText;
    }

    public void setResetText(String resetText) {
        this.resetText = resetText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRichTextSyncReady() {
        return richTextSyncReady;
    }

    public void setRichTextSyncReady(String richTextSyncReady) {
        this.richTextSyncReady = richTextSyncReady;
    }

    public String getRichTextSyncNotReady() {
        return richTextSyncNotReady;
    }

    public void setRichTextSyncNotReady(String richTextSyncNotReady) {
        this.richTextSyncNotReady = richTextSyncNotReady;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        EndMessage other = (EndMessage ) obj;
        return
                nullSafeEquals(getSyncNecessary(), other.getSyncNecessary()) ;

    }

    public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            EndMessage mct = (EndMessage) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (mct.getSyncNecessary() != null) returnObject.put("syncNecessary", mct.getSyncNecessary());
                if (mct.getSyncButtonText() != null) returnObject.put("syncButtonText", mct.getSyncButtonText());
                if (mct.getShowReset() != null) returnObject.put("showReset", mct.getShowReset());
                if (mct.getResetText() != null) returnObject.put("resetText", mct.getResetText());
                if (mct.getTitle() != null) returnObject.put("title", mct.getTitle());
                if (mct.getRichTextSyncReady() != null) returnObject.put("richTextSyncReady", mct.getRichTextSyncReady());
                if (mct.getRichTextSyncNotReady() != null) returnObject.put("richTextSyncNotReady", mct.getRichTextSyncNotReady());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };

    public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

        @Override
        public EndMessage toBean(JSONObject object) {
            EndMessage mct = new EndMessage();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            EndMessage mctItem = (EndMessage) genericBean;
            if (object.has("syncNecessary")) mctItem.setSyncNecessary(object.getBoolean("syncNecessary"));
            if (object.has("syncButtonText")) mctItem.setSyncButtonText(object.getString("syncButtonText"));

            if (object.has("showReset")) mctItem.setShowReset(object.getBoolean("showReset"));
            if (object.has("resetText")) mctItem.setResetText(object.getString("resetText"));

            if (object.has("title")) mctItem.setTitle(object.getString("title"));
            if (object.has("richTextSyncReady")) mctItem.setRichTextSyncReady(object.getString("richTextSyncReady"));
            if (object.has("richTextSyncNotReady")) mctItem.setRichTextSyncNotReady(object.getString("richTextSyncNotReady"));
        };
    };

}
