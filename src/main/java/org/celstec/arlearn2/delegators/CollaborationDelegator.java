package org.celstec.arlearn2.delegators;

import com.google.appengine.api.utils.SystemProperty;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.celstec.arlearn2.jdo.classes.ContactEntity;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ContactManager;

public class CollaborationDelegator extends GoogleDelegator {

    public CollaborationDelegator(String authtoken) {
        super(authtoken);
    }
    public CollaborationDelegator(EnhancedUser user) {
        super(user);
    }

    public CollaborationDelegator() {
        super();
    }
    public CollaborationDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public Account getContactDetails(String addContactToken) {
        Account myAccount = ContactManager.getContactViaId(addContactToken);
        if (myAccount == null) return null;
        AccountDelegator ad = new AccountDelegator(this);
        myAccount = ad.getAccountInfo(myAccount);
        if (myAccount == null) return null;
        return myAccount;
    }

    public void addContactViaEmail(String toEmail) {
        com.google.apphosting.api.ApiProxy.getCurrentEnvironment();
//		String version = SystemProperty.version.get();
//		String applicationVersion = 
        Account fullAccount = getMyAccount();
        if (fullAccount == null) return;
        ContactEntity jdo = ContactManager.addContactInvitation(fullAccount.getLocalId(), fullAccount.getAccountType(), toEmail, "");

        String msgBody = "<html><body>";
        msgBody += "Hi,<br>";
        msgBody += "<p>";
        msgBody += fullAccount.getName() + " has invited you to become his ARLearn contact";
        msgBody += "</p>";
        msgBody += "<p>";
        msgBody += "Click  <a href=\"http://" + SystemProperty.applicationId.get() + ".appspot.com/contact.html?id=" + jdo.getUniqueId() + "\">here</a> to accept this invitation.";
        msgBody += "</p>";
        msgBody += "</body></html>";

        MailDelegator md;

        System.out.println("sending mail");
        System.out.println("from  " + fullAccount.getEmail());
        System.out.println("name  " + fullAccount.getName());
        System.out.println("to  " + toEmail);
        System.out.println("body  " + msgBody);
        md = new MailDelegator(getAuthToken());
        md.sendMail("no-reply@" + SystemProperty.applicationId.get() + ".appspotmail.com", fullAccount.getName(), toEmail, "Pending contact request", msgBody);


    }

    public void addContactViaEmail(String toEmail, String note, String from) {
        com.google.apphosting.api.ApiProxy.getCurrentEnvironment();
//		String version = SystemProperty.version.get();
//		String applicationVersion =
        Account fullAccount = getMyAccount();
        String name = "Somebody";
        if (fullAccount != null) {
            name = fullAccount.getName();
        }
//        System.out.println("fullaccount "+this.account);
//        System.out.println("fullaccount "+this.account.getLocalId()+ " " + this.account.getAccountType());
        ContactEntity jdo = ContactManager.addContactInvitation(this.account.getLocalId(), this.account.getAccountType(), toEmail, from);

        String msgBody = "<html><body>";
        msgBody += "Hi,<br>";
        msgBody += "<p>";
        msgBody += from + " has invited you to become his contact";
        msgBody += "</p>";
        msgBody += note;
        msgBody += "<p>";
        msgBody += "Click  <a href=\"http://" + SystemProperty.applicationId.get() + ".appspot.com/#/portal/root/pending/\">here</a> to accept this invitation.";
        msgBody += "</p>";
        msgBody += "</body></html>";

        MailDelegator md;

        System.out.println("sending mail");
        System.out.println("from  " + this.account.getEmail());
        System.out.println("name  " + from);
        System.out.println("to  " + toEmail);
        System.out.println("body  " + msgBody);
        md = new MailDelegator(getAuthToken());
        md.sendMail("no-reply@" + SystemProperty.applicationId.get() + ".appspotmail.com", from, toEmail, "Pending contact request", msgBody);


    }

    public String confirmAddContact(String addContactToken) {
        Account fullAccount = getMyAccount();
        if (fullAccount == null) return null;
        Account targetAccount = ContactManager.getContactViaId(addContactToken);
//        System.out.println("fullAccount account is "+ fullAccount.getFullId()+" "+fullAccount.getLocalId()+ " "+fullAccount.getAccountType());
//        System.out.println("target account is "+ targetAccount.getFullId()+" "+targetAccount.getLocalId()+ " "+targetAccount.getAccountType());
        ContactManager.addContact(fullAccount, targetAccount, addContactToken);
        return "{}";
    }

    public void resendInvitation(String addContactToken, String from) {
        Account targetAccount = ContactManager.getContactViaId(addContactToken);
        String toEmail = targetAccount.getEmail();

        String msgBody = "<html><body>";
        msgBody += "Hi,<br>";
        msgBody += "<p>";
        msgBody += from + " is still waiting for you to become his contact";
        msgBody += "Click  <a href=\"http://" + SystemProperty.applicationId.get() + ".appspot.com/#/portal/root/pending/\">here</a> to accept this invitation.";
        msgBody += "</p>";
        msgBody += "</body></html>";

        MailDelegator md;

        System.out.println("sending mail");
        System.out.println("from  " + this.account.getEmail());
        System.out.println("name  " + from);
        System.out.println("to  " + toEmail);
        System.out.println("body  " + msgBody);
        md = new MailDelegator(getAuthToken());
        md.sendMail("no-reply@" + SystemProperty.applicationId.get() + ".appspotmail.com", from, toEmail, "Pending contact request", msgBody);
    }


    public Account getAccountForInvitation(String addContactToken) {
        Account contact =  ContactManager.getContactViaId(addContactToken);
        if (contact.getError() != null) return contact;
        return  AccountManager.getAccount(contact.getFullId());
    }

    public Account getMyAccount() {
        UsersDelegator qu = new UsersDelegator(this);
        Account myAccount = qu.getCurrentUserAccountObject();
        if (myAccount == null) {
            return null;
        }
        return AccountManager.getAccount(myAccount);
    }

    public AccountList getContacts(int providerId, String localId, Long from, Long until, String cursor) {
        UsersDelegator qu = new UsersDelegator(this);
        Account myAccount = qu.getCurrentUserAccountObject();
        return ContactManager.getContacts(providerId, localId, from, until, cursor, new AccountDelegator(this));
    }

    public AccountList pendingInvitations() {
        UsersDelegator qu = new UsersDelegator(this);
//        Account myAccount = qu.getCurrentUserAccountObject();
        return ContactManager.pendingInvitations(this.account);
    }


    public void removeInvitation(String invitation) {
        ContactManager.removeInvitation(invitation);
    }

    public void removeContact(Integer accountType, String localId) {
        Account fullAccount = getMyAccount();
        ContactManager.removeContact(fullAccount.getAccountType(), fullAccount.getLocalId(), accountType, localId);
    }
}
