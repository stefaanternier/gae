package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.beans.run.RunAccessList;
import org.celstec.arlearn2.jdo.classes.RunAccessEntity;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;

import java.util.Iterator;
import java.util.StringTokenizer;


public class RunAccessDelegator extends GoogleDelegator {

    public RunAccessDelegator(Service service) {
        super(service);
    }

    public RunAccessDelegator(String authtoken) {
        super(authtoken);
    }

    public RunAccessDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public void provideAccessWithCheck(Long runIdentifier, Long gameId, String account, Integer accessRight) {
        provideAccess(runIdentifier, gameId, account, accessRight);
    }

    public void provideAccess(Long runIdentifier, Long gameId, String account, int accessRights) {
        StringTokenizer st = new StringTokenizer(account, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        RunAccessManager.addRunAccess(localID, accountType, runIdentifier, gameId, accessRights);
    }

    public void provideAccess(Long runIdentifier, Long gameId, Account account, int accessRights) {
        RunAccessManager.addRunAccess(account.getLocalId(), account.getAccountType(), runIdentifier, gameId, accessRights);
    }

    public RunAccessList getRunsAccess(Long from, Long until) {
        RunAccessList gl = new RunAccessList();
        if (account != null) {
            return getRunsAccess(account.getFullId(), from, until);
        }
        gl.setError("login to retrieve your list of runs");
        return gl;
    }

    public RunAccessList getRunsAccess(Long gameId) {
        RunAccessList gl = new RunAccessList();
        if (account != null) {
            return getRunsAccess(account.getFullId(), gameId);
        }
        gl.setError("login to retrieve your list of runs");
        return gl;
    }

    public RunAccessList getRunsAccess(String account, Long from, Long until) {
        StringTokenizer st = new StringTokenizer(account, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        Iterator<RunAccess> it = RunAccessManager.getRunList(accountType, localID, from, until).iterator();
        RunAccessList rl = new RunAccessList();
        while (it.hasNext()) {
            RunAccess ga = (RunAccess) it.next();
            rl.addRunAccess(ga);
        }
        rl.setServerTime(System.currentTimeMillis());
        return rl;
    }

    public RunAccessList getRunsAccess(String account, Long gameId) {
        StringTokenizer st = new StringTokenizer(account, ":");
        int accountType = 0;
        String localID = null;
        if (st.hasMoreTokens()) {
            accountType = Integer.parseInt(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            localID = st.nextToken();
        }
        Iterator<RunAccess> it = RunAccessManager.getRunList(accountType, localID, gameId).iterator();
        RunAccessList rl = new RunAccessList();
        while (it.hasNext()) {
            RunAccess ga = (RunAccess) it.next();
            rl.addRunAccess(ga);
        }
        rl.setServerTime(System.currentTimeMillis());
        return rl;
    }


    public RunAccessList getRunAccess(Long runId) {
        RunAccessList ral = new RunAccessList();
        ral.setRunAccess(RunAccessManager.getRunAccessList(runId));
        return ral;
    }

    public boolean isOwner(Long runId) {
        try {
            return RunAccessManager.getAccessById(account.getFullId() + ":" + runId).getAccessRights() == RunAccessEntity.OWNER;
        } catch (Exception e) {
            return false;
        }

    }

    public void broadcastRunUpdate(Run run) {
        for (RunAccess ra : RunAccessManager.getRunAccessList(run.getRunId())) {
            new NotificationDelegator(this).broadcast(run, ra.getAccount());
        }


    }
}
