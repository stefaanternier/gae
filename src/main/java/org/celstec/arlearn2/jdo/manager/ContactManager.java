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
package org.celstec.arlearn2.jdo.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;

import com.google.appengine.api.datastore.*;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
//import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.beans.account.Invitation;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.jdo.classes.AccountEntity;
import org.celstec.arlearn2.jdo.classes.ContactEntity;
//import com.google.appengine.datanucleus.query.JDOCursorHelper;

//import com.google.appengine.api.datastore.Cursor;


public class ContactManager {

	private static DatastoreService datastore;
	static {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	private static final int ACCOUNTS_IN_LIST = 10;

	public static ContactEntity addContactInvitation(String localID, int accountType, String email, String from) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
		ContactEntity contact = new ContactEntity();
		contact.setFromAccountType(accountType);
		contact.setFromLocalId(localID);
		contact.setStatus(ContactEntity.PENDING);
		contact.setToEmail(email);
		contact.setUniqueId(UUID.randomUUID().toString());
		contact.setLastModificationDate(System.currentTimeMillis());
		contact.setToLocalId(from);
		Entity contactEntity = contact.toEntity();
		datastore.put(contactEntity);
		return contact;
	}

	public static Account getContactViaId(String addContactToken) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key key = KeyFactory.createKey(ContactEntity.KIND,addContactToken);
		ContactEntity result = null;
		try {
			result = new ContactEntity(datastore.get(key));
		} catch (EntityNotFoundException e) {
			Account account = new Account();
			account.setError("could not retrieve this account");
			return account;
		}
		Account account = new Account();
		account.setLocalId(result.getFromLocalId());
		account.setAccountType(result.getFromAccountType());
		account.setEmail(result.getToEmail());


		return account;
	}
	
	public static Account getContactViaAcountId(String accountId) {
		Key key = KeyFactory.createKey(ContactEntity.KIND,accountId);
		ContactEntity result = null;
		try {
			result = new ContactEntity(datastore.get(key));
		} catch (EntityNotFoundException e) {
			return null;
		}
		Account account = new Account();
		account.setLocalId(result.getFromLocalId());
		account.setAccountType(result.getFromAccountType());
		return account;
	}

	public static void addContact(Account fullAccount, Account targetAccount, String addContactToken) {

			createContact(fullAccount, targetAccount);
			createContact(targetAccount, fullAccount);
			datastore.delete(KeyFactory.createKey(ContactEntity.KIND,addContactToken));
	}
	
	private static void createContact(Account fullAccount, Account targetAccount) {
		ContactEntity contact = new ContactEntity();
		contact.setFromAccountType(fullAccount.getAccountType());
		contact.setFromLocalId(fullAccount.getLocalId());
		contact.setStatus(ContactEntity.ACCEPTED);
		contact.setToAccountType(targetAccount.getAccountType());
		contact.setToLocalId(targetAccount.getLocalId());
		contact.setUniqueId();
		contact.setLastModificationDate(System.currentTimeMillis());
		datastore.put(contact.toEntity());


	}
	
	public static AccountList getContacts(int providerId, String localId, Long from, Long until, String cursorString, AccountDelegator accountDelegator) {
//		System.out.println("fetching from "+myAccount.getFullId()+" "+myAccount.getLocalId()+" "+myAccount.getAccountType()+" "+from+" "+until);
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(ACCOUNTS_IN_LIST);
		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
		Query.CompositeFilter filter;
		if (from == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ContactEntity.COL_FROMACCOUNTTYPE, Query.FilterOperator.EQUAL, providerId),
					new Query.FilterPredicate(ContactEntity.COL_FROMLOCALID, Query.FilterOperator.EQUAL,  localId),
					new Query.FilterPredicate(ContactEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until)
			);
		} else if (until == null) {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ContactEntity.COL_FROMACCOUNTTYPE, Query.FilterOperator.EQUAL, providerId),
					new Query.FilterPredicate(ContactEntity.COL_FROMLOCALID, Query.FilterOperator.EQUAL,  localId),
					new Query.FilterPredicate(ContactEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		} else {
			filter = Query.CompositeFilterOperator.and(
					new Query.FilterPredicate(ContactEntity.COL_FROMACCOUNTTYPE, Query.FilterOperator.EQUAL, providerId),
					new Query.FilterPredicate(ContactEntity.COL_FROMLOCALID, Query.FilterOperator.EQUAL,  localId),
					new Query.FilterPredicate(ContactEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.LESS_THAN_OR_EQUAL, until),
					new Query.FilterPredicate(ContactEntity.COL_LASTMODIFICATIONDATE, Query.FilterOperator.GREATER_THAN_OR_EQUAL, from)
			);
		}
		Query q = new Query(ContactEntity.KIND);
		q.setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);

		AccountList returnList = new AccountList();
		QueryResultList<Entity> results =pq.asQueryResultList(fetchOptions);
		for (Entity result : results) {
			ContactEntity contactEntity = new ContactEntity(result);
//			AccountManager.getAccount(providerId, localId);
			if (contactEntity.getToLocalId() != null) {
				try {
					Account contactDetails = accountDelegator.getContactDetails(contactEntity.getToFullId());
					if ( contactDetails!= null) returnList.addAccount(contactDetails);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("unable to read account "+contactEntity.getToLocalId());
				}
			}
		}
		if (results.size() == ACCOUNTS_IN_LIST) {
			returnList.setResumptionToken(results.getCursor().toWebSafeString());
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;
	}


	public static AccountList pendingInvitationsToEmail(String email) {
		AccountList returnList = new AccountList();

		Query q = new Query(ContactEntity.KIND);//.setKeysOnly();

		q.setFilter(new Query.FilterPredicate(ContactEntity.COL_TOEMAIL, Query.FilterOperator.EQUAL, email));
		for (Entity result : datastore.prepare(q).asIterable()) {
			Invitation acc = new Invitation();

			ContactEntity object = new ContactEntity(result);
			System.out.println(object.getToEmail());
			acc.setAccountType(object.getFromAccountType());
			acc.setLocalId(object.getFromLocalId());
			acc.setTimestamp(object.getLastModificationDate());
			acc.setInvitationId(object.getUniqueId());
			acc.setFromName(object.getToLocalId());
			returnList.addInvitation(acc);
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;
	}

	public static AccountList pendingInvitations(Account myAccount) {
		AccountList returnList = new AccountList();

		Query q = new Query(ContactEntity.KIND);//.setKeysOnly();
		Query.CompositeFilter accountFilter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(ContactEntity.COL_FROMACCOUNTTYPE, Query.FilterOperator.EQUAL, myAccount.getAccountType()),
				new Query.FilterPredicate(ContactEntity.COL_FROMLOCALID, Query.FilterOperator.EQUAL, myAccount.getLocalId()),
				new Query.FilterPredicate(ContactEntity.COL_STATUS, Query.FilterOperator.EQUAL, ContactEntity.PENDING)
		);

		q.setFilter(accountFilter);
		for (Entity result : datastore.prepare(q).asIterable()) {
			Account acc = new Account();


			ContactEntity object = new ContactEntity(result);

			acc.setEmail(object.getToEmail());
			acc.setTimestamp(object.getLastModificationDate());
			acc.setLocalId(object.getUniqueId());
			returnList.addAccount(acc);
		}
		returnList.setServerTime(System.currentTimeMillis());
		return returnList;

//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		AccountList returnList = new AccountList();
//
//		try {
//			Query query =  pm.newQuery(ContactJDO.class,
//					"status == 1 & fromAccountType == "+myAccount.getAccountType()+"  & fromLocalId == '"+myAccount.getLocalId()+"'");
//			List<ContactJDO> results = (List<ContactJDO>) query.execute();
//			Iterator<ContactJDO> it = (results).iterator();
//			int i = 0;
//			while (it.hasNext()) {
//				i++;
//				ContactJDO object = it.next();
//				Account acc = new Account();
//				acc.setEmail(object.getToEmail());
//				acc.setTimestamp(object.getLastModificationDate());
//				acc.setLocalId(object.getUniqueId());
//				returnList.addAccount(acc);
//			}
//			returnList.setServerTime(System.currentTimeMillis());
//
//
//		}finally {
//			pm.close();
//		}
//		return returnList;

	}

	public static void removeInvitation(String invitation) {
		datastore.delete(KeyFactory.createKey(ContactEntity.KIND,invitation));
	}

	public static void removeContact(Integer fromAccountType, String fromLocalId,Integer toAccountType, String toLocalId) {
		Query q = new Query(ContactEntity.KIND).setKeysOnly();
		Query.CompositeFilter accountFilter = Query.CompositeFilterOperator.and(
				new Query.FilterPredicate(ContactEntity.COL_FROMACCOUNTTYPE, Query.FilterOperator.EQUAL, fromAccountType),
				new Query.FilterPredicate(ContactEntity.COL_FROMLOCALID, Query.FilterOperator.EQUAL, fromLocalId),
				new Query.FilterPredicate(ContactEntity.COL_TOACCOUNTTYPE, Query.FilterOperator.EQUAL, toAccountType),
				new Query.FilterPredicate(ContactEntity.COL_TOLOCALID, Query.FilterOperator.EQUAL, toLocalId)
		);

		q.setFilter(accountFilter);
		for (Entity result : datastore.prepare(q).asIterable()) {
			datastore.delete(result.getKey());
		}
	}
}
