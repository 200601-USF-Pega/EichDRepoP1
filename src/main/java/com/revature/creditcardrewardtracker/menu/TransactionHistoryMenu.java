package com.revature.creditcardrewardtracker.menu;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.service.TransactionService;

public class TransactionHistoryMenu implements IUserMenu {
	
	@Override
	public void menu(Scanner sc, String username, Connection connection) {
		
		/**view transaction history
		launches sub menu that lets you: 
			- view all transaction records
			- pull total spent in a certain category
			- pull total spent for a certain time period
			- pull total spent for a certain credit card
			- pull total rewards earned for certain category 
			- pull total rewards for a certain time period
			- pull total rewards for a certain credit card
		**/
		
		InputValidationService inputValidation = new InputValidationService(sc);
		
		listMenuOptions();
		
		TransactionService service = new TransactionService(username, connection, sc);
		
		int menuOption = inputValidation.getValidInt();
		
		boolean isMenuActive = true;
		
		while (isMenuActive == true) {			
			switch (menuOption) {
			case (0) :
				//view all transaction records
				System.out.println("Pulling All Transaction Records");
				service.printUserTransactions();
				listMenuOptions();
				break;
			case (1) :
				//calc total spent in a certain category
				System.out.println("$" + service.getTotalForCategories());
				listMenuOptions();
				break;
			case (2) :
				//calc total spent on a specific cc
				System.out.println("$" + service.getTotalForCard());
				listMenuOptions();
				break;
			case (3) :
				//calc total spent for date range
				System.out.println("$" + service.getTotalForDateRange());
				listMenuOptions();
				break;
			case (4) :
				// calc total cash back
				System.out.println("Total Cash Back is $" + service.getTotalCashBack());
				listMenuOptions();
				break;
			case (5) :
				//calc total cash back for category
				System.out.println("Total Cash Back for Category is $" + service.getTotalCashBackForCategories());
				listMenuOptions();
				break;
			case (6) :
				//calc total cash back for CC
				System.out.println("Total Cash Back for Card is $" + service.getTotalCashBackForCard());
				listMenuOptions();
				break;
			case (7) :
				//calc total cash back for date range
				System.out.println("Total Cash Back for Date Range is $" + service.getTotalCashBackForDateRange());
				listMenuOptions();
				break;
			case (8) :
				// remove transaction from records
				service.removeTransaction();
				listMenuOptions();
				break;
			case (9) :
				// update transaction in records
				service.updateTransaction();
				listMenuOptions();
				break;
			case (10) :
				isMenuActive = false;
				System.out.println("Returning to Main Menu");
				return;
			default :
				System.out.println("Please enter a menu option from 0 to 10.");
				listMenuOptions();			
			}
			menuOption = inputValidation.getValidInt();
		}
	}
		
	private static void listMenuOptions() {
		System.out.println("TRANSACTION HISTORY MENU");
		System.out.println("[0] View All Transaction Records");
		System.out.println("[1] Calculate Total Spent in a Specific Category");
		System.out.println("[2] Calculate Total Spent on a Specific Credit Card");
		System.out.println("[3] Calculate Total Spent for a Specific Date Range");
		System.out.println("[4] Calculate Total Cash Back");
		System.out.println("[5] Calculate Total Cash Back in a Specific Category");
		System.out.println("[6] Calculate Total Cash Back on a Specific Credit Card");
		System.out.println("[7] Calculate Total Cash Back for a Specific Date Range");
		System.out.println("[8] Remove Transaction from Records");
		System.out.println("[9] Update Transaction from Records");
		System.out.println("[10] Return to Main Menu");
	}


}
