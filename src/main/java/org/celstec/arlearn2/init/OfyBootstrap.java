package org.celstec.arlearn2.init;
//import com.googlecode.objectify.ObjectifyService;
//import org.celstec.arlearn2.beans.database.FeaturedGameTestJDO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
public class OfyBootstrap implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
//        ObjectifyService.init();
//        ObjectifyService.register(FeaturedGameTestJDO.class);


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}