package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.run.Thread;

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
public class ThreadEntity {
    public static String KIND = "ThreadJDO";

    public static String COL_DELETED= "deleted";
    public static String COL_ISDEFAULT= "isDefault";
    public static String COL_LASTMODIFICATIONDATE= "lastModificationDate";
    public static String COL_NAME= "name";
    public static String COL_RUNID= "runId";

    public ThreadEntity(){

    }

    public ThreadEntity(Entity entity){
        this.threadId = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.name = (String) entity.getProperty(COL_NAME);
        this.runId = (Long) entity.getProperty(COL_RUNID);

        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.isDefault = (Boolean) entity.getProperty(COL_ISDEFAULT);

    }
    public Entity toEntity() {
        Entity result = null;
        if (this.threadId == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, this.threadId.getName());
        }
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_NAME, this.name);
        result.setProperty(COL_RUNID, this.runId);

        result.setProperty(COL_ISDEFAULT, this.isDefault);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        return result;
    }

    public Thread toBean() {
        Thread bean = new Thread();
        bean.setName(getName());
        bean.setDeleted(getDeleted());
        bean.setRunId(getRunId());
        bean.setThreadId(getThreadId());
        bean.setLastModificationDate(getLastModificationDate());
        return bean;
    }

    private Key threadId;
    private Long runId;

    private String name;
    private Boolean isDefault;
    private Boolean deleted;
    private Long lastModificationDate;

    public Long getThreadId() {
        return threadId.getId();
    }

    public void setThreadId(Long threadId) {
        this.threadId = KeyFactory.createKey(KIND, threadId);

    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getIsDefault() {
        return (isDefault == null) ? false : isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}
