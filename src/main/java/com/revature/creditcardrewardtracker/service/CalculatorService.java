package com.revature.creditcardrewardtracker.service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CategoryCashBack;
import com.revature.creditcardrewardtracker.models.CreditCard;

public class CalculatorService {
	
	private List<CreditCard> cards;
	private InputValidationService inputValidation;
	private ValidationService validation;
	private String username;
	
	public CalculatorService(Connection connection, String username, Scanner sc) {
		ICreditCardRepo ccr = new CreditCardRepoDB(connection);
		cards = ccr.getCreditCards(username);
		inputValidation = new InputValidationService(sc);
		validation = new ValidationService(connection);
		this.username = username;
	}
	
	public void selectBestCard() {
		
		System.out.println("Please enter the category of the purchase.");
		String category = inputValidation.getValidStringInput();
		
		String bestCard = null;
		double bestRate = 0.00;
		
		for (int i = 0; cards.size() > i; i++) {
			CreditCard tempCard = cards.get(i);
			for (int j = 0; tempCard.getCardCashBackCategories().size() > j; j++) {
				List<CategoryCashBack> tempCategoriesList = tempCard.getCardCashBackCategories();
				if (tempCategoriesList.get(j).getCategoryOfCashBack().equalsIgnoreCase(category)) {
					double tempRate = tempCategoriesList.get(j).getPercentageOfCashBack();
					if (tempRate > bestRate) {
						bestRate = tempRate;
						bestCard = tempCard.getCreditCardName();
					}
				}
			}
		}
		if (bestRate > 0.00) {
			System.out.println("The best card for " + category + " is " + bestCard + " at the rate of " + (bestRate*100) + "%.");
		} else {
			System.out.println("No cards were found with a cashback category of " + category + ".");
		}
		
	}
	
	public void calculatePercentageBack() {
		
		System.out.println("Which of the following cards do you plan on using? Please enter the Card ID.");
		for (CreditCard c : cards) {
			System.out.println(c.stringNameAndId());
		}
		int cardNumber = inputValidation.getValidInt();
		boolean belongsToUser = validation.permissionToModifyCard(username, cardNumber);
		
		if (belongsToUser == true) {
			for (int i = 0; cards.size() > i; i++) {
				CreditCard tempCard = cards.get(i);
				if (tempCard.getCreditCardID() == cardNumber) {
					System.out.println("What is the category of the purchase?");
					String category = inputValidation.getValidStringInput();
					
					double bestRate = 0.00;
					for (int j = 0; tempCard.getCardCashBackCategories().size() > j; j++) {
						List<CategoryCashBack> tempCategoriesList = tempCard.getCardCashBackCategories();
						if (tempCategoriesList.get(j).getCategoryOfCashBack().equalsIgnoreCase(category)) {
							double tempRate = tempCategoriesList.get(j).getPercentageOfCashBack();
							if (tempRate > bestRate) {
								bestRate = tempRate;
							}
						}
					}
					
					System.out.println("How much do you plan on spending?");
					double total = inputValidation.getValidDouble();
					double savings = total*bestRate;
					double adjustedPrice = total - savings;
					
					System.out.println("Using your " + tempCard.getCreditCardName() + " card will save you $" + savings + " for an adjusted total of $" + adjustedPrice + ".");
					
				}
			}
		}
	}

}
