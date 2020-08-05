package org.celstec.arlearn2.beans.account;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Invitation extends Bean {


    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    private String localId;
    private Integer accountType;
    private Long lastModificationDate;
    private String invitationId;
    private String fromName;
    public static BeanDeserializer deserializer = new InvitationDeserializer();

    public static class InvitationDeserializer extends BeanDeserializer{

        @Override
        public Invitation toBean(JSONObject object) {
            Invitation bean = new Invitation();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            Invitation bean = (Invitation) genericBean;
            if (object.has("localId")) bean.setLocalId(object.getString("localId"));
            if (object.has("accountType")) bean.setAccountType(object.getInt("accountType"));
            if (object.has("lastModificationDate")) bean.setLastModificationDate(object.getLong("lastModificationDate"));
            if (object.has("invitationId")) bean.setInvitationId(object.getString("invitationId"));
            if (object.has("fromName")) bean.setFromName(object.getString("fromName"));

//
        }
    };



    public static BeanSerializer serializer = new InvitationSerializer();

    public static class InvitationSerializer	extends BeanSerializer  {

        @Override
        public JSONObject toJSON(Object bean) {
            Invitation accountBean = (Invitation) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (accountBean.getLocalId() != null) returnObject.put("localId", accountBean.getLocalId());
                if (accountBean.getAccountType() != null) returnObject.put("accountType", accountBean.getAccountType());
                if (accountBean.getLastModificationDate() != null) returnObject.put("lastModificationDate", accountBean.getLastModificationDate());
                if (accountBean.getInvitationId() != null) returnObject.put("invitationId", accountBean.getInvitationId());
                if (accountBean.getFromName() != null) returnObject.put("fromName", accountBean.getFromName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
