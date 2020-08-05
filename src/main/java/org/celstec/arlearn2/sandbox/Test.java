package org.celstec.arlearn2.sandbox;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.server.spi.auth.GoogleJwtAuthenticator;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
public class Test {

    public static void main(String[] args) {
        System.out.println("test");
    }

    public static void main23(String[] args) {
        String payload = "3a25bf9d45b30be443030000";
        payload = "3a55ff9d451e120244040000";
        BigInteger mask = new BigInteger("7");
        System.out.println(payload.substring(0, 2) + " " + payload.length());
//            int lat =new BigInteger(payload.substring(2,10), 32).intValue();
//            float myFloat = Float.intBitsToFloat(lat);
//
//            System.out.println("framenr is "+payload.substring(2,10)+" - "+lat+" - "+myFloat);

//            int lat =new BigInteger(payload.substring(10,16), 16).and(mask).intValue();
//            System.out.println("framenr is "+framenr);


//            int sat =new BigInteger(payload.substring(18,20), 16).and(mask).intValue();
//            System.out.println("sat is "+sat);
//            int hex = 0x55ff9d45;

//            float f = Float.intBitsToFloat(hex);
//            System.out.println(f);
//            System.out.printf("%f", f);

        ByteBuffer buf = ByteBuffer.wrap(toByteArray(payload.substring(2, 10)));
        float lat = buf.order(ByteOrder.LITTLE_ENDIAN).getFloat() / 100;
        buf = ByteBuffer.wrap(toByteArray(payload.substring(10, 18)));
        float lng = buf.order(ByteOrder.LITTLE_ENDIAN).getFloat() / 100;
        System.out.printf("lat %f  ", lat);
        System.out.printf("lng %f  ", lng);

        System.out.println("--");
        System.out.println("lat   " + (convertFromDegreesToDecimal(55.9166f) + 50));
        System.out.println("lat   " + (convertFromDegreesToDecimal(20.2831f) + 5));

        System.out.println("lng final " + getLat(payload) + "," + getLng(payload));
        //50.931943, 5.338052
    }

    private static double getLat(String payload) {
        ByteBuffer buf = ByteBuffer.wrap(toByteArray(payload.substring(2, 10)));
        float lng = buf.order(ByteOrder.LITTLE_ENDIAN).getFloat() / 100;
        float lngFloor = (float) Math.floor(lng);
        System.out.println(lngFloor);
        return ((lng - lngFloor) * 100 / 60d) + lngFloor;

    }


    private static double getLng(String payload) {
        ByteBuffer buf = ByteBuffer.wrap(toByteArray(payload.substring(10, 18)));
        float lng = buf.order(ByteOrder.LITTLE_ENDIAN).getFloat() / 100;
        float lngFloor = (float) Math.floor(lng);
        System.out.println(lngFloor);
        return ((lng - lngFloor) * 100 / 60d) + lngFloor;

    }

    private static double convertFromDegreesToDecimal(float value) {
        double result = value / 60d;
        return result;
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static final double lat2y(float aLat) {
        return ((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, 0)) * 256;
    }

    public static void mainq(String[] args) throws Exception {
        System.out.println("hallo");
        GoogleJwtAuthenticator gwt = new GoogleJwtAuthenticator();
        String tok = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjMxYTI2OGZjOTAyYmY5NjA5YzFmMzA5YmMyOTJmMmYxOGVhNjc3MzAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYXJsZWFybi1ldSIsIm5hbWUiOiJTdGVmYWFuIFRlcm5pZXIiLCJwaWN0dXJlIjoiaHR0cHM6Ly9ncmFwaC5mYWNlYm9vay5jb20vMTAwMDAwNzM0NTAxNDk0L3BpY3R1cmUiLCJhdWQiOiJhcmxlYXJuLWV1IiwiYXV0aF90aW1lIjoxNTQ3MDMwMzM0LCJ1c2VyX2lkIjoiZ0ZlYkxxRHJjcllaU1RuMUU0NVNveURmQVVtMiIsInN1YiI6ImdGZWJMcURyY3JZWlNUbjFFNDVTb3lEZkFVbTIiLCJpYXQiOjE1NDcwMzAzMzQsImV4cCI6MTU0NzAzMzkzNCwiZW1haWwiOiJzdGVmYWFuLnRlcm5pZXJAb3UubmwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZmFjZWJvb2suY29tIjpbIjEwMDAwMDczNDUwMTQ5NCJdLCJlbWFpbCI6WyJzdGVmYWFuLnRlcm5pZXJAb3UubmwiXX0sInNpZ25faW5fcHJvdmlkZXIiOiJmYWNlYm9vay5jb20ifX0.QpdBdhgTOI6XYdUTzkrSJycic07lkfgP4_bjZt-j3KVywhQlFMNqvgnhTKIWj9qKqf1MSKK7KFwiNHlGVUsN4AtbbqaNe-AkfgvlFNy7rGKjmcbkpOZfzSEuYiKtXIFbsm4_ofucfdQSd3zC260BfxbMbnYGplMk0NTxzrzYzoXOcr78eNaNrvQeAntpWaON5cJvzyT7SJAAJE66NMxcLmHb4hW2d7peZx0W4WKRLmCY8NzcrHwivY8ALTyjIgzhSb27f3tllNI6d4ldCTFwih-DetRZa3Dg6fxyUcj_XzGGfQu5cqpPP2ofFjrxoEedzgLOTWkym8jPPyGHiVyBKg";
        IdToken idToken = IdToken.parse(new GsonFactory(), tok);
//        IdToken idToken = IdToken.parse(new GsonFactory(), "eyJhbGciOiJSUzI1NiIsImtpZCI6IjMxYTI2OGZjOTAyYmY5NjA5YzFmMzA5YmMyOTJmMmYxOGVhNjc3MzAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYXJsZWFybi1ldSIsIm5hbWUiOiJTdGVmYWFuIFRlcm5pZXIiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1yUmI4bVNLTHJOWS9BQUFBQUFBQUFBSS9BQUFBQUFBQTVucy9MQmNGT1lrOVV4US9zOTYtYy9waG90by5qcGciLCJhdWQiOiJhcmxlYXJuLWV1IiwiYXV0aF90aW1lIjoxNTQ3MDIyOTkwLCJ1c2VyX2lkIjoiTkdEc3V1bW9aNGEzVTE0N21zNXZhOXN1clN4MiIsInN1YiI6Ik5HRHN1dW1vWjRhM1UxNDdtczV2YTlzdXJTeDIiLCJpYXQiOjE1NDcwMjI5OTAsImV4cCI6MTU0NzAyNjU5MCwiZW1haWwiOiJzdGVmYWFuLnRlcm5pZXJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMTY3NDM0NDkzNDk5MjA4NTAxNTAiXSwiZW1haWwiOlsic3RlZmFhbi50ZXJuaWVyQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.QBJ4mqXC0tuE9513E2EYC1ng9L1EyqZSTeCKNymtBib8qWZczd4u3gbTQe3Vwzr5zcpx0beDtmg_f_fWeU8aDECO5a8kpV5tPH-upZMYqjQWog6hHTG8967bRKLqU1NL4syh3Baj0mbNAhiMEvz2uTGEL8FU7fNIRi6I30I7R8rMB2X8VjUUmjjpZ_P11U9CpS93mC258-YJQMp8lPBOeMM_NiO2UVhrVXCBIimxsG3vmxr7UHXq4OIdQbpnysnoeVszwydnszOdOzHFvgpag1VCbevb6aTgCAZENjXuI6vW23eTR_TvGvRKkPeyX5MnBLQJSZYRd57yKJaTrpmfXA");
        System.out.println(idToken.getPayload().toPrettyString());
        ArrayMap map = (ArrayMap) idToken.getPayload().get("firebase");

        int provider = 0;
        String identifier = "";
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry next = (Map.Entry) iterator.next();
            System.out.println(next.getKey() + " " + next.getValue());
            if (next.getKey().equals("facebook.com")) {
                provider = 1;
            }
            if (next.getKey().equals("google.com")) {
                provider = 2;
            }
            if (next.getKey().equals("identities")) {
                identifier = getIdentifier((Map) next.getValue());
            }
        }
        System.out.println("provider " + provider);
        System.out.println("identifier " + identifier);
    }

    public static String getIdentifier(Map map) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry next = (Map.Entry) iterator.next();
            if (next.getKey().equals("facebook.com")) {
                return (String) (((List) next.getValue()).get(0));
            }
            if (next.getKey().equals("google.com")) {
                return (String) (((List) next.getValue()).get(0));
            }

        }
        return "test";
    }

//    private static boolean isValidIdToken(String idTokenString) throws IOException {
//
//            List<String> audiences = Collections.singletonList(clientId);
//            IdTokenVerifier verifier = new IdTokenVerifier.Builder()
//                    .setAudience(audiences)
//                    .setAcceptableTimeSkewSeconds(1000)
//                    .setIssuer(issuerId)
//                    .build();
//
//            IdToken idToken = IdToken.parse(new GsonFactory(), idTokenString);
//
//            return verifier.verify(idToken);
//    }
}
