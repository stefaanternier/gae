package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.celstec.arlearn2.beans.run.GeneralItemVisibility;

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
public class GeneralItemVisibilityEntity {
    public static String KIND = "GeneralItemVisibilityJDO";

    public static String COL_DELETED= "deleted";
    public static String COL_EMAIL= "email";
    public static String COL_GENERALITEMID= "generalItemId";
    public static String COL_RUNID= "runId";
    public static String COL_STATUS= "status";
    public static String COL_TIMESTAMP= "timeStamp";
    public static String COL_LASTMODIFICATIONDATE= "lastModificationDate";

    public GeneralItemVisibilityEntity(){

    }

    public GeneralItemVisibilityEntity(Entity entity){
        this.id = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.email = (String) entity.getProperty(COL_EMAIL);

        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.generalItemId = (Long) entity.getProperty(COL_GENERALITEMID);
        this.status =((Long) entity.getProperty(COL_STATUS)).intValue();
        this.runId = (Long) entity.getProperty(COL_RUNID);
        this.timeStamp = (Long) entity.getProperty(COL_TIMESTAMP);

    }

    public Entity toEntity() {
        Entity result = null;
        if (this.id == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.id.getId());
        }
        result.setProperty(COL_DELETED,this.deleted);
        result.setProperty(COL_EMAIL,this.email);
        result.setProperty(COL_GENERALITEMID,this.generalItemId);
        result.setProperty(COL_RUNID,this.runId);
        result.setProperty(COL_STATUS,this.status);
        result.setProperty(COL_TIMESTAMP,this.timeStamp);
        result.setProperty(COL_LASTMODIFICATIONDATE,this.lastModificationDate);
        return result;

    }


    public  GeneralItemVisibility toBean() {

        GeneralItemVisibility giv = new GeneralItemVisibility();
        giv.setEmail(getEmail());
        giv.setGeneralItemId(getGeneralItemId());
        giv.setLastModificationDate(getLastModificationDate());
        giv.setRunId(getRunId());
        giv.setTimeStamp(getTimeStamp());
        giv.setStatus(getStatus());
        return giv;
    }

    //from run
    public Key getKey(){
        return id;
    }
    protected Key id;
    private Long runId;
    private Boolean deleted;

    public Long getId() {
        return id.getId();
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Boolean getDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public Boolean getDeletedBis() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    private Long generalItemId;
    private String email;
    private Integer status;
    private Long timeStamp;
    private Long lastModificationDate;

    public Long getGeneralItemId() {
        return generalItemId;
    }

    public void setGeneralItemId(Long generalItemId) {
        this.generalItemId = generalItemId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

}
