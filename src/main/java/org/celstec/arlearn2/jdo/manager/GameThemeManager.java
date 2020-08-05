package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameTheme;
import org.celstec.arlearn2.jdo.classes.GameEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameThemeManager {
    public static String KIND = "GameThemeJDO";

    private static DatastoreService datastore;

    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public static GameTheme getGameTheme(Long themeId) {
        Key key = KeyFactory.createKey(KIND, themeId);
        Entity result = null;
        try {
            result = datastore.get(key);
        } catch (EntityNotFoundException e) {
            System.out.println("error ");
            return null;
        }
        return GameTheme.from(result);
    }

    public static GameTheme create(GameTheme newTheme) {
        Entity result = null;
        if (newTheme.getThemeId() == null) {
            result = new Entity(KIND);
        } else {
            result = new Entity(KIND, newTheme.getThemeId());
        }

        result.setProperty("primaryColor", newTheme.getPrimaryColor());
        result.setProperty("secondaryColor", newTheme.getSecondaryColor());

        result.setProperty("global", newTheme.isGlobal());
        result.setProperty("fullAccount", newTheme.getFullAccount());
        result.setProperty("category", newTheme.getCategory());
        result.setProperty("name", newTheme.getName());

        result.setProperty("iconPath", newTheme.getIconPath());
        result.setProperty("backgroundPath", newTheme.getBackgroundPath());
        result.setProperty("correctPath", newTheme.getCorrectPath());
        result.setProperty("wrongPath", newTheme.getWrongPath());


        datastore.put(result);
        return GameTheme.from(result);

    }

    public static List<GameTheme> listGlobal() {
        ArrayList<GameTheme> globalThemeList = new ArrayList<GameTheme>();
        Query.FilterPredicate featuredFilter = new Query.FilterPredicate("global", Query.FilterOperator.EQUAL, true);

        Query q = new Query(KIND).setFilter(featuredFilter);
        PreparedQuery pq = datastore.prepare(q);
        List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(25));
        Iterator<Entity> it = results.iterator();
        while (it.hasNext()) {
            globalThemeList.add(GameTheme.from(it.next()));
        }
        return globalThemeList;
    }
}
