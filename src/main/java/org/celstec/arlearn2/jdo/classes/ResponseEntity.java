package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.run.Response;
import org.json.JSONObject;

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
public class ResponseEntity {
    public static String KIND = "ResponseJDO";

    public static String COL_GENERATLITEMID = "generalItemId";
    public static String COL_RESPONSEVALUE= "responseValue";
    public static String COL_LASTMODIFICATIONDATE= "lastModificationDate";

    public static String COL_REVOKED= "revoked";

    public static String COL_RUNID= "runId";
    public static String COL_TIMESTAMP = "timeStamp";
    public static String COL_USERID = "userId";
    public static String COL_LAT = "lat";
    public static String COL_LNG = "lng";

    public ResponseEntity() {

    }

    public ResponseEntity(Entity entity){
        this.id = entity.getKey();
        this.generalItemId = (Long) entity.getProperty(COL_GENERATLITEMID);
        this.responseValue = (Text) entity.getProperty(COL_RESPONSEVALUE);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.revoked = (Boolean) entity.getProperty(COL_REVOKED);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.lat = (Double) entity.getProperty(COL_LAT);
        this.lng = (Double) entity.getProperty(COL_LNG);
        this.timeStamp = (Long) entity.getProperty(COL_TIMESTAMP);
        this.userId = (String) entity.getProperty(COL_USERID);
    }

    public Entity toEntity() {
        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_GENERATLITEMID, this.generalItemId);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_RESPONSEVALUE,this.responseValue);
        result.setProperty(COL_REVOKED, this.revoked);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_LAT, this.lat);
        result.setProperty(COL_LNG, this.lng);
        result.setProperty(COL_TIMESTAMP, this.timeStamp);
        result.setProperty(COL_USERID, this.userId);
        return result;

    }

    public Response toBean() {
        Response pd = new Response();
        pd.setResponseId(getId());
        pd.setRunId(getRunId());
        pd.setGeneralItemId(getGeneralItemId());
        pd.setTimestamp(getTimeStamp());
        pd.setUserId(getUserId());
        pd.setLat(getLat());
        pd.setLng(getLng());
        pd.setRevoked(isRevoked());
        pd.setLastModificationDate(getLastModificationDate());

        pd.setResponseValue(getResponseValue());
        return pd;
    }

//    public static String normalizeValue(String responseValueString){
//        String responseValueStringNormalized = responseValueString;
//        try {
//            JSONObject valJsonObject = new JSONObject(responseValueString);
//            if (valJsonObject.has("value")){
//                Object valueObject = valJsonObject.get("value");
//                if (valueObject instanceof String) {
//                    String valueObjectAsString = (String) valueObject;
//                    Double valueDouble = Double.parseDouble(valueObjectAsString.replace(",","."));
//                    valJsonObject.remove("value");
//                    valJsonObject.put("value", valueDouble);
//                    responseValueStringNormalized = valJsonObject.toString(2);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return responseValueStringNormalized;
//    }

    protected Key id;
    private Long runId;

    private Long generalItemId;
    private String userId;
    private Text responseValue;
    private Long timeStamp;
    private Long lastModificationDate;
    private Double lat;
    private Double lng;
    private boolean revoked;

    public Long getId() {
        return id.getId();
    }


    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getGeneralItemId() {
        return generalItemId;
    }

    public void setGeneralItemId(Long generalItemId) {
        this.generalItemId = generalItemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResponseValue() {
        return responseValue.getValue();
    }

    public void setResponseValue(String responseValue) {
        this.responseValue = new Text(responseValue);
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }



    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setResponseValue(Text responseValue) {
        this.responseValue = responseValue;
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
}
