package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.account.Account;

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
public class ContactEntity {

    public final static int PENDING = 1;
    public final static int ACCEPTED = 2;
    public final static int DELETED = 3;

    public static String KIND = "ContactJDO";

    public static String COL_FROMACCOUNTTYPE = "fromAccountType";
    public static String COL_FROMLOCALID = "fromLocalId";
    public static String COL_LASTMODIFICATIONDATE = "lastModificationDate";
    public static String COL_STATUS = "status";
    public static String COL_TOACCOUNTTYPE = "toAccountType";
    public static String COL_TOLOCALID = "toLocalId";
    public static String COL_TOEMAIL = "toEmail";


    public ContactEntity(){

    }

    public ContactEntity(Entity entity){
        this.uniqueId = entity.getKey();
        if ( entity.getProperty(COL_FROMACCOUNTTYPE) !=null ) this.fromAccountType = ((Long) entity.getProperty(COL_FROMACCOUNTTYPE)).intValue();
        this.fromLocalId = (String) entity.getProperty(COL_FROMLOCALID);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        if ( entity.getProperty(COL_STATUS) !=null ) this.status = ((Long) entity.getProperty(COL_STATUS)).intValue();
        if ( entity.getProperty(COL_TOACCOUNTTYPE) !=null )
            this.toAccountType = ((Long) entity.getProperty(COL_TOACCOUNTTYPE)).intValue();
        this.toLocalId = (String) entity.getProperty(COL_TOLOCALID);
        this.toEmail = (String) entity.getProperty(COL_TOEMAIL);
    }

    public Entity toEntity() {
        Entity result = new Entity(KIND, this.uniqueId.getName());
        result.setProperty(COL_FROMACCOUNTTYPE, this.fromAccountType);
        result.setProperty(COL_FROMLOCALID, this.fromLocalId);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_STATUS, this.status);
        result.setProperty(COL_TOACCOUNTTYPE, this.toAccountType);
        result.setProperty(COL_TOLOCALID, this.toLocalId);
        result.setProperty(COL_TOEMAIL, this.toEmail);
        return result;
    }

    private Long lastModificationDate;
    private Key uniqueId;
    private Integer fromAccountType;
    private String fromLocalId;
    private Integer toAccountType;
    private String toLocalId;
    private String toEmail;
    private Integer status;

    public String getUniqueId() {
        return uniqueId.getName();
    }

    public void setUniqueId() {
        if (getToEmail() != null) {
            this.uniqueId = KeyFactory.createKey(ContactEntity.KIND, getFromAccountType() + ":" + getFromLocalId() + ":" + getToEmail());
        } else {
            this.uniqueId = KeyFactory.createKey(ContactEntity.KIND, getFromAccountType() + ":" + getFromLocalId() + ":" + getToAccountType() + ":" + getToLocalId());
        }
    }

    public void setUniqueId(String id) {
        this.uniqueId = KeyFactory.createKey(ContactEntity.KIND, id);
    }

    public Integer getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(Integer fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getFromLocalId() {
        return fromLocalId;
    }

    public void setFromLocalId(String fromLocalId) {
        this.fromLocalId = fromLocalId;
    }

    public Integer getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(Integer toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getToLocalId() {
        return toLocalId;
    }

    public void setToLocalId(String toLocalId) {
        this.toLocalId = toLocalId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getToFullId() {
        return getToAccountType()+":"+getToLocalId();
    }
}
