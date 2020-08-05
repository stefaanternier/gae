package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceTest;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.ResponseManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

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
public class ResponseAsHtmlDelegator {

    private int punten = 0;
    private String userId;
    private long runId;
    private User user;
    private List<GeneralItem> generalItems;
    private List<Response> responses;
    private HashSet<Long> itemIdsForWhichResponsesExist = new HashSet<Long>();

    private TreeMap<Long, Response> responsesOrderedByTime = new TreeMap<Long, Response>();
    private HashMap<Long, GeneralItem> generalItemHashMap = new HashMap<Long, GeneralItem>();
    private String mailOnleesBaar;

    public ResponseAsHtmlDelegator(String userId, long runId) {
        this.userId = userId;
        this.runId = runId;
    }

    public void loadRun(){
         user = UserManager.getUser(runId, userId);
    }

    public void loadGeneralItems(){
        generalItems = GeneralItemManager.getGeneralitemsFromUntil(user.getGameId(), 0l, System.currentTimeMillis());
        for (GeneralItem generalItem: generalItems) {
            generalItemHashMap.put(generalItem.getId(), generalItem);
        }
    }

    public void loadResponses(){
        responses = ResponseManager.getResponseForRunId(runId);
        for (Response response: responses) {
            itemIdsForWhichResponsesExist.add(response.getGeneralItemId());
            responsesOrderedByTime.put(response.getTimestamp(), response);
            if (response.getResponseValue().contains("\"isCorrect\":false")) {
                punten = punten -1;
            }
            if (response.getResponseValue().contains("\"isCorrect\":true")) {
                punten = punten +2;
            }
        }
    }

    public String toString(){
        String returnString = "<!DOCTYPE html>";
        returnString += "<html>";
        returnString += "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Mijn Stad resultaten</title>\n"  +
                "</head>" +
                "<style>\n" +
                "body {\n" +
                "    background-image: url(\"/game/"+user.getGameId()+"/resultBackground\");\n" +
                "        background-repeat:no-repeat;\n" +
                "        font-family:\"Myriad Pro\", Helvetica, Arial, sans-serif;\n" +
                "        background-size: 100%;\n" +
                "    background-color: #1b80b4;\n" +
                "        color:white;\n" +
                "        margin-left:40px;\n" +
                "}\n" +
                "\n" +
                "h1 {\n" +
                "    color: #faaf1e;\n" +
                "        font-weight:300;\n" +
                "} \n" +
                ".score {\n" +
                "        font-weight:bold;\n" +
                "        margin-bottom:30px;\n" +
                "}\n" +
                ".naam {\n" +
                "}\n" +
                "hr { \n" +
                "    \tdisplay: block;\n" +
                "    \tmargin-top: 0.5em;\n" +
                "    \tmargin-bottom: 0.5em;\n" +
                "    \tmargin-left: auto;\n" +
                "    \tmargin-right: auto;\n" +
                "    \tborder-style: inset;\n" +
                "    \tborder-width: 1px;\n" +
                "\tcolor: #faaf1e;\n" +
                "\tbackground-color: #faaf1e;\n" +
                "}" +
                ".vraag {\n" +
                "        color: #faaf1e;\n" +
                "        font-weight:300;\n" +
                "        font-size: 20px;\n" +
                "}\n" +
                ".antwoord {     \n" +
                "}\n" +
                "\n" +
                ".antwoord img{  \n" +
                "        width:300px;\n" +
                "        }\n" +
                ".punten {\n" +
                "        margin-bottom:30px;\n" +
                "        font-style:italic;\n" +
                "}\n" +
                "\n" +
                "</style>\n";
        returnString += "<body>";
        if (mailOnleesBaar != null) {
            returnString += mailOnleesBaar;
        }
        returnString += "<h1>Mijn Stad resultaten</h1>";
        Account acc = AccountManager.getAccount(userId);

        returnString += "<div class=\"naam\">"+acc.getName()+"/"+acc.getEmail()+"</div>";
        if (punten == 1){
            returnString += "<div class=\"score\">1 punt</div>";
        } else {
            returnString += "<div class=\"score\">"+punten+" punten</div>";
        }


        returnString += "";
        returnString += "";
        for (Long responseTime: responsesOrderedByTime.keySet()){
            Response response = responsesOrderedByTime.get(responseTime);
            if (generalItemHashMap.containsKey(response.getGeneralItemId())) {
                GeneralItem generalItem = generalItemHashMap.get(response.getGeneralItemId());


                if (generalItem instanceof NarratorItem){
                    returnString += "<div class=\"vraag\" id=\"1v\">"+formatRichText(((NarratorItem) generalItem).getRichText())+"</div>";
                } else
                if (generalItem instanceof SingleChoiceTest){
                    returnString += "<div class=\"vraag\" id=\"1v\">"+formatRichText(((SingleChoiceTest) generalItem).getRichText())+"</div>";
                } else
                if (generalItem instanceof MultipleChoiceTest){
                    returnString += "<div class=\"vraag\" id=\"1v\">"+formatRichText(((MultipleChoiceTest) generalItem).getRichText())+"</div>";
                } else {
                    returnString += "<div class=\"vraag\" id=\"1v\">"+generalItem.getName()+"</div>";
                }
                for (Long responseKey: responsesOrderedByTime.keySet()){
                    response = responsesOrderedByTime.get(responseKey);
                    if (response.getGeneralItemId().longValue() == generalItem.getId().longValue()){
                        returnString += formatResponse(response.getResponseValue());
                    }
                }
                returnString += "<hr>";
                generalItemHashMap.remove(generalItem.getId());
            }
        }

//        for (GeneralItem generalItem: generalItems) {
//            if (itemIdsForWhichResponsesExist.contains(generalItem.getId())){
//                returnString += "<h1>"+generalItem.getName()+"</h1>";
//                for (Response response: responses) {
//                    if (response.getGeneralItemId() !=null && response.getGeneralItemId().longValue() == generalItem.getId().longValue()) {
//                        returnString += "<h2>"+response.getResponseValue()+"</h2>";
//                    }
//                }
//            }
//
//        }
        returnString += "</body>";
        returnString += "</html>";
        return returnString;
    }

    public String formatRichText(String richText) {
        return richText.replace("src=\"game/", " width=\"50%\" src=\"/game/");
    }
    public String formatResponse(String responseValue) {
        try {
            JSONObject responseJson = new JSONObject(responseValue);
            if (responseJson.has("text")) {
                return "<div class=\"antwoord\" id=\"1a\">"+responseJson.getString("text")+"</div>";
            }
            if (responseJson.has("audioUrl")) {
                return "<audio controls=\"controls\">\n" +
                        "    <source src=\""+responseJson.getString("audioUrl")+"\" type=\"audio/mp4\" />\n" +
                        "</audio>";
            }
            if (responseJson.has("isCorrect")) {
                String result = "<div class=\"antwoord\" >"+responseJson.getString("answer")+"</div>";
                if (responseJson.getString("isCorrect").equals("false")){
                    result +="<div class=\"punten\" id=\"2p\">-1 punt</div>";
                } else {
                    result +="<div class=\"punten\" id=\"2p\">2 punten</div>";
                }
                return result;

            }
            if (responseJson.has("imageUrl")){
                return "<div class=\"antwoord\" id=\"5a\"><img src=\""+responseJson.getString("imageUrl")+"\"></div>";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
        return "todo: " + responseValue;

    }

    public void setMailOnleesBaar(String mailOnleesBaar) {
        this.mailOnleesBaar = mailOnleesBaar;
    }
}
