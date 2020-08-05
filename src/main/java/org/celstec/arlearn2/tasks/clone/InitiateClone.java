package org.celstec.arlearn2.tasks.clone;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;

import java.util.List;

public class InitiateClone implements DeferredTask {

    Long gameId;
    EnhancedUser user;

    CloneManifest cloneManifest;

    public InitiateClone(Long gameId, EnhancedUser user) {
        this.gameId = gameId;
        this.user = user;
        this.cloneManifest = new CloneManifest(gameId);
    }

    @Override
    public void run() {
        try {
            System.out.println("starting clone process");
            cloneGame();
            cloneItems();
            Queue queue = QueueFactory.getDefaultQueue();
            queue.add(
                    TaskOptions.Builder
                            .withPayload(new UpdateGeneralItemIds(cloneManifest)
                            ));
            queue.add(
                    TaskOptions.Builder
                            .withPayload(new CloneMediaLibrary(cloneManifest)
                            ));
        } catch (Exception e) {
            System.out.println("exception cannot lead to restart of system");
            e.printStackTrace();
        }
    }

    private void cloneItems() {

        GeneralItemDelegator gid = new GeneralItemDelegator();
        GeneralItemList generalitemList = gid.getGeneralItems(gameId);
        List<GeneralItem> items = generalitemList.getGeneralItems();
        for (int i = 0; i < items.size(); i++) {
            GeneralItem old = items.get(i);
            Long oldId = old.getId();
            old.setGameId(cloneManifest.newGameId);
            old.setId(null);
            GeneralItem newItem = gid.createGeneralItem(old);
            cloneManifest.itemIdentifierMappings.put(oldId, newItem.getId());

        }

        System.out.println("cloneManifest" + cloneManifest);

    }

    private void cloneGame() {
        GameDelegator qg = new GameDelegator(user);
        Game g = qg.getGame(gameId);
        g.setTitle(g.getTitle() + " (copy) ");
        if (g == null) {
            System.out.println("[clone] game not found");
            return;
        }
        System.out.println("set game id to null");
        g.setGameId(null);
        Game newGame = qg.createGame(g, GameModification.CREATED);
        cloneManifest.newGameId = newGame.getGameId();
        System.out.println("cloneManifest" + cloneManifest);
    }
}
