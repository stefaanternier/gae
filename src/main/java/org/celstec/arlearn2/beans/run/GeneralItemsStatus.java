package org.celstec.arlearn2.beans.run;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */

public class GeneralItemsStatus extends RunBean{

    private Long runId;
    private Long generalItemId;
    private Integer status;
    private Long serverCreationTime;
    private Long lastModificationDate;




    public static BeanDeserializer deserializer = new RunBeanDeserializer(){

        @Override
        public GeneralItemsStatus toBean(JSONObject object) {
            GeneralItemsStatus bean = new GeneralItemsStatus();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            GeneralItemsStatus bean = (GeneralItemsStatus) genericBean;
            if (object.has("generalItemId")) bean.setGeneralItemId(object.getLong("generalItemId"));
            if (object.has("status")) bean.setStatus(object.getInt("status"));
            if (object.has("serverCreationTime")) bean.setServerCreationTime(object.getLong("serverCreationTime"));
            if (object.has("lastModificationDate")) bean.setLastModificationDate(object.getLong("lastModificationDate"));
        }
    };

    public static BeanSerializer serializer = new RunBeanSerialiser() {

        @Override
        public JSONObject toJSON(Object bean) {
            GeneralItemsStatus gItemStatusBean = (GeneralItemsStatus) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gItemStatusBean.getGeneralItemId() != null) returnObject.put("generalItemId", gItemStatusBean.getGeneralItemId());
                if (gItemStatusBean.getStatus() != null) returnObject.put("status", gItemStatusBean.getStatus());
                if (gItemStatusBean.getServerCreationTime() != null) returnObject.put("serverCreationTime", gItemStatusBean.getServerCreationTime());
                if (gItemStatusBean.getLastModificationDate() != null) returnObject.put("lastModificationDate", gItemStatusBean.getLastModificationDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };


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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getServerCreationTime() {
        return serverCreationTime;
    }

    public void setServerCreationTime(Long serverCreationTime) {
        this.serverCreationTime = serverCreationTime;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

}

