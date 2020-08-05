package org.celstec.arlearn2.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import org.celstec.arlearn2.beans.database.FeaturedGameTestJDO;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.jdo.manager.FeaturedGameManager;

//import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * ****************************************************************************
 * Copyright (C) 2019 Open Universiteit Nederland
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
@Api(name = "featuredGames")
public class FeaturedApi {

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "get_featured_games",
            path = "/games/featured/{lang}"
    )
    public GamesList getFeatured(EnhancedUser user, @Named("lang") String lang) {
//        return new GameDelegator().getParticipateGames(user.createFullId());
//        return ofy().load().type(FeaturedGameTestJDO.class).id(123l).now();
        return FeaturedGameManager.getFeaturedGames(lang);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "create_dummy_featured_games",
            path = "/games/featured/dummy"
    )
    public GamesList createDummyFeatured(EnhancedUser user) {
//        FeaturedGameTestJDO ob = new FeaturedGameTestJDO();
//        ob.setLang("en");
//        ob.setLastModificationDate(123l);
//        ob.setRank(2l);
//        ofy().save().entity(ob).now();

        //return new GameDelegator().getParticipateGames(user.createFullId());
        FeaturedGameManager.createFeaturedGame(5634472569470976l, '3', "nl");
        FeaturedGameManager.createFeaturedGame(5641906755207168l, '2', "nl");
//        FeaturedGameManager.createFeaturedGame(125l, '1', "nl");
//        FeaturedGameManager.createFeaturedGame(125l, '4', "nl");
        return null;
    }

}
