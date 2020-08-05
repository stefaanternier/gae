package org.celstec.arlearn2.endpoints.impl.account;

import com.google.appengine.api.search.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import java.util.ArrayList;
import java.util.logging.Level;

public class AccountSearchIndex extends GenericBean {

    String fullId;
    String displayName;
    String labels;
    String email;
    boolean delete = false;

    public AccountSearchIndex() {
        super();
    }

    public AccountSearchIndex(String fullId, String displayName, String labels, String email) {
        super();
        this.displayName = displayName;
        this.fullId = fullId;
        this.labels = labels;
        this.email = email;
        this.delete = false;
    }

    public AccountSearchIndex(String fullId, String displayName, String labels, boolean delete) {
        super();
        this.displayName = displayName;
        this.fullId = fullId;
        this.labels = labels;
        this.delete = delete;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean getDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public void run() {
        try {
            if (delete) {
                removeFromIndex();
            } else {
                addToIndex();
            }



        } catch (PutException e) {
            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
                // retry storing the document
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scheduleTask() {
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions to  = TaskOptions.Builder.withUrl("/asyncTask")
                .param("type", this.getClass().getName());
        queue.add(setParameters(to));
    }

    private void removeFromIndex() {
        ArrayList<String> docIds = new ArrayList<String>();
        docIds.add(fullId);
        getIndex().delete(docIds);
    }

    private void addToIndex() throws PutException {
        Document.Builder builder = Document.newBuilder()
                .setId(fullId)
                .addField(Field.newBuilder().setName("displayName").setText(""+getDisplayName()))
                .addField(Field.newBuilder().setName("email").setText(""+getEmail()))
                .addField(Field.newBuilder().setName("labels").setText(getLabels()));

        Document doc = builder.build();
        getIndex().put(doc);
    }

    public Index getIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("account_index").build();
        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
    }
}
