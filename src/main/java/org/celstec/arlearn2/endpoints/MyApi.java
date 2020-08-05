package org.celstec.arlearn2.endpoints;

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

import com.google.api.server.spi.auth.EspAuthenticator;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.*;

import com.google.api.server.spi.response.UnauthorizedException;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.endpoints.util.Constants;


import com.google.api.server.spi.auth.common.User;
import org.celstec.arlearn2.endpoints.util.LocalhostAuthenticator;


public class MyApi extends GenericApi{


    @ApiMethod(name = "echo")
    public Game echo(@Named("n") @Nullable Integer n) {
        return new Game();
    }
    // [END echo_method]

    @ApiMethod(
            path = "firebase_user",
            httpMethod = ApiMethod.HttpMethod.GET


    )
    public Game privateMethod(){
        return new Game();

    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "game",

            authenticators = {EspAuthenticator.class}

    )
    public Game getUserEmail(User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Invalid credentials");
        }
        ;
        Game ret = new Game();
        ret.setTitle(user.getEmail()+" "+user.getId()+" "+user.toString());
        ret.setOwner("owner "+Constants.API_NAME_SPACE);
        return ret;
    }
}

