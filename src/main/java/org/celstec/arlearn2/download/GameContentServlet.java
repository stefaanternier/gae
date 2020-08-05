package org.celstec.arlearn2.download;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.blobstore.BlobInfoFactory;

import com.google.appengine.tools.cloudstorage.*;
import org.celstec.arlearn2.jdo.classes.FilePathEntity;
import org.celstec.arlearn2.jdo.manager.FilePathManager;
import org.celstec.arlearn2.upload.BlobStoreServlet;
import org.celstec.arlearn2.upload.BlobStoreServletWithExternalUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by str on 27/05/14.
 */
public class GameContentServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BlobStoreServlet.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    private final String bucketName = "arlearn-eu-rundata";
    private BlobInfoFactory infoFactory = new BlobInfoFactory();
    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(
                    new RetryParams.Builder()
                            .initialRetryDelayMillis(10)
                            .retryMaxAttempts(10)
                            .totalRetryPeriodMillis(15000)
                            .build());
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        String gameIdString = getFirstPath(path);
        Long gameId = Long.parseLong(gameIdString);
        path = "/"+getReminder(path);



        GcsFilename fn = new GcsFilename(bucketName, "game/"+gameId+path);
        GcsFileMetadata fileMetadata = gcsService.getMetadata(fn);

        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(new GcsFilename(bucketName, "game/"+gameId+path), 0, BUFFER_SIZE);
        resp.setContentType(fileMetadata.getOptions().getMimeType());
        copy(Channels.newInputStream(readChannel), resp.getOutputStream());
//        List<FilePathEntity> filePathEntities = FilePathManager.getFilePathEntityByGameId(gameId, path);
//        BlobKey bk = null;
//        if (!filePathEntities.isEmpty()) {
//            bk = filePathEntities.get(0).getBlobKey();
//        }
//
//        if (bk != null) {
//
//            if (req.getParameter("thumbnail") == null) {
//                BlobInfoFactory bi = new BlobInfoFactory();
//                resp.setContentType(bi.loadBlobInfo(bk).getContentType());
//
//                blobstoreService.serve(bk, resp);
//            }  else {
//                ImagesService imagesService = ImagesServiceFactory.getImagesService();
//                ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(bk);
//                options.imageSize(Integer.parseInt(req.getParameter("thumbnail")));
//                boolean crop = false;
//                if (req.getParameter("crop")!=null) {
//                    crop = Boolean.parseBoolean(req.getParameter("crop"));
//                }
//                options.crop(req.getParameter("crop")!=null);
//                String thumbnailUrl =imagesService.getServingUrl(options);
//
//                resp.sendRedirect(thumbnailUrl);
//            }
//
//        } else {
//            if (req.getPathInfo().endsWith("gameThumbnail")) {
//                resp.setHeader("Cache-Control", "max-age=2592000");
//                resp.sendRedirect("/images/default_game_icon.png");
//            } else {
//                resp.setStatus(404);
//            }
//
//        }
    }

    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
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
}
