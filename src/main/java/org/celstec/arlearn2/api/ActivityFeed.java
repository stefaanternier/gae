package org.celstec.arlearn2.api;

import org.celstec.arlearn2.jdo.manager.XapiManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

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
@Path("/activityFeed")
public class ActivityFeed extends Service {
    private static final Logger logger = Logger.getLogger(Actions.class.getName());

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/get")
    public String getActions(@HeaderParam("Authorization") String token,

                             @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        return XapiManager.getActivityFeed(getAccount().getFullId(), null);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/game/{gameId}")
    public String getActionsGame(@HeaderParam("Authorization") String token,
                                 @PathParam("gameId") Long gameId,

                             @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        return XapiManager.getActivityFeedGame(gameId, null);
    }

}
