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
public class UserLoggedInEntity {
    public static String KIND = "UsersLoggedIn";

    public static String COL_AUTHTOKEN = "authToken";
    public static String COL_USERNAME = "username";


    private Key key;
    private String username;
    private String authToken;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setKey(int hash) {
        Key key = KeyFactory.createKey(KIND,hash);
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserLoggedInEntity(Entity entity){
        this.key = entity.getKey();
        this.authToken = (String) entity.getProperty(COL_AUTHTOKEN);
        this.username = (String) entity.getProperty(COL_USERNAME);

    }

    public UserLoggedInEntity(){

    }
    public Entity toEntity() {
        Entity result = new Entity(KIND, this.key.getId());
        result.setProperty(COL_AUTHTOKEN, this.authToken);
        result.setProperty(COL_USERNAME, this.username);
        return result;

    }
}
