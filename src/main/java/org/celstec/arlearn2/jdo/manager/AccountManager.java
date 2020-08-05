package org.celstec.arlearn2.jdo.manager;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountEntity;
import org.celstec.arlearn2.jdo.classes.GeneralItemEntity;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountManager {

    private static DatastoreService datastore;

    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public static Account addAccount(Account account) {
        boolean allowTrackLocation = false;
        if (account.getAllowTrackLocation() != null) allowTrackLocation = account.getAllowTrackLocation();
        return addAccount(account.getFirebaseId(), account.getLocalId(), account.getAccountType(), account.getEmail(), account.getGivenName(), account.getFamilyName(), account.getName(), account.getPicture(), allowTrackLocation).toAccount();
    }




    public static Account deleteAccount(String accountId) throws Exception{
        Key key = KeyFactory.createKey(AccountEntity.KIND,
                accountId);
        Entity result = datastore.get(key);
        datastore.delete(key);
        return new AccountEntity(result).toAccount();
    }

    public static AccountEntity addAccount(String fbId, String localID, int accountType,
                                           String email, String given_name, String family_name, String name,
                                           String picture, boolean allowTrackLocation) {
        try {
            AccountEntity account = getAccount(accountType, localID);
            account.setFirebaseId(fbId);
            account.setLocalId(localID);
            account.setAccountType(accountType);
            account.setUniqueId();
            account.setEmail(email);
            account.setGiven_name(given_name);
            account.setFamily_name(family_name);
            account.setName(name);
            account.setPicture(picture);
            account.setLastModificationDate(System.currentTimeMillis());
            account.setAllowTrackLocation(allowTrackLocation);
            return account;
        } catch (Exception e) {

        }

        AccountEntity account = new AccountEntity();
        account.setFirebaseId(fbId);
        account.setLocalId(localID);
        account.setAccountType(accountType);
        account.setUniqueId();
        account.setEmail(email);
        account.setGiven_name(given_name);
        account.setFamily_name(family_name);
        account.setName(name);
        account.setPicture(picture);
        account.setLastModificationDate(System.currentTimeMillis());
        account.setAccountLevel(AccountEntity.USER);
        account.setAllowTrackLocation(allowTrackLocation);

        datastore.put(account.toEntity());
        return account;

    }


    public static AccountEntity overwriteAccount(String fbId, String localID, int accountType,
                                                 String email, String given_name, String family_name, String name,
                                                 String picture,
                                                 boolean allowTrackLocation,
                                                 long expirationDate,
                                                 String labels
    ) {
        AccountEntity account = new AccountEntity();
        account.setFirebaseId(fbId);
        account.setLocalId(localID);
        account.setAccountType(accountType);
        account.setUniqueId();
        account.setEmail(email);
        account.setGiven_name(given_name);
        account.setFamily_name(family_name);
        account.setName(name);
        account.setLabels(labels);
        account.setPicture(picture);
        account.setLastModificationDate(System.currentTimeMillis());
        account.setAccountLevel(AccountEntity.USER);
        account.setAllowTrackLocation(allowTrackLocation);
        account.setExpirationDate(expirationDate);
        datastore.put(account.toEntity());
        return account;
    }


    public static Account getAccount(Account myAccount) {
        return (getAccount(myAccount.getAccountType() + ":" + myAccount.getLocalId()));
    }

    public static AccountEntity getAccount(int accountType, String localID)  {
        return getAccountEntity(accountType + ":" + localID);
    }

    public static Account getAccount(String accountId) {
        System.out.println("accountId "+ accountId);
            AccountEntity accountEntity = getAccountEntity(accountId);
        System.out.println("accountId "+ accountEntity);
            if (accountEntity == null) return null;
            return accountEntity.toAccount();

    }

    public static AccountEntity getAccountEntity(String accountId){
        try {
        Key key = KeyFactory.createKey(AccountEntity.KIND,
                accountId);

        Entity result = datastore.get(key);
        return new AccountEntity(result);
        } catch (Exception e) {
            return null;
        }

    }



    public static void makeSuper(String accountId) {
        AccountEntity accountEntity = null;
        try {
            accountEntity = getAccountEntity(accountId);
            accountEntity.setAccountLevel(Account.ADMINISTRATOR);
            datastore.put(accountEntity.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static AccountList query(String query) {
        AccountList itemsResult = new AccountList();
        Query q = new Query(AccountEntity.KIND);
        PreparedQuery pq = datastore.prepare(q);

        List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(250));
        Iterator<Entity> it = results.iterator();
        while (it.hasNext()) {
            itemsResult.addAccount(new AccountEntity(it.next()).toAccount());
        }
        return itemsResult;
    }

    public static Account queryViaEmail(String email) {
        AccountList itemsResult = new AccountList();
        Query q = new Query(AccountEntity.KIND)
                .setFilter(new Query.FilterPredicate(AccountEntity.COL_EMAIL, Query.FilterOperator.EQUAL, email.toLowerCase()));
        PreparedQuery pq = datastore.prepare(q);

        List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(2));
        Iterator<Entity> it = results.iterator();
        if (it.hasNext()) {
            return new AccountEntity(it.next()).toAccount();
        }
        return null;
    }

    public static Account setExpirationDate(String accountId, Long expirationDate) {
        AccountEntity accountEntity = null;
        try {
            accountEntity = getAccountEntity(accountId);
            accountEntity.setExpirationDate(expirationDate);
            datastore.put(accountEntity.toEntity());
            return accountEntity.toAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
