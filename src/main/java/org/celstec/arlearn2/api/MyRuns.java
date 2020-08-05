/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.api;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.utils.SystemProperty;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.OpenBadgeAssertion;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.*;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


@Path("/myRuns")
public class MyRuns extends Service {
	private static final Logger logger = Logger.getLogger(MyRuns.class.getName());

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getRuns(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.getRuns(), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/runAccess")
	public String getGameAccess(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		if (from == null) {
			from = 0l;
		}
		RunAccessDelegator qg = new RunAccessDelegator(this);
		return serialise(qg.getRunsAccess(from, until), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/runAccess/gameId/{gameId}")
	public String getGameAccess(@HeaderParam("Authorization") String token,
								@DefaultValue("application/json") @HeaderParam("Accept") String accept,
								@PathParam("gameId") Long gameId)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunAccessDelegator qg = new RunAccessDelegator(this);
		return serialise(qg.getRunsAccess(gameId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/participate")
	public String getParticipateRuns(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		if (from == null && until == null) {
			return serialise(rd.getParticipateRuns(), accept);	
		}
		return serialise(rd.getParticipateRuns(from, until), accept);
	}

	@GET
	@Produces({ MediaType.TEXT_HTML })
	@Path("/responses/{userId}/runId/{runId}")
	public String getResponse(
			@PathParam("userId") String userId,
			@PathParam("runId") Long runId
	){
		ResponseAsHtmlDelegator responseAsHtmlDelegator = new ResponseAsHtmlDelegator(userId, runId);
		responseAsHtmlDelegator.loadRun();
		responseAsHtmlDelegator.loadGeneralItems();
		responseAsHtmlDelegator.loadResponses();
		return responseAsHtmlDelegator.toString();
	}


	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/responses/sendMail/runId/{runId}")
	public String sendResponsesByMail(

			@HeaderParam("Authorization") String token,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("runId") Long runId
	){
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		UsersDelegator ud = new UsersDelegator(token);
		Account account = ud.getCurrentAccount();
		MailDelegator md = new MailDelegator(token);
		String url = "http://streetlearn.appspot.com/rest/myRuns/responses/"+account.getFullId()+"/runId/"+runId;

		ResponseAsHtmlDelegator responseAsHtmlDelegator = new ResponseAsHtmlDelegator(account.getFullId(), runId);
		responseAsHtmlDelegator.loadRun();
		responseAsHtmlDelegator.loadGeneralItems();
		responseAsHtmlDelegator.loadResponses();

		String results = "results are available at < a href="+url+">"+url+"</a>";
		String onleesbaar = "<SPAN>Is deze mail onleesbaar? "+
				"Open "+url+"</SPAN>";
		responseAsHtmlDelegator.setMailOnleesBaar(onleesbaar);
		md.sendMail("no-reply@" + SystemProperty.applicationId.get() + ".appspotmail.com", account.getName(), account.getEmail(), "Resultaten", responseAsHtmlDelegator.toString());
		return "{'result':'ok'}";
	}


	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/participate/gameId/{gameId}")
	public String getParticipateRunsWithGameId(
			@HeaderParam("Authorization") String token,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@QueryParam("from") Long from,
//			@QueryParam("until") Long until,
			@PathParam("gameId") Long gameId
	)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
//		if (from == null && until == null) {
			return serialise(rd.getParticipateRuns(gameId), accept);
//		}
//		return serialise(rd.getParticipateRuns(from, until), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/tagId/{tagId}")
	public String getRunsByTag(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("tagId") String tagId
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		
		
		return serialise(rd.getTaggedRuns(tagId), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/access/runId/{runIdentifier}/account/{account}/accessRight/{accessRight}")
	public String access(@HeaderParam("Authorization") String token, 
			@PathParam("runIdentifier") Long runIdentifier, 
			@PathParam("account") String account, 
			@PathParam("accessRight") Integer accessRight, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
				 RunAccessDelegator rd = new RunAccessDelegator(this);
				 RunDelegator runDelegator = new RunDelegator(rd);
				 rd.provideAccessWithCheck(runIdentifier, runDelegator.getRun(runIdentifier).getGameId(), account, accessRight);
				 return "{}";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getRun(@HeaderParam("Authorization") String token, 
			@PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		RunDelegator rd = new RunDelegator(this);
        Run r = rd.getRun(runIdentifier);
        if (r.getRunConfig()!= null &&r.getRunConfig().getSelfRegistration()!=null) {
            if (r.getRunConfig().getSelfRegistration()) {
                return serialise(r, accept);
            }
        }
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        return serialise(r, accept);
	}
	
	
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/config/runId/{runIdentifier}")
	public String getConfig(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		Config c = rd.getConfig(runIdentifier);
		return serialise(c, accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/selfRegister/runId/{runId}")
	public String selfRegisterWithRunId(@HeaderParam("Authorization") String token, 
			@PathParam("runId") Long runId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.selfRegister(runId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public String getRuns(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept)  {
        // TODO
//        if (!validCredentials(token))
//            return serialise(getInvalidCredentialsBean(), accept);
//        RunDelegator rd = new RunDelegator(this);
//        return serialise(rd.getRunsForGame(Long.parseLong(gameIdentifier), account), accept);
        return "{}";
    }

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String runString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.createRun(run), accept);
	}
	
	@PUT
	@Path("/runId/{runIdentifier}")
	public String updateRun(@HeaderParam("Authorization") String token, String runString,
			@PathParam("runIdentifier") Long runIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.updateRun(run, runIdentifier), accept);
	}

	@DELETE
	@Path("/runId/{runIdentifier}")
	public String deleteRun(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("gameIdentifier") String gameIdentifier,
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.deleteRun(runIdentifier), accept);
	}

}
