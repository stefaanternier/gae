package org.celstec.arlearn2.endpoints.util;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.server.spi.auth.common.User;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ****************************************************************************
 * Copyright (C) 2019 Open Universiteit Nederland
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
public class EnhancedUser extends User{


    public static final String FACEBOOK_COM = "facebook.com";
    public static final String GOOGLE_COM = "google.com";
    public static final String TWITTER_COM= "twitter.com";
    public static final String ANONYMOUS= "anonymous";

    public static final String PASSWORD= "password";
    public static final String IDENTITIES = "identities";

    public String localId;
    public String name;
    public String picture;
    public int provider=0;
    public boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public EnhancedUser(String email) {
        super(email);
    }

    public EnhancedUser(String id, String email) {
        super(id, email);
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public User parseAndSetIdToken(String idTokenString){
        IdToken idToken = null;
        try {
            idToken = IdToken.parse(new GsonFactory(), idTokenString);
//            System.out.println("idtoken "+idToken.toString());
            if (idToken.getPayload() != null){
                if (idToken.getPayload().get("name") != null){
                    name = idToken.getPayload().get("name").toString();
                }
                if (idToken.getPayload().get("picture") != null){
                    picture = idToken.getPayload().get("picture").toString();
                }
            }
            if (idToken.getPayload().get("admin")!= null) {
                setAdmin((Boolean)idToken.getPayload().get("admin"));
            } else {
                setAdmin(false);
            }
            ArrayMap map = (ArrayMap) idToken.getPayload().get("firebase");
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry next = (Map.Entry) iterator.next();
//                System.out.println("key is "+next.getKey());
                if (next.getValue().equals(FACEBOOK_COM)){
                    provider =1;
                }
                if (next.getValue().equals(GOOGLE_COM)){
                    provider =2;
                }
                if (next.getValue().equals(TWITTER_COM)){
                    provider =4;
                }
                if (next.getValue().equals(PASSWORD)){
                    provider =7;
                    localId = idToken.getPayload().get("user_id").toString();
                }
                if (next.getValue().equals(ANONYMOUS)){
                    provider =8;
                    localId = idToken.getPayload().get("user_id").toString();
                }
                if (next.getKey().equals(IDENTITIES) && (provider !=7 & provider !=8 )) {
                    localId = parseAndGetIdentifier((Map) next.getValue());
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String createFullId() {
        return getProvider()+":" + getLocalId();
    }

    public String parseAndGetIdentifier(Map map) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry next = (Map.Entry) iterator.next();
            if (next.getKey().equals(FACEBOOK_COM)) {
                return (String) (((List)next.getValue()).get(0));
            }
            if (next.getKey().equals(GOOGLE_COM)) {
                return (String) (((List)next.getValue()).get(0));
            }
            if (next.getKey().equals(TWITTER_COM)) {
                return (String) (((List)next.getValue()).get(0));
            }

            if (next.getKey().equals(ANONYMOUS)) {
                return (String) (((List)next.getValue()).get(0));
            }


        }
        return "no_id";
    }
}
