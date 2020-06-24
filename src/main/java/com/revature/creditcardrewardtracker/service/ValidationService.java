package com.revature.creditcardrewardtracker.service;

import java.util.List;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.Transaction;


public class ValidationService {


	
	//validates that the username is available to be used to prevent unique errors from DB
	//returns false if used, true if new
	public boolean usernameUniqueValidation(String username) {
		IUserRepo ur = new UserRepoDB();
		List<String> usernames = ur.getAllUsers();
		
		for (String user : usernames) {
			if (user.equalsIgnoreCase(username)) {
				System.out.println(username + " has already been used. Please select a different username.");
				return false;
			}
		}
		return true;
	}
	
	public boolean usernameLengthValidation(String username) {
		if(username.length() >= 5 && username.length() <= 25) {
			return true;
		} else {
			System.out.println("Username length is invalid. Please make your username between 5 and 25 characters.");
			return false;
		}
	}
	
	public boolean usernameExistsValidation(String username) {
		IUserRepo ur = new UserRepoDB();
		List<String> usernames = ur.getAllUsers();
		
		for (String user : usernames) {
			if (user.equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean passwordLengthValidation(String password) {
		
		if(password.length() >= 5 && password.length() <= 25) {
			return true;
		} else {
			System.out.println("Password length is invalid. Please make your passowrd between 5 and 25 characters.");
			return false;
		}
	}
	
	//validates that the transaction belongs to the user
	public boolean permissionToModifyTransaction(String username, int transactionId) {
		ITransactionRepo tr = new TransactionRepoDB();
		List<Transaction> transactionsForUser = tr.listTransactions(username);
		
		for (Transaction t : transactionsForUser) {
			if (t.getTransactionId() == transactionId) {
				return true;
			}
		}
		
		System.out.println("Transaction " + transactionId + " is not associated with this account.");
		return false;
	}
	
	//validates that the credit card belongs to the user
	public boolean permissionToModifyCard(String username, int cardId) {
		ICreditCardRepo ccr = new CreditCardRepoDB();
		List<CreditCard> cardsForUser = ccr.getCreditCards(username);
		
		for (CreditCard c : cardsForUser) {
			if (c.getCreditCardID() == cardId) {
				return true;
			}
		}
		
		System.out.println("Card Id " + cardId + " is not assocaited with this account.");
		return false;
	}
	

}
