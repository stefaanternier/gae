package org.celstec.arlearn2.tasks;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.celstec.arlearn2.tasks.clone.CloneMediaLibrary;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            resp.getWriter().println("hallo wereldd");

            Queue queue = QueueFactory.getDefaultQueue();
//        queue.add(
//                TaskOptions.Builder
//                        .withPayload(new InitiateClone(g.getGameId(),user)
//                        ));

//            queue.add(
//                    TaskOptions.Builder
//                            .withPayload(new CloneMediaLibrary()
//                            ));

//            GenerateHourlyStatsForCustomer task = new GenerateHourlyStatsForCustomer(5639445604728832l, StrigooDevice.class);
//            IterateCustomersHourly task = new IterateCustomersHourly("Europe/Brussels", null);
//            task.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
