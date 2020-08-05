package org.celstec.arlearn2.tasks.clone;

import java.io.Serializable;
import java.util.HashMap;

public class CloneManifest implements Serializable {

    Long oldGameId;
    Long newGameId;

    HashMap<Long, Long> itemIdentifierMappings = new HashMap<>();

    public CloneManifest(Long oldGameId) {
        this.oldGameId = oldGameId;
    }

    @Override
    public String toString() {
        return "CloneManifest{" +
                "oldGameId=" + oldGameId +
                ", newGameId=" + newGameId +
                ", itemIdentifierMappings=" + itemIdentifierMappings +
                '}';
    }
}
