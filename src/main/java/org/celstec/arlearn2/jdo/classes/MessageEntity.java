package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.run.Message;

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
public class MessageEntity {

    public static String KIND = "MessageJDO";


    public static String COL_THREADID = "threadId";
    public static String COL_SUBJECT = "subject";
    public static String COL_BODY= "body";
    public static String COL_DATE = "date";
    public static String COL_LASTMODIFICATIONDATE = "lastModificationDate";
    public static String COL_USERIDS = "userIds";
    public static String COL_TEAMIDS = "teamIds";
    public static String COL_DELETED = "deleted";
    public static String COL_RUNID = "runId";
    public static String COL_SENDERPROVIDERID = "senderProviderId";
    public static String COL_SENDERID = "senderId";

    public MessageEntity(){

    }

    public MessageEntity(Entity entity){
        this.messageId = entity.getKey().getId();
        this.threadId = (Long) entity.getProperty(COL_THREADID);
        this.subject = (Text) entity.getProperty(COL_SUBJECT);
        this.body = (Text) entity.getProperty(COL_BODY);
        this.date = (Long) entity.getProperty(COL_DATE);
        this.lastModificationDate = (Long) entity.getProperty(COL_LASTMODIFICATIONDATE);
        this.userIds = (String) entity.getProperty(COL_USERIDS);
        this.teamIds = (String) entity.getProperty(COL_USERIDS);
        this.deleted = (Boolean) entity.getProperty(COL_TEAMIDS);
        this.runId = (Long) entity.getProperty(COL_RUNID);
        if ( entity.getProperty(COL_SENDERPROVIDERID) !=null )
            this.senderProviderId = ((Long) entity.getProperty(COL_SENDERPROVIDERID)).intValue();
        this.senderId = (String) entity.getProperty(COL_SENDERID);
    }

    public Entity toEntity() {
        Entity result = new Entity(KIND, this.messageId);
        result.setProperty(COL_SUBJECT, this.subject);
        result.setProperty(COL_BODY, this.body);
        result.setProperty(COL_DATE, this.date);
        result.setProperty(COL_LASTMODIFICATIONDATE, this.lastModificationDate);
        result.setProperty(COL_USERIDS, this.userIds);
        result.setProperty(COL_TEAMIDS, this.teamIds);
        result.setProperty(COL_DELETED, this.deleted);
        result.setProperty(COL_RUNID, this.runId);
        result.setProperty(COL_SENDERPROVIDERID, this.senderProviderId);
        result.setProperty(COL_SENDERID, this.senderId);
        return result;
    }

    public Message toBean() {
        Message bean = new Message();
        bean.setThreadId(getThreadId());
        bean.setRunId(getRunId());
        bean.setSubject(getSubject());
        bean.setBody(getBody());
        bean.setMessageId(getMessageId());
        bean.setDate(getDate());
        bean.setDeleted(getDeleted());
        bean.setSenderId(getSenderId());
        bean.setSenderProviderId(getSenderProviderId());
        return bean;
    }

    private Long messageId;
    private Long threadId;
    private Text subject;
    private Text body;
    private Long date;
    private Long lastModificationDate;
    private String userIds;
    private String teamIds;
    private Boolean deleted;
    private Long runId;
    private Integer senderProviderId;
    private String senderId;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getSubject() {
        return subject.getValue();
    }

    public void setSubject(String subject) {
        this.subject = new Text(subject);
    }

    public String getBody() {
        return body.getValue();
    }

    public void setBody(String body) {
        this.body = new Text(body);
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(String teamIds) {
        this.teamIds = teamIds;
    }

    public Boolean getDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Integer getSenderProviderId() {
        return senderProviderId;
    }

    public void setSenderProviderId(Integer senderProviderId) {
        this.senderProviderId = senderProviderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

}
