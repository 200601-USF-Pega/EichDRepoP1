package com.revature.creditcardrewardtracker.menu;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.web.CreditCardRewardService;
import com.revature.creditcardrewardtracker.web.CreditCardService;

public class CreditCardMenu implements IUserMenu {

	@Override
	public void menu(Scanner sc, String username, Connection connection) {
		
		InputValidationService inputValidation = new InputValidationService(sc);
		
		listMenuOptions();
		CreditCardService cardService = new CreditCardService(username, connection, sc);
		CreditCardRewardService categoryService = new CreditCardRewardService(username, connection, sc);
		
		int menuOption = inputValidation.getValidInt();
		
		boolean isMenuActive = true;
		
		while (isMenuActive == true) {			
			switch (menuOption) {
			case (0) :
				//Add a new credit card
				cardService.createNewCreditCard();
				listMenuOptions();
				break;
			case (1) :
				//Remove a credit card on file
				cardService.deleteCreditCard();
				listMenuOptions();
				break;
			case (2) :
				//Update a credit card name on file
				cardService.updateCardName();
				listMenuOptions();
				break;
			case (3) :
				//Add a cash back category to a credit card
				categoryService.addCashbackCategory();
				listMenuOptions();
				break;
			case (4) :
				// Remove a cash back category from a credit card
				categoryService.deleteCashbackCategory();
				listMenuOptions();
				break;
			case (5) :
				//Update a cash back category
				categoryService.updateCashbackCategory();
				listMenuOptions();
				break;
			case (6) :
				//List all credit cards
				System.out.println(cardService.getCreditCards());
				System.out.println();
				listMenuOptions();
				break;
			case (7) :
				// list cash back categories for a credit card
				categoryService.printCashBackCategories();
				listMenuOptions();
				break;
			case (8) :
				//returns to main menu
				System.out.println("Returning to Main Menu");
				isMenuActive = false;
				return;
			default :
				System.out.println("Please enter a menu option from 0 to 7.");
				listMenuOptions();
			}
			menuOption = inputValidation.getValidInt();
		}
		
		
	}

	private static void listMenuOptions() {
		System.out.println("CREDIT CARD MENU");
		System.out.println("[0] Add a new credit card");
		System.out.println("[1] Remove a credit card on file");
		System.out.println("[2] Update the name of a credit card on file");
		System.out.println("[3] Add a cash back category to a credit card");
		System.out.println("[4] Remove a cash back category from a credit card");
		System.out.println("[5] Update a cash back category");
		System.out.println("[6] List all credit cards");
		System.out.println("[7] List all cash back categories for a card");
		System.out.println("[8] Return to main menu");
	}
}
