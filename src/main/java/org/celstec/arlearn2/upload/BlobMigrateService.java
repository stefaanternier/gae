package org.celstec.arlearn2.upload;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.tools.cloudstorage.*;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class BlobMigrateService extends HttpServlet {
    private final String bucketName = "arlearn-eu-rundata";
    private BlobInfoFactory infoFactory = new BlobInfoFactory();
    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(
                    new RetryParams.Builder()
                            .initialRetryDelayMillis(10)
                            .retryMaxAttempts(10)
                            .totalRetryPeriodMillis(15000)
                            .build());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        path = getReminder(path);

        String urlString = "https://streetlearn.appspot.com/"+path;
        System.out.println("path=="+path+" "+urlString);
        URL url = new URL(urlString);
        String contentType = "application/octet-stream";
        URLConnection urlConnection = url.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();

            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {

                if (headerName!= null && headerName.equalsIgnoreCase("Content-Type")){
                    contentType = value;
                }
            }
        }
        InputStream inputStream = urlConnection.getInputStream();

        BufferedInputStream bis = (new BufferedInputStream(inputStream));


        GcsFileOptions options = new GcsFileOptions.Builder()
                .mimeType(contentType)
                .acl("public-read")
                .build();

        GcsOutputChannel writeChannel = gcsService.createOrReplace(new GcsFilename(bucketName, path), options);
        try {
            byte[] contents = new byte[1024];
            int length;
            while ( (length = bis.read(contents)) != -1) {
                writeChannel.write(ByteBuffer.wrap(contents, 0, length));
            }
        } finally {
            writeChannel.close();
            bis.close();
        }
    }

    private String getFirstPath(String path) {
        if (path == null)
            return null;
        if (path.startsWith("/"))
            return getFirstPath(path.substring(1));
        if (path.contains("/"))
            return path.substring(0, path.indexOf("/"));
        return path;
    }

    private String getReminder(String path) {
        if (path == null)
            return null;
        if (path.startsWith("/"))
            return getReminder(path.substring(1));
        if (path.contains("/"))
            return path.substring(path.indexOf("/") + 1);
        return null;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {}
}
