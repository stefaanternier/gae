package org.celstec.arlearn2.endpoints;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;


import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.endpoints.impl.account.AccountSearchIndex;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.jdo.manager.AccountManager;

import javax.naming.AuthenticationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Api(name = "account")
public class AccountApi extends GenericApi {
    static {
        FileInputStream serviceAccount =
                null;


        try {
            serviceAccount = new FileInputStream("WEB-INF/firebase-pk.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://serious-gaming-platform.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "accountDetails",
            path = "/account/accountDetails"
    )
    public Account getUserEmail(EnhancedUser user) {
        return new AccountDelegator().getContactDetails(user.getProvider() + ":" + user.getLocalId(), user);
    }

    @SuppressWarnings("ResourceParameter")
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "create_account",
            path = "/account/create"
    )
    public Account createAccount(final User user, Account account) throws Exception{
        adminCheck(user);
        EnhancedUser us = (EnhancedUser) user;
        if (AccountManager.queryViaEmail(account.getEmail())!= null){
            throw new Exception("EXCEPTION.USER_EXISTS");
        }
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(account.getEmail())
                .setEmailVerified(false)
                .setPassword(account.getPassword())
                .setDisplayName(account.getName())
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        String fullId = "7:" + userRecord.getUid();
        new AccountSearchIndex(fullId, userRecord.getDisplayName(), account.getLabel(), account.getEmail()).scheduleTask();
        return new AccountDelegator().createAccount(
                userRecord.getUid(),
                userRecord.getUid(), 7, userRecord.getEmail().toLowerCase(),
                userRecord.getDisplayName(), null, -1l, account.getLabel());

    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "updateAccounts",
            path = "/account/updateOnce"
    )
    public Account updateOnce(final User user) throws Exception{
        adminCheck(user);
        AccountDelegator ad = new AccountDelegator();
        AccountList list = ad.query("");
        List<Account> listAc = list.getAccountList();
        for (int i = 0; i < listAc.size(); i++) {
            Account account = listAc.get(i);
            new AccountSearchIndex(account.getFullId(), account.getName(), account.getLabel(), account.getEmail()).scheduleTask();
            if ((account.getFirebaseId() == null || account.getFirebaseId().startsWith("7:")) &&account.getAccountType() == 7) {
                account.setFirebaseId(account.getLocalId());
                System.out.println(account.getFullId());
                ad.createAccount( account.getFirebaseId(),
                        account.getLocalId(), account.getAccountType(), account.getEmail().toLowerCase(),
                        account.getName(), null, account.getExpirationDate()==null?-1l:account.getExpirationDate(), account.getLabel());
            }
        }
        return null;
    }

    @SuppressWarnings("ResourceParameter")
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_account",
            path = "/account/update"
    )
    public Account updateAccount(final User user, Account account) throws Exception{
        adminCheck(user);
        EnhancedUser us = (EnhancedUser) user;
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(account.getFirebaseId());
        request.setDisplayName(account.getName());

        FirebaseAuth.getInstance().updateUser(request);
        new AccountSearchIndex(account.getFullId(), account.getName(), account.getLabel(), account.getEmail()).scheduleTask();

        return new AccountDelegator().createAccount(
                account.getFirebaseId(),
                account.getLocalId(), account.getAccountType(), account.getEmail().toLowerCase(),
                account.getName(), null, account.getExpirationDate(), account.getLabel());
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.DELETE,
            name = "deleteAccount",
            path = "/account/{fullId}"
    )
    public Account deleteAccount(final User user, @Named("fullId") String accountId) throws  Exception{
        adminCheck(user);
        if (accountId.startsWith("7:")) {
            FirebaseAuth.getInstance().deleteUser(accountId.substring(2));
            new AccountSearchIndex(accountId, "", "", true).scheduleTask();
            return AccountManager.deleteAccount(accountId);
        } else {
            Account acc = new AccountDelegator().getContactDetails(accountId);
            String firebaseId = acc.getFirebaseId();
            if (firebaseId!= null) {
                try {
                    FirebaseAuth.getInstance().deleteUser(firebaseId);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            new AccountSearchIndex(acc.getFullId(), "", "", true).scheduleTask();
            return AccountManager.deleteAccount(accountId);
        }
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "accountDetailsWithId",
            path = "/account/{fullId}"
    )
    public Account getFullAccount(EnhancedUser user, @Named("fullId") String accountId) {
        return new AccountDelegator().getContactDetails(accountId, null);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "accountDetailsWithIds",
            path = "/accounts/{fullIds}"
    )
    public AccountList getFullIdAccounts(EnhancedUser user, @Named("fullIds") String accountIds) {
        AccountList returnList = new AccountList();
        List<String> idList = Arrays.asList(accountIds.split(";"));
        for (int i = 0; i < idList.size(); i++) {
            returnList.addAccount(new AccountDelegator().getContactDetails(idList.get(i), null));
        }
        return returnList;
    }



    //todo set account name

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "delete_account",
            path = "/account/deleteMe"
    )
    public void deleteMe(final User user) {
        EnhancedUser us = (EnhancedUser) user;
        new AccountDelegator().deleteAccount(us.createFullId());
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "searchAllUsers",
            path = "/usermgt/accounts/{query}"
    )
    public AccountList searchUsers(EnhancedUser user, @Named("query") String query) {
        AccountList returnList = new AccountList();
        if (user.isAdmin()) {
//            AccountManager.query(query)
            Results<ScoredDocument> results = new AccountSearchIndex().getIndex().search(query);
            for (ScoredDocument document : results) {
                Account account = new Account();
                account.setFullid(document.getId());
                account.setName(document.getFields("displayName").iterator().next().getText());
                account.setLabel(document.getFields("labels").iterator().next().getText());
                account.setEmail(document.getFields("email").iterator().next().getText());
                returnList.addAccount(account);
            }
        }

        return returnList;
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "setExpirationDate",
            path = "/usermgt/accounts/{fullId}/setExpiration/{expiration}"
    )
    public Account setExpirationDate(EnhancedUser user,
                                     @Named("fullId") String fullId,
                                     @Named("expiration") Long date
    ) {
        if (user.isAdmin()) {
            Account account = new AccountDelegator().setExpirationDate(fullId, date);
//            FirebaseOptions options = null;
//            try {
//                options = new FirebaseOptions.Builder()
//                        .setCredentials(GoogleCredentials.getApplicationDefault())
//                        .setDatabaseUrl("https://serious-gaming-platform.firebaseio.com")
//                        .build();
//                FirebaseApp.initializeApp(options);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//
//            FirebaseApp.initializeApp(options);
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("expirationdate", date);
//            try {
//                FirebaseAuth.getInstance().setCustomUserClaims(user.getId(), claims);
//            } catch (FirebaseAuthException e) {
//                e.printStackTrace();
//            }

//            FirebaseAuth.getInstance().setCustomUserClaims(user.getId(), claims);
            if (account == null) {
                throw new InvalidParameterException("no account with id  "+fullId + " exists" );
            } else {
                return account;
            }
        }
        throw new IllegalArgumentException("only admin can do this");

    }

//    static {
//        FileInputStream serviceAccount =
//                null;
//
//
//        try {
//            serviceAccount = new FileInputStream("WEB-INF/firebase-pk.json");
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://serious-gaming-platform.firebaseio.com")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


}
