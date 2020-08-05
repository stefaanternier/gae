package org.celstec.arlearn2.tasks.clone;

import com.google.api.gax.paging.Page;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.cloud.storage.*;


public class CloneMediaLibrary implements DeferredTask {

    CloneManifest cloneManifest;

    public CloneMediaLibrary(CloneManifest cloneManifest) {
        this.cloneManifest = cloneManifest;
    }

//    public CloneMediaLibrary(long gameIdFrom, long gameIdTo) {
//        cloneManifest = new CloneManifest(gameIdFrom);
//        cloneManifest.newGameId = gameIdTo;
//    }

    @Override
    public void run() {
        try {
            String projectId = "serious-gaming-platform-dev";
            String bucketName = "serious-gaming-platform-dev.appspot.com";
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            Bucket bucket = storage.get(bucketName);

            iterateFilesInFolder(bucket, "game/"+cloneManifest.oldGameId+"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iterateFilesInFolder(Bucket bucket, String folder) {
        System.out.println("going into folder "+folder);
        Page<Blob> blobs =
                bucket.list(
                        Storage.BlobListOption.prefix(folder));//, //generalItems/6601759870943232/
                        //Storage.BlobListOption.currentDirectory());
        System.out.println(Storage.BlobListOption.currentDirectory());

        for (Blob blob : blobs.iterateAll()) {

            if (blob.isDirectory()) {

                iterateFilesInFolder(bucket, blob.getName());

            } else {
//                System.out.println("to : " +blob.getName().replace(this.cloneManifest.oldGameId+"", cloneManifest.newGameId+""));
                blob.copyTo(bucket.getName(), blob.getName().replace(this.cloneManifest.oldGameId+"", cloneManifest.newGameId+""));
            }
        }
    }
}
