package org.celstec.arlearn2.beans;

import org.apache.commons.lang3.ArrayUtils;

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
public class GameIdentifierList {

    private String resumptionToken;
    private long[] gameIds;

    public GameIdentifierList(String resumptionToken, long[] gameIds) {
        this.resumptionToken = resumptionToken;
        this.gameIds = gameIds;
    }

    public GameIdentifierList(String resumptionToken, Long[] gameIds) {
        this.resumptionToken = resumptionToken;
        this.gameIds = ArrayUtils.toPrimitive(gameIds);
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

    public void setResumptionToken(String resumptionToken) {
        this.resumptionToken = resumptionToken;
    }

    public long[] getGameIds() {
        return gameIds;
    }

    public void setGameIds(long[] gameIds) {
        this.gameIds = gameIds;
    }
}
