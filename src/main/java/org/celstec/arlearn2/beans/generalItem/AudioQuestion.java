package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AudioQuestion extends NarratorItem {

    public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

        @Override
        public AudioQuestion toBean(JSONObject object) {
            AudioQuestion mct = new AudioQuestion();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {

            super.initBean(object, genericBean);
            AudioQuestion mctItem = (AudioQuestion) genericBean;

            if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
            if (object.has("text")) mctItem.setText(object.getString("text"));
        }
    };

    public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            AudioQuestion gi = (AudioQuestion) bean;
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
