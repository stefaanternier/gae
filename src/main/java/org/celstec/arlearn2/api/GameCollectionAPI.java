package org.celstec.arlearn2.api;

import org.celstec.arlearn2.delegators.GameCollectionDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
//@Path("/gameCollection")
public class GameCollectionAPI extends Service {

//    @GET
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    @CacheControlHeader("no-cache")
//    @Path("/{collectionId}")
//    public String getGames(@HeaderParam("Authorization") String token,
//                           @DefaultValue("application/json") @HeaderParam("Accept") String accept,
//                           @PathParam("collectionId") Long gameCollectionId
//    ) {
////        if (!validCredentials(token))
////            return serialise(getInvalidCredentialsBean(), accept);
//        GameCollectionDelegator gameCollectionDelegator = new GameCollectionDelegator(account, token);
//        return serialise(gameCollectionDelegator.getGameCollection(gameCollectionId), accept);
//    }
}
