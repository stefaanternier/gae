package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.GameAccessEntity;
import org.celstec.arlearn2.jdo.manager.GameAccessManager;

import java.util.Iterator;
import java.util.StringTokenizer;


public class GameAccessDelegator extends GoogleDelegator {
    public GameAccessDelegator() {
        super();
    }

    public GameAccessDelegator(EnhancedUser user) {
        super(user);
    }

    public GameAccessDelegator(String authtoken) {
        super(authtoken);
    }
    public GameAccessDelegator(com.google.appengine.api.users.User user) {
        super(user);
    }
    public GameAccessDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public void provideAccess(Long gameId, Account account, int accessRights) {
//		StringTokenizer st = new StringTokenizer(account, ":");
//		int accountType = 0;
//		String localID = null;
//		if (st.hasMoreTokens()) {
//			accountType = Integer.parseInt(st.nextToken());
//		}
//		if (st.hasMoreTokens()) {
//			localID = st.nextToken();
//		}
        GameAccessManager.addGameAccess(account.getLocalId(), account.getAccountType(), gameId, accessRights);
    }

    public GameAccess provideAccess(Long gameId, String accountString, int accessRights) {
        StringTokenizer st = new StringTokenizer(accountString, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        return GameAccessManager.addGameAccess(localID, accountType, gameId, accessRights);
//        Game game = new Game();
//        game.setGameId(gameId);
//        new NotificationDelegator(this).broadcast(game, accountString);

    }

    public void provideAccessWithCheck(Long gameIdentifier, Account account, Integer accessRight) {
        provideAccess(gameIdentifier, account, accessRight);

    }

    public GameAccess provideAccessWithCheck(Long gameIdentifier, String account, Integer accessRight) {
        return provideAccess(gameIdentifier, account, accessRight);


    }

    public void removeAccessWithCheck(Long gameIdentifier, String fullId) {
        StringTokenizer st = new StringTokenizer(fullId, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        GameAccessManager.removeGameAccess(localID, accountType, gameIdentifier);
//        Game game = new Game();
//        game.setGameId(gameIdentifier);
//        game.setDeleted(true);
//        new NotificationDelegator(this).broadcast(game, account);
    }

    public GameAccessList getGamesAccess(String account, Long from, Long until) {
        StringTokenizer st = new StringTokenizer(account, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        Iterator<GameAccess> it = GameAccessManager.getGameList(accountType, localID, from, until).iterator();
        GameAccessList rl = new GameAccessList();
        while (it.hasNext()) {
            GameAccess ga = (GameAccess) it.next();
            rl.addGameAccess(ga);
        }
        rl.setServerTime(System.currentTimeMillis());
        return rl;
    }

    public GameAccessList getGamesAccess(String account, String resumptionToken, long from) {
        StringTokenizer st = new StringTokenizer(account, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        return GameAccessManager.getGameList(accountType, localID, resumptionToken,from);
    }

    public GameAccessList getGamesAccess(Long from, Long until) {
        GameAccessList gl = new GameAccessList();
        String myAccount = null;
        if (account != null) {
            myAccount = account.getFullId();
        } else
            myAccount = UserLoggedInManager.getUser(authToken);
        if (myAccount == null) {
            gl.setError("login to retrieve your list of games");
            return gl;
        }
        return getGamesAccess(myAccount, from, until);
    }

    public GameAccessList getGamesAccess(String resumptionToken, long from, int provider, String localId) {
//        GameAccessList gl = new GameAccessList();
//        String myAccount = null;
//        System.out.println("fullId "+ account.getFullId() + " "+authToken);
//        if (account != null) {
//            myAccount = account.getFullId();
//        } else
////            myAccount = UserLoggedInManager.getUser(authToken);
//        if (myAccount == null) {
//            gl.setError("login to retrieve your list of games");
//            return gl;
//        }
        return GameAccessManager.getGameList(provider, localId, resumptionToken,from);
//        return getGamesAccess(myAccount, resumptionToken, from);
    }

    public GameAccessList getAccessList(Long gameIdentifier) {
        GameAccessList returnList = new GameAccessList();
        returnList.setGameAccess(GameAccessManager.getGameList(gameIdentifier));
        returnList.myFullId = this.account.getFullId();
        return returnList;

    }

    public boolean isOwner(String myAccount, Long gameId) {
        try {
            return GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights() == GameAccessEntity.OWNER;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean canEdit(String myAccount, Long gameId) {
        try {
            int accessRights = GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights();
            return accessRights == GameAccessEntity.OWNER || accessRights == GameAccessEntity.CAN_EDIT;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean canView( Long gameId) {
        if (this.account == null) return false;
        String myAccount = this.account.getFullId();
        try {
            int accessRights = GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights();
            return accessRights == GameAccessEntity.OWNER || accessRights == GameAccessEntity.CAN_EDIT || accessRights == GameAccessEntity.CAN_VIEW;
        } catch (Exception e) {
            return false;
        }
//return true;
    }
    public boolean canView( Long gameId, String myAccount) {
        try {
            int accessRights = GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights();
            return accessRights == GameAccessEntity.OWNER || accessRights == GameAccessEntity.CAN_EDIT || accessRights == GameAccessEntity.CAN_VIEW;
        } catch (Exception e) {
            return false;
        }

    }

    public void broadcastGameUpdate(Game game) { //todo optimize via cache
        for (GameAccess ga : GameAccessManager.getGameList(game.getGameId())) {
            new NotificationDelegator(this).broadcast(game, ga.getAccount());
        }
    }

}
