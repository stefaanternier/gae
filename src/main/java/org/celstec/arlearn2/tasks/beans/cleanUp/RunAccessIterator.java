package org.celstec.arlearn2.tasks.beans.cleanUp;

import com.google.appengine.api.datastore.Cursor;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunAccessEntity;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import javax.jdo.PersistenceManager;
import java.util.List;

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
public class RunAccessIterator extends GenericBean {

    String cursorString;

    public RunAccessIterator() {
        super();
    }

    public RunAccessIterator(String cursorString) {
        super();
        this.cursorString = cursorString;
    }

    public String getCursorString() {
        return cursorString;
    }

    public void setCursorString(String cursorString) {
        this.cursorString = cursorString;
    }

    @Override
    public void run() {
        if ("*".equals(cursorString)) {
            startIteration(null);
        } else {
            startIteration(cursorString);
        }
    }

    private void startIteration(String cursorString) {
//        PersistenceManager pm = PMF.get().getPersistenceManager();
//        try {
//            List<RunAccessJDO> runAccessJDOList = RunAccessManager.listAll(pm, cursorString);
//            processRunAccess(pm, runAccessJDOList);
//            if (!runAccessJDOList.isEmpty()) rescheduleIteration(runAccessJDOList);
//        } finally {
//            pm.close();
//        }

    }

    private void processRunAccess(PersistenceManager pm, List<RunAccessEntity> runAccessJDOList) {
//        for (RunAccessJDO userJDO : runAccessJDOList) {
//            processRunAccess(pm, userJDO);
//        }
    }

    private void processRunAccess(PersistenceManager pm, RunAccessEntity runAccessJDO) {

//        if (runAccessJDO.getGameId() == null) {
//            System.out.println("dealing with runaccess  "+runAccessJDO.getRunId() +" "+runAccessJDO.getGameId());
//            try {
//                RunDelegator rd = new RunDelegator("test");
//                Run r = rd.getRun(runAccessJDO.getRunId());
//                if (r!=null){
//                    if (r.getGameId() != null) {
//                        System.out.println(r.getGameId());
//                        runAccessJDO.setGameId(r.getGameId());
////                        RunAccessManager.update(pm, runAccessJDO);
//                    }
//                }
//
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }


    }


        private void rescheduleIteration(List users) {
//        Cursor cCursor = JDOCursorHelper.getCursor(users);
//        String cursorString = cCursor.toWebSafeString();
//        System.out.println("cursorout " + cursorString);
//        (new RunAccessIterator(cursorString)).scheduleTask();
    }
}
