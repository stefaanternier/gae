package org.celstec.arlearn2.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.auth.common.User;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;

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
@Api(name = "action")
public class ActionApi extends GenericApi {

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "actions",
            path = "/actions/run/{runId}/from/{from}/{cursor}"
    )
    public ActionList getActionList(EnhancedUser user,
                                   @Named("runId") Long runIdentifier,
                                   @Named("from") Long from,
                                   @Named("cursor") String cursor
    ) {
        if (cursor == null || cursor.length() < 3) {
            cursor = null;
        }
        System.out.println("user email "+user.createFullId());
        return new ActionDelegator().getActionsFromUntil(runIdentifier, user.createFullId(), from, null, cursor);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "create_action",
            path = "/action/create"
    )
    public Action createAction(final User user, Action action) {
        EnhancedUser us = (EnhancedUser) user;
        action.setUserId(us.createFullId());
        if (action.getTimestamp() == null) action.setTimestamp(System.currentTimeMillis());
        return new ActionDelegator().createAction(action, us.createFullId());
    }
}
