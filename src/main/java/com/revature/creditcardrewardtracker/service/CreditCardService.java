package com.revature.creditcardrewardtracker.service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CreditCard;

public class CreditCardService {
	
	private String username;
	private Connection connection;
	private ICreditCardRepo d;
	private Scanner sc;
	private ValidationService validation;
	private InputValidationService inputValidation;
	
	public CreditCardService(String username, Connection connection, Scanner sc) {
		this.username = username;
		this.connection = connection;
		this.sc = sc;
		d = new CreditCardRepoDB(connection);
		validation = new ValidationService(connection);
		inputValidation = new InputValidationService(sc);
	}
	
	public CreditCard createNewCreditCard() {
		
		CashbackCategoryService cashback = new CashbackCategoryService(username, connection, sc);
		
		CreditCard card = new CreditCard();
		
		try {
			System.out.println("What is the name of the credit card?");
			String creditCardName = inputValidation.getValidStringInput();
			card.setCreditCardName(creditCardName);
			
			/*System.out.println("What are the last 4 digits of the credit card?");
			int cardID = Integer.parseInt(input.nextLine());
			card.setCreditCardID(cardID);*/
			
			card.setCardCashBackCategories(cashback.createNewCashbackCategory());
			
			card = d.addCreditCard(username, card);
			
			System.out.println(card);
			return card;
		} catch (Exception e) {
			System.out.println("Invalid input type.");
		} 
		
		return card;
		
		
	}
	
	public List<CreditCard> getCreditCards() {
		List<CreditCard> cardsOnFile;
		cardsOnFile = d.getCreditCards(username);
		return cardsOnFile;
	}
	
	public void deleteCreditCard() {
		System.out.println("Please enter the Card ID for the credit card you wish to delete.");
		
		List<CreditCard> cards = d.getCreditCards(username);
		for (CreditCard c : cards) {
			System.out.println(c.stringNameAndId());
		}
		
		int id = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyCard(username, id);
		
		if (belongsToUser == true) {
			boolean result = d.deleteCard(id);
			if (result == true) {
				System.out.println(id + " deleted successfully.");
			} else {
				System.out.println(id + " not deleted.");
			}
		}
	}
	
	
	public void updateCardName() {
		System.out.println("Please enter the Card ID for the credit card you wish to modify.");
		
		List<CreditCard> cards = d.getCreditCards(username);
		for (CreditCard c : cards) {
			System.out.println(c.stringNameAndId());
		}
		
		int id = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyCard(username, id);
		
		if (belongsToUser == true) {
			System.out.println("What would you like to rename this card?");
			String newName = inputValidation.getValidStringInput();
			
			boolean result = d.updateCard(id, newName);
			if (result == true) {
				System.out.println(id + " updated successfully.");
			} else {
				System.out.println(id + " not updated.");
			} 
		}
	}

}
