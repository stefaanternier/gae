package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Team;

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
public class TeamEntity {
    public static String KIND = "TeamJDO";

    public static String COL_DELETED= "deleted";

    public static String COL_NAME= "name";
    public static String COL_RUNID= "runId";

    public TeamEntity(){

    }

    public TeamEntity(Entity entity){
        this.id = entity.getKey();
        this.deleted = (Boolean) entity.getProperty(COL_DELETED);
        this.name = (String) entity.getProperty(COL_NAME);
        this.runId = (Long) entity.getProperty(COL_RUNID);


    }
    public Entity toEntity() {
        Entity result = new Entity(KIND, this.id.getName());
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_NAME, this.name);
        result.setProperty(COL_RUNID, this.runId);
        return result;
    }

    public Team toBean() {
        Team teamBean = new Team();
        teamBean.setName(getName());
        teamBean.setRunId(getRunId());
        teamBean.setTeamId(getTeamIdString());
        return teamBean;
    }

    protected Key id;
    private Long runId;
    private Boolean deleted;

    private String name;

    //from run

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

    public Key getTeamId() {
        return id;
    }

    public String getTeamIdString() {
        return id.getName();
    }

    public void setTeamId(String teamId) {
        if (teamId != null)
            setTeamId(KeyFactory.createKey(TeamEntity.KIND, teamId));
    }

    public void setTeamId(Key teamId) {
        this.id = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
