package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.celstec.arlearn2.beans.run.Action;

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
public class ActionEntity {
    public static String KIND = "ActionJDO";

    public static String COL_ACTION = "action";
    public static String COL_GENERATLITEMID = "generalItemId";
    public static String COL_GENERALITEMTYPE = "generalItemType";
    public static String COL_RUNID= "runId";
    public static String COL_TIME = "time";
    public static String COL_USERID = "userId";


    public ActionEntity(){

    }

    public ActionEntity(Entity entity){
        this.id = entity.getKey();
        this.action = (String) entity.getProperty(COL_ACTION);
        this.generalItemId = (Long) entity.getProperty(COL_GENERATLITEMID);
        this.generalItemType = (String) entity.getProperty(COL_GENERALITEMTYPE);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.time = (Long) entity.getProperty(COL_TIME);
        this.userId = (String) entity.getProperty(COL_USERID);

    }

    public Entity toEntity() {
        Entity result;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getName());
        }
        result.setProperty(COL_ACTION, this.action);
        result.setProperty(COL_GENERATLITEMID, this.generalItemId);
        result.setProperty(COL_GENERALITEMTYPE, this.generalItemType);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_TIME, this.time);
        result.setProperty(COL_USERID, this.userId);
        return result;
    }

    public Action toBean() {
        Action actionBean = new Action();
        actionBean.setAction(getAction());
        actionBean.setGeneralItemId(getGeneralItemId());
        actionBean.setGeneralItemType(getGeneralItemType());
        actionBean.setRunId(getRunId());
        actionBean.setTime(getTime());
        actionBean.setTimestamp(getTime());
        actionBean.setUserId(getUserId());
        actionBean.setIdentifier(getId());
        return actionBean;
    }


    protected Key id;
    private String action;
    private Long generalItemId;
    private String generalItemType;
    private Long runId;
    private Long time;
    private String userId;

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

    public String getGeneralItemType() {
        return generalItemType;
    }

    public void setGeneralItemType(String generalItemType) {
        this.generalItemType = generalItemType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
