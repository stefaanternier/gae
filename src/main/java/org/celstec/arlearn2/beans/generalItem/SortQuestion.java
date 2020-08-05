package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
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
public class SortQuestion extends GeneralItem {

    public static String answersType = "org.celstec.arlearn2.beans.generalItem.SortQuestionItem";

    private List<SortQuestionItem> answers = new Vector();
    private String richText;
    private String text;

    private Boolean showFeedback;
    private String feedbackWrong;
    private String feedbackCorrect;

    public SortQuestion() {

    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getShowFeedback() {
        return showFeedback;
    }

    public void setShowFeedback(Boolean showFeedback) {
        this.showFeedback = showFeedback;
    }

    public String getFeedbackWrong() {
        return feedbackWrong;
    }

    public void setFeedbackWrong(String feedbackWrong) {
        this.feedbackWrong = feedbackWrong;
    }

    public String getFeedbackCorrect() {
        return feedbackCorrect;
    }

    public void setFeedbackCorrect(String feedbackCorrect) {
        this.feedbackCorrect = feedbackCorrect;
    }

    public List<SortQuestionItem> getAnswers() {
        return answers;
    }
    public void setAnswers(List<SortQuestionItem> answers) {
        this.answers = answers;
    }

    public void addAnswer(SortQuestionItem sortQuestionItem) {
        answers.add(sortQuestionItem);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        MultipleChoiceTest other = (MultipleChoiceTest ) obj;
        return
                nullSafeEquals(getAnswers(), other.getAnswers()) &&
                        nullSafeEquals(getText(), other.getText()) &&
                        nullSafeEquals(getRichText(), other.getRichText()) ;

    }

    public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            SortQuestion mct = (SortQuestion) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (mct.getText() != null) returnObject.put("text", mct.getText());
                if (mct.getRichText() != null) returnObject.put("richText", mct.getRichText());
                if (mct.getAnswers() != null) returnObject.put("answers", ListSerializer.toJSON(mct.getAnswers()));
                if (mct.getShowFeedback()!= null) returnObject.put("showFeedback", mct.getShowFeedback());

                if (mct.getFeedbackCorrect() != null) returnObject.put("feedbackCorrect", mct.getFeedbackCorrect());
                if (mct.getFeedbackWrong() != null) returnObject.put("feedbackWrong", mct.getFeedbackWrong());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };

    public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

        @Override
        public SortQuestion toBean(JSONObject object) {
            SortQuestion mct = new SortQuestion();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            SortQuestion mctItem = (SortQuestion) genericBean;
            if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
            if (object.has("text")) mctItem.setText(object.getString("text"));
            if (object.has("answers")) mctItem.setAnswers(ListDeserializer.toBean(object.getJSONArray("answers"), SortQuestionItem.class));
            if (object.has("showFeedback")) mctItem.setShowFeedback(object.getBoolean("showFeedback"));
            if (object.has("feedbackCorrect")) mctItem.setFeedbackCorrect(object.getString("feedbackCorrect"));
            if (object.has("feedbackWrong")) mctItem.setFeedbackWrong(object.getString("feedbackWrong"));
        };
    };

}
