package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.cache.AccountCache;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.jdo.manager.AccountManager;

import java.util.UUID;

public class AccountDelegator extends GoogleDelegator {

    public AccountDelegator() {
        super();
    }

    public AccountDelegator(String authToken) {
        super(authToken);
    }

    public AccountDelegator(Service service) {
        super(service);
    }

    public AccountDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public Account getAccountInfo(Account myAccount) {
        return AccountManager.getAccount(myAccount.getFullId());

    }

    public Account getContactDetails(String accountId, EnhancedUser user) {
        Account account = getContactDetails(accountId);
        if (account == null && user != null) {
            account = AccountManager.addAccount(user.getLocalId(), user.localId, user.getProvider(), user.getEmail(),user.name, "", user.name,user.picture, false).toAccount();
            if (account != null) {
                AccountCache.getInstance().storeAccountValue(account.getFullId(), account);
            }
        }
        return account;
    }

    public Account getContactDetails(String accountId) {
        Account account = AccountCache.getInstance().getAccount(accountId);
        if (account == null) {
            account = AccountManager.getAccount(accountId);
        }
        return account;
    }

//    public Account createAnonymousContact(Account inContact) {
//        String localID = UUID.randomUUID().toString();
//        return AccountManager.addAccount(localID, 0, inContact.getEmail(), inContact.getGivenName(), inContact.getFamilyName(), inContact.getName(), inContact.getPicture(), false).toAccount();
//    }


    public Account createAccount(String fbId, String localID, int accountType, String email, String displayName, String picture, Long expirationDate, String labels) {
        return AccountManager.overwriteAccount(fbId, localID, accountType,
                email, "", "",
                displayName,
                picture,
                false,
                expirationDate,
                labels

        ).toAccount();
    }

    public void makeSuper(String accountId) {
        AccountManager.makeSuper(accountId);
    }

    public void deleteAccount(String fullid){

        try {
            AccountManager.deleteAccount(fullid);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public Account setExpirationDate(String fullid, Long expirationDate){
        return AccountManager.setExpirationDate(fullid, expirationDate);
    }

    public AccountList query(String query) {
        return AccountManager.query(query);
    }

}
