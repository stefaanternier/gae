package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PictureQuestion extends NarratorItem {


    public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

        @Override
        public PictureQuestion toBean(JSONObject object) {
            PictureQuestion mct = new PictureQuestion();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {

            super.initBean(object, genericBean);
            PictureQuestion mctItem = (PictureQuestion) genericBean;

            if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
            if (object.has("text")) mctItem.setText(object.getString("text"));
        }
    };

    public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            PictureQuestion gi = (PictureQuestion) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gi.getText() != null) returnObject.put("text", gi.getText());
                if (gi.getRichText() != null) returnObject.put("richText", gi.getRichText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };
}
