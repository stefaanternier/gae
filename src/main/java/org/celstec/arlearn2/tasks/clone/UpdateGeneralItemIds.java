package org.celstec.arlearn2.tasks.clone;

import com.google.appengine.api.taskqueue.DeferredTask;
import org.celstec.arlearn2.beans.dependencies.*;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateGeneralItemIds  implements DeferredTask {

    CloneManifest cloneManifest;
    GeneralItemDelegator gid;// = new GeneralItemDelegator();

    public UpdateGeneralItemIds(CloneManifest cloneManifest) {
        this.cloneManifest = cloneManifest;
    }

    @Override
    public void run() {
        gid = new GeneralItemDelegator();
        System.out.println("in update UpdateGeneralItemIds "+ cloneManifest);

        Set<Map.Entry<Long, Long>> entries = cloneManifest.itemIdentifierMappings.entrySet();
        for (Iterator<Map.Entry<Long, Long>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Map.Entry<Long, Long> next = iterator.next();
            Long oldId = next.getKey();
            Long newId = next.getValue();
            GeneralItem item = gid.getGeneralItem(newId);
            Dependency dep = item.getDependsOn();
            dep =  updateEntry(dep);
            item.setDependsOn(dep);
            gid.createGeneralItem(item);
        }
    }

    public Dependency updateEntry(Dependency dep) {


        System.out.println(dep);
        if (dep != null) {
            if (dep instanceof ActionDependency) {
                return updateDependencyAction((ActionDependency) dep);
            }
            if (dep instanceof AndDependency) {
                return updateAndDependency((AndDependency) dep);
            }
            if (dep instanceof OrDependency) {
                return updateOrDependency((OrDependency) dep);
            }
            if (dep instanceof ProximityDependency) {
                return dep;
            }
            if (dep instanceof TimeDependency) {
                ((TimeDependency) dep).setOffset(updateEntry(((TimeDependency)dep).getOffset()));
                return dep;
            }
        }
        return null;


    }

    private Dependency updateAndDependency(AndDependency dependsOn) {
        AndDependency newAndDependency = new AndDependency();
        List<Dependency> list = dependsOn.getDependencies();
        for (int i = 0; i < list.size(); i++) {
            Dependency dependency = list.get(i);
            newAndDependency.addDependecy(updateEntry(dependency));
        }
        return newAndDependency;

    }

    private Dependency updateOrDependency(OrDependency dependsOn) {
        OrDependency newAndDependency = new OrDependency();
        List<Dependency> list = dependsOn.getDependencies();
        for (int i = 0; i < list.size(); i++) {
            Dependency dependency = list.get(i);
            newAndDependency.addDependecy(updateEntry(dependency));
        }
        return newAndDependency;

    }


    public Dependency updateDependencyAction(ActionDependency dep) {
        dep.setGeneralItemId(cloneManifest.itemIdentifierMappings.get(dep.getGeneralItemId()));
        return dep;
    }

}
