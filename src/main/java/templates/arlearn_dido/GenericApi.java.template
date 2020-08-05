package org.celstec.arlearn2.endpoints;

import com.google.api.server.spi.auth.EndpointsAuthenticator;
import com.google.api.server.spi.auth.GoogleOAuth2Authenticator;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.ForbiddenException;
import org.celstec.arlearn2.endpoints.util.Constants;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.endpoints.util.LocalhostAuthenticator;
import com.google.api.server.spi.auth.EspAuthenticator;
import com.google.api.server.spi.auth.EnhancedEspAuthenticator;
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
@Api(  name = "myApi",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.FALSE,
        authenticators =  {EnhancedEspAuthenticator.class},
        issuerAudiences = {@ApiIssuerAudience(name = "firebase", audiences = {"moonlit-cistern-280812"})},
        issuers = {
                @ApiIssuer(
                        name = "firebase",
                        issuer = "https://securetoken.google.com/moonlit-cistern-280812",
                        jwksUri = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
        }

)
public class GenericApi {
 protected void adminCheck(User user) throws ForbiddenException {
                EnhancedUser us = (EnhancedUser) user;
                if (!us.isAdmin()) {
                        throw new ForbiddenException("only admin can call this method");
                }
        }
}
