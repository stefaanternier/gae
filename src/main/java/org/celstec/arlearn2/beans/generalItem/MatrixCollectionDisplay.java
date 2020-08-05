package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
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
public class MatrixCollectionDisplay extends GeneralItem {

    private List<DisplayColumn> displayColumns;
    private List<DisplayRow> displayRows;
    private String richText;

    public MatrixCollectionDisplay() {
    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public List<DisplayColumn> getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(List<DisplayColumn> displayColumns) {
        this.displayColumns = displayColumns;
    }

    public List<DisplayRow> getDisplayRows() {
        return displayRows;
    }

    public void setDisplayRows(List<DisplayRow> displayRows) {
        this.displayRows = displayRows;
    }

    public static class DisplayColumn extends Bean {
        private String audioUrl;
        private String imageUrl;
        private Long itemId;

        public DisplayColumn() {

        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }
    }

    public static class DisplayRow extends Bean{
        private String audioUrl;
        private String imageUrl;
        private String action;

        public DisplayRow() {
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static GeneralItemDeserializer matrixCollectionDisplayDeserializer = new GeneralItemDeserializer(){

        @Override
        public MatrixCollectionDisplay toBean(JSONObject object) {
            MatrixCollectionDisplay gi = new MatrixCollectionDisplay();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            MatrixCollectionDisplay gi = (MatrixCollectionDisplay) genericBean;
            if (object.has("richText")) gi.setRichText(object.getString("richText"));

            if (object.has("displayColumns")) gi.setDisplayColumns(ListDeserializer.toBean(object.getJSONArray("displayColumns"), DisplayColumn.class));
            if (object.has("displayRows")) gi.setDisplayRows(ListDeserializer.toBean(object.getJSONArray("displayRows"), DisplayRow.class));


        }

    };


    public static BeanDeserializer displayColumnDeserializer = new BeanDeserializer(){

        @Override
        public DisplayColumn toBean(JSONObject object) {
            DisplayColumn gi = new DisplayColumn();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            DisplayColumn gi = (DisplayColumn) genericBean;

            if (object.has("audioUrl")) gi.setAudioUrl(object.getString("audioUrl"));
            if (object.has("imageUrl")) gi.setImageUrl(object.getString("imageUrl"));
            if (object.has("itemId")) gi.setItemId(object.getLong("itemId"));


        }

    };

    public static BeanDeserializer displayRowsDeserializer = new BeanDeserializer(){

        @Override
        public DisplayRow toBean(JSONObject object) {
            DisplayRow gi = new DisplayRow();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            DisplayRow gi = (DisplayRow) genericBean;
            if (object.has("audioUrl")) gi.setAudioUrl(object.getString("audioUrl"));
            if (object.has("imageUrl")) gi.setImageUrl(object.getString("imageUrl"));
            if (object.has("action")) gi.setAction(object.getString("action"));


        }

    };










    public static GeneralItemSerializer matrixCollectionDisplaySerializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            MatrixCollectionDisplay ou = (MatrixCollectionDisplay) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getDisplayColumns() != null) returnObject.put("displayColumns", ListSerializer.toJSON(ou.getDisplayColumns()));
                if (ou.getDisplayRows() != null) returnObject.put("displayRows", ListSerializer.toJSON(ou.getDisplayRows()));
                if (ou.getRichText() != null) returnObject.put("richText", ou.getRichText());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanSerializer displayColumnSerializer = new BeanSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            DisplayColumn ou = (DisplayColumn) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getAudioUrl() != null) returnObject.put("audioUrl", ou.getAudioUrl());
                if (ou.getImageUrl() != null) returnObject.put("imageUrl", ou.getImageUrl());
                if (ou.getItemId() != null) returnObject.put("itemId", ou.getItemId());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanSerializer displayRowSerializer = new BeanSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            DisplayRow ou = (DisplayRow) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getAudioUrl() != null) returnObject.put("audioUrl", ou.getAudioUrl());
                if (ou.getImageUrl() != null) returnObject.put("imageUrl", ou.getImageUrl());
                if (ou.getAction() != null) returnObject.put("action", ou.getAction());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static void main(String[] args) throws  Exception {
        MatrixCollectionDisplay display = new MatrixCollectionDisplay();
        display.setRichText("test");
        display.setId(6192449487634432l);
        display.setGameId(5066549580791808l);
        display.setName("matrix display");
        display.setAutoLaunch(false);
        display.setSortKey(0);
        display.setDisplayColumns(new ArrayList<DisplayColumn>());
        DisplayColumn column = new DisplayColumn();
        column.setItemId(6649846324789248l);
        column.setAudioUrl("audio");

        DisplayColumn column1 = new DisplayColumn();
        column1.setItemId(6086896371367936l);
        column1.setAudioUrl("audio");

        display.getDisplayColumns().add(column);

        display.setDisplayRows(new ArrayList<DisplayRow>());

        DisplayRow row1 = new DisplayRow();
        row1.setAction("paars");
        row1.setImageUrl("iamge");

        DisplayRow row = new DisplayRow();
        row.setAction("blauw");
        row.setImageUrl("iamge");

       display.getDisplayRows().add(row);
        display.getDisplayRows().add(row1);

        System.out.println("object "+display.toString());
        System.out.println("display object "+ JsonBeanDeserializer.deserialize(display.toString()));
    }

}
