package com.google.api.server.spi.auth;


import com.google.api.auth.UnauthenticatedException;
import com.google.api.auth.UserInfo;
import com.google.api.control.ConfigFilter;
import com.google.api.control.model.MethodRegistry.AuthInfo;
import com.google.api.control.model.MethodRegistry.Info;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.api.server.spi.config.Singleton;
import com.google.api.server.spi.response.UnauthorizedException;
import endpoints.repackaged.com.google.api.Service;
import endpoints.repackaged.com.google.common.annotations.VisibleForTesting;
import endpoints.repackaged.com.google.common.base.Optional;
import endpoints.repackaged.com.google.common.util.concurrent.UncheckedExecutionException;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

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
 * Contributors: Stefaan Ternie r
 * ****************************************************************************
 */
@Singleton
public class EnhancedEspAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(EspAuthenticator.class.getName());
    private final com.google.api.auth.Authenticator authenticator;

    public EnhancedEspAuthenticator() {
        this(com.google.api.auth.Authenticator.create());
    }

    @VisibleForTesting
    EnhancedEspAuthenticator(com.google.api.auth.Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public User authenticate(HttpServletRequest request) throws UnauthorizedException{
        //System.out.println(request.);
        Info methodInfo = ConfigFilter.getMethodInfo(request);
        if (methodInfo == null) {
            throw new IllegalStateException("method_info is not set in the request");
        } else {
            Optional<AuthInfo> authInfo = methodInfo.getAuthInfo();
            if (!authInfo.isPresent()) {
                logger.info("auth is not configured for this request");
                return null;
            } else {
                Service service = ConfigFilter.getService(request);
                if (service == null) {
                    throw new IllegalStateException("service is not set in the request");
                } else {
                    String serviceName = service.getName();

                    try {
                        UserInfo userInfo = this.authenticator.authenticate(request, (AuthInfo)authInfo.get(), serviceName);
                        return new EnhancedUser(userInfo.getId(), userInfo.getEmail())
                                .parseAndSetIdToken(request.getHeader("Authorization").substring(7));
                    } catch (UncheckedExecutionException | UnauthenticatedException var7) {
                        logger.warning(String.format("Authentication failed: %s", var7));
                        throw new UnauthorizedException("Invalid credentials");
//                        return null;
                    }
                }
            }
        }
    }
}
