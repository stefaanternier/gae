package org.celstec.arlearn2.upload;


import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.tools.cloudstorage.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.jdo.classes.FilePathEntity;
import org.celstec.arlearn2.jdo.manager.FilePathManager;



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
public class GoogleCloudStorageServlet  extends HttpServlet {
//    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private final String bucketName = "arlearn-eu-rundata";
    private BlobInfoFactory infoFactory = new BlobInfoFactory();
    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(
                    new RetryParams.Builder()
                            .initialRetryDelayMillis(10)
                            .retryMaxAttempts(10)
                            .totalRetryPeriodMillis(15000)
                            .build());
//
//
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        BlobId blobId = BlobId.of(bucketName, "blob_name");
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
//
//        storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));
//
//        final byte[] bytes = "FooBar".getBytes();
//
////        InputStream content = new ByteArrayInputStream("Hello, World!".getBytes("UTF_8"));
////        Blob blob = bucket.create(blobName, content, "text/plain");
//        resp.setStatus(404);
//        resp.getWriter().write("hallo");
//    }
private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        Long gameId = Long.parseLong(req.getParameter("gameId"));
        String newPath = "/gcs" + path+"?gameId="+gameId;

        UploadOptions options = UploadOptions.Builder.withDefaults()
                .googleStorageBucketName(bucketName);
        String uploadUrl = blobstoreService.createUploadUrl(newPath,options);
        String page = "<body>";
        page += "Example invocation: uploadGameContent/filePath?gameId=&lt;gameId&gt; + <br>";
        page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
        page += "<input type=\"file\" name=\"myFile\">";
        page += "<input type=\"submit\" value=\"Submit\">";
        page += "</form></body>";
        res.getWriter().write(page);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Long gameId = Long.parseLong(req.getParameter("gameId"));
        java.util.Map<java.lang.String, java.util.List<BlobKey>> blobs = blobstoreService.getUploads(req);

        for (String key : blobs.keySet()) {
            deleteIfFileExists(gameId, req.getPathInfo());



            BlobInfo info = infoFactory.loadBlobInfo(blobs.get(key).get(0));
//            System.out.println("key " +key+" "+   blobs.get(key).get(0).getKeyString()+" "+req.getPathInfo());
//            System.out.println(info.getFilename());
//            System.out.println(info.getGsObjectName());

            String gsc = info.getGsObjectName();
            System.out.println(info.getGsObjectName());
            String buck = gsc.substring(1,gsc.indexOf('/', 1));
            gsc = gsc.substring(buck.length()+2);
            System.out.println(buck+ "    "+gsc);
            GcsFilename filename = new GcsFilename(buck, gsc);
            gcsService.copy(filename, new GcsFilename(bucketName, "game/"+gameId+req.getPathInfo()));


            FilePathManager.addFile(null, gameId, null, req.getPathInfo(), blobs.get(key).get(0));
        }
    }

    private void deleteIfFileExists(Long gameId,String path) {
//        BlobKey bk = FilePathManager.getBlobKey(null, null, gameId, path);
        List<FilePathEntity> filePathEntityByGameId = FilePathManager.getFilePathEntityByGameId(gameId, path);
        if (!filePathEntityByGameId.isEmpty()) {
            BlobKey bk = filePathEntityByGameId.get(0).getBlobKey();

            if (bk != null) {
                try {
                    blobstoreService.delete(bk);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FilePathManager.delete(bk);
            }
        }

    }

    public static void deleteIfFileExists(BlobstoreService blobstoreService, Long gameId,String path) {
        //BlobKey bk = FilePathManager.getBlobKey(null, null, gameId, path);

        BlobKey bk = FilePathManager.getFilePathEntityByGameId(gameId, path).get(0).getBlobKey();

        if (bk != null) {
            try {
                blobstoreService.delete(bk);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilePathManager.delete(bk);
        }

    }

}
