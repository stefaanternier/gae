package org.celstec.arlearn2.sandbox;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class CopyGame {

    public static final String server = "https://streetlearn.appspot.com/";
//    public static final String server = "http://localhost:8888/";
//
    public static final String gameIdIn = "6127011881811968";
    public static final String gameIdOut = "5308175746072576";
    public static final String auth = "GoogleLogin auth=ya29.Ci9VA6MyrizakwhWDz84DO_RZhRFBqL4d4Ue5sDB_ZsUHhbX1VxXgO0sRk9odHvV7w";
//    public static final String auth = "GoogleLogin auth=ya29.Ci9dAz4V2eOxYy1k-JH5zF71WhIZD10CVbF6DGZ4C7CsIw-4xtk2nTos1qzM0h-Cag";


    public static final HashMap<String, String> itemIdMapping = new HashMap();

    public static void main(String[] args) throws Exception {
        JSONObject json = readJsonFromUrl(server+"rest/myGames/gameId/" + gameIdIn);
        System.out.println(json.toString());
        json = readJsonFromUrl(server+"rest/generalItems/gameId/" + gameIdIn);
        JSONArray array = json.getJSONArray("generalItems");
        for (int i = 0; i<array.length(); i++) { //i<array.length()
            JSONObject entry = array.getJSONObject(i);
            if (!entry.getBoolean("deleted")) {
                System.out.println(i + " " + entry.getString("name"));
                String id = entry.getString("id");
                entry.put("gameId", gameIdOut);
                entry.remove("id");
                System.out.println(entry.toString());
                JSONObject result = excutePost(server+"rest/generalItems", entry.toString());
                System.out.println(result);
                itemIdMapping.put(id, result.getString("id"));
            }
        }
        System.out.println(itemIdMapping);
        json = readJsonFromUrl(server+"rest/generalItems/gameId/" + gameIdIn);
         array = json.getJSONArray("generalItems");
        for (int i = 0;i<array.length(); i++) {
            JSONObject entry = array.getJSONObject(i);
            entry.put("gameId", gameIdOut);
            if (!entry.getBoolean("deleted")) {
                Iterator it = itemIdMapping.entrySet().iterator();
                String entryString = entry.toString();
                entryString = entryString.replaceAll(gameIdIn+"",gameIdOut+"");
                while (it.hasNext()) {
                    Map.Entry<String, String> pair = (Map.Entry) it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    entryString = entryString.replaceAll(pair.getKey(), pair.getValue());
                }
                System.out.println("entry in "+entryString);
                JSONObject newItem = excutePost(server+"rest/generalItems", entryString);
                System.out.println("result2"+ newItem.toString());

            }
        }

        json = readJsonFromUrl(server+"rest/myGames/gameContent/gameId/" + gameIdIn);
        System.out.println(json);
        JSONArray contentArray = json.getJSONArray("gameFiles");
        for (int i = 0;i<contentArray.length(); i++) {
            JSONObject entry = contentArray.getJSONObject(i);
            System.out.println(entry);
            String path = entry.getString("path");
            Long id = entry.getLong("id");
            System.out.println(path+" "+id);
            Iterator it = itemIdMapping.entrySet().iterator();
                String newPath = path;
                while (it.hasNext()) {
                    Map.Entry<String, String> pair = (Map.Entry) it.next();
                    newPath = newPath.replaceAll(pair.getKey(), pair.getValue());
                }
            excutePost(server+"rest/generalItems/clone/fromGameId/"+gameIdIn+"/toGameId/"+gameIdOut, path +"\n"+ newPath);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String urlString) throws Exception {
//        InputStream is = new URL(url).openStream();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization:", auth);
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            conn.disconnect();
        }
    }

    public static JSONObject excutePost(String targetURL, String urlParameters) throws Exception {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Authorization:", auth);

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}
