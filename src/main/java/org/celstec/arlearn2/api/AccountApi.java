package org.celstec.arlearn2.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.AccountEntity;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationEntity;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ContactManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;

import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Logger;

@Path("/account")
public class AccountApi extends Service {
	
//	@POST
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/createAnonymousContact")
//	public String createAnonymContact(@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			String contact
//			)  {
////		if (!validCredentials(token))
////			return serialise(getInvalidCredentialsBean(), accept);
//		Object inContact = deserialise(contact, Account.class, contentType);
//		if (inContact instanceof java.lang.String)
//			return serialise(getBeanDoesNotParseException((String) inContact), accept);
//		AccountDelegator ad = new AccountDelegator(this);
//		return serialise(ad.createAnonymousContact((Account) inContact), accept);
//	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/test")
	public String createAnonymContact(){
		System.out.println("about to manually create some contacts");
//		AccountManager.addAccount("id1", 1, "email@test.com", "Name 1", "Ternier", " st 1 tern", "", true);
//		Account acc1 = new Account();
//		acc1.setLocalId("116743449349920850150");
//		acc1.setAccountType(2);
//		Account acc2 = new Account();
//		acc2.setLocalId("id1");
//		acc2.setAccountType(1);
//
//		ContactManager.addContact(acc1,acc2, null);
		return "oik";

	}

//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/createAnonymousContact/{email}/{name}")
//	public String createAnonymContact(@HeaderParam("Authorization") String token,
//									  @PathParam("email") String email,
//									  @PathParam("name") String name,
//									  @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
//									  @DefaultValue("application/json") @HeaderParam("Accept") String accept,
//									  String contact
//	)  {
//		Account inContact = new Account();
//		inContact.setPicture("");
//		inContact.setEmail(email);
//		inContact.setName(name);
//		AccountDelegator ad = new AccountDelegator(this);
//		return serialise(ad.createAnonymousContact((Account) inContact), accept);
//	}

//	@POST
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/createAnonymousContact")
//	public String createAnonymContactPOST(@HeaderParam("Authorization") String token,
//									  @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
//									  @DefaultValue("application/json") @HeaderParam("Accept") String accept,
//									  String contact
//	)  {
//		Object inContact = deserialise(contact, Account.class, contentType);
//		if (inContact instanceof java.lang.String)
//			return serialise(getBeanDoesNotParseException((String) inContact), accept);
//
//		if (((Account)inContact).getPicture()== null) ((Account)inContact).setPicture("");
//
//		AccountDelegator ad = new AccountDelegator(this);
//		return serialise(ad.createAnonymousContact((Account) inContact), accept);
//	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/anonymousLogin/{anonToken}")
	public String anonymousLogin( 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("anonToken") String anonToken
			) {
		Account account = AccountManager.getAccount("0:"+anonToken);
		AuthResponse ar = new AuthResponse();

		if (account != null && account.getError() == null) {
			ar.setAuth(UUID.randomUUID().toString());
			UserLoggedInManager.submitOauthUser("0:"+anonToken, ar.getAuth());		
			return serialise(ar, accept);
		}
		
		ar.setError("Authentication failed");
		return serialise(ar, accept);

	}
	
//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/accountDetails")
//	public String getContactDetails(@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept
//			) {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//
//		AccountDelegator ad = new AccountDelegator(this);
//		return serialise(ad.getContactDetails(this.account.getFullId(), null), accept);
//	}
	
//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/accountDetails/{accountFullId}")
//	public String getContactDetailsForId(@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@PathParam("accountFullId") String accountFullId
//
//			) {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//
//		AccountDelegator ad = new AccountDelegator(this);
//		String myAccount = UserLoggedInManager.getUser(token);
//		if (myAccount == null) {
//			Account ac = new Account();
//			ac.setError("account is not logged in");
//		}
//		return serialise(ad.getContactDetails(accountFullId, null), accept);
//	}
	
//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/makesuper/{accountFullId}")
//	public String makesuper(@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@PathParam("accountFullId") String accountFullId
//
//			) {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//
//		AccountDelegator ad = new AccountDelegator(this);
//		String myAccount = UserLoggedInManager.getUser(token);
//		if (myAccount == null) {
//			Account ac = new Account();
//			ac.setError("account is not logged in");
//		}
//		ad.makeSuper(accountFullId);
//		return "{}";
//	}

    private static final Logger log = Logger.getLogger(AccountApi.class.getName());

	//creating an account by external server (e.g. wespot) is no longer supported
//    @POST
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    @CacheControlHeader("no-cache")
//    @Path("/createAccount")
//    public String createAccount(@HeaderParam("Authorization") String token,
//                                      @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
//                                      @DefaultValue("application/json") @HeaderParam("Accept") String accept,
//                                      String contact
//    ) {
//
//        Object inContact = deserialise(contact, Account.class, contentType);
//        log.log(Level.SEVERE, "registering account"+contact);
//
//        if (inContact instanceof java.lang.String)
//            return serialise(getBeanDoesNotParseException((String) inContact), accept);
//        AccountDelegator ad = new AccountDelegator(this);
//        return serialise(ad.createAccount((Account) inContact, token), accept);
//    }


	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/verifyToken")
	public String verify(
	        String authObject
			) throws Exception {

        System.out.println(authObject);
        return "no longer in use";
//        String serverAuthCode = authObject.substring(authObject.indexOf("=")+1);
//        System.out.println(serverAuthCode);
//        GoogleTokenResponse tokenResponse =
//                new GoogleAuthorizationCodeTokenRequest(
//                        new NetHttpTransport(),
//                        JacksonFactory.getDefaultInstance(),
//                        "https://www.googleapis.com/oauth2/v4/token",
//                        client_id,
//                        client_secret,
//                        serverAuthCode,
//                        redirect_uri)  // Specify the same redirect URI that you use with your web
//                        // app. If you don't have a web version of your app, you can
//                        // specify an empty string.
//                        .execute();
//
//        String accessToken = tokenResponse.getAccessToken();
//        saveAccount(accessToken);
//        JSONObject result = new JSONObject();
//        result.put("accessToken", accessToken);
//        return result.toString();

//		NetHttpTransport transport = new NetHttpTransport();
//		GsonFactory jsonFactory = new GsonFactory();
//		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//				.setAudience(Collections.singletonList(clientid))
//				// Or, if multiple clients access the backend:
//				//.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//                .setIssuer("https://accounts.google.com")
//				.build();
//
//// (Receive idTokenString by HTTPS POST)
//
//		GoogleIdToken idToken = verifier.verify(token);
//		if (idToken != null) {
//			Payload payload = idToken.getPayload();
//
//			// Print user identifier
//			String userId = payload.getSubject();
//			System.out.println("User ID: " + userId);
//
//			// Get profile information from payload
//			String email = payload.getEmail();
//			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//			String name = (String) payload.get("name");
//			String pictureUrl = (String) payload.get("picture");
//			String locale = (String) payload.get("locale");
//			String familyName = (String) payload.get("family_name");
//			String givenName = (String) payload.get("given_name");
//
//			// Use or store profile information
//			// ...
//
//		} else {
//			System.out.println("Invalid ID token.");
//			return "{'invalidtoken':'true', 'client_id':'"+clientid+"', 'token':'"+token+"'}";
//		}
		//return "{}";
	}

//    public void saveAccount(String accessToken) {
//        try {
//            JSONObject profileJson = new JSONObject(readURL(new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken)));
//            String id = "";
//            String picture = "";
//            String email = "";
//            String given_name = "";
//            String family_name = "";
//            String name = "";
//            if (profileJson.has("picture")) picture = profileJson.getString("picture");
//            if (profileJson.has("id")) id = profileJson.getString("id");
//            if (profileJson.has("email")) email =  profileJson.getString("email");
//            if (profileJson.has("given_name")) given_name = profileJson.getString("given_name");
//            if (profileJson.has("family_name")) family_name = profileJson.getString("family_name");
//            if (profileJson.has("name")) name = profileJson.getString("name");
//			AccountEntity account = AccountManager.addAccount(id, AccountEntity.GOOGLECLIENT, email, given_name, family_name,name, picture, false);
//            if (accessToken != null) {
//                UserLoggedInManager.submitOauthUser(account.getUniqueId(), accessToken);
//            }
//        } catch (Throwable ex) {
//            throw new RuntimeException("failed login", ex);
//        }
//    }
    protected String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }

	private static  String client_id;
	private static  String redirect_uri;
	private static  String client_secret;


	static {
		OauthConfigurationEntity jdo = OauthKeyManager.getConfigurationObject(AccountEntity.GOOGLECLIENT);
		client_id = jdo.getClient_id();
		redirect_uri = jdo.getRedirect_uri();
		client_secret = jdo.getClient_secret();
	}
}
