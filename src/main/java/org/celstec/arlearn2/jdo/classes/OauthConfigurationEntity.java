package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
public class OauthConfigurationEntity {

    public static String KIND = "OauthConfigurationJDO";

    public OauthConfigurationEntity(Entity entity){
        this.oauthProviderId = entity.getKey();
        this.client_id = (String) entity.getProperty("client_id");
        this.redirect_uri = (String) entity.getProperty("redirect_uri");
        this.client_secret = (String) entity.getProperty("client_secret");
    }

    public OauthConfigurationEntity(){

    }

    private Key oauthProviderId;
    private String client_id;
    private String redirect_uri;
    private String client_secret;

    public int getOauthProviderId() {
        return (int) oauthProviderId.getId();
    }

    public void setOauthProviderId(int oauthProviderId) {
        this.oauthProviderId = KeyFactory.createKey(KIND, oauthProviderId);
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }


    public Entity toEntity() {
        Entity result = new Entity(KIND, this.oauthProviderId.getId());
        result.setProperty("client_id", this.client_id);
        result.setProperty("redirect_uri", this.redirect_uri);
        result.setProperty("client_secret", this.client_secret);
        return result;
    }
}
