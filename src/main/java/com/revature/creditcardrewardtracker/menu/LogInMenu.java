package com.revature.creditcardrewardtracker.menu;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.service.LogInService;
import com.revature.creditcardrewardtracker.service.UserService;

public class LogInMenu implements IMenu {
	
	
	public void menu(Scanner sc, Connection connection) {
		
		InputValidationService inputValidation = new InputValidationService(sc);
		
		System.out.println("Welcome to the Credit Card Reward Calculator and Tracker!");
		listMenuOptions();
		
		//Scanner sc = new Scanner(System.in);
		
		int option = inputValidation.getValidInt();
		boolean isMenuActive = true;
		
		while (isMenuActive == true) {
			switch (option) {
			//log in
			case (1) :
				LogInService login = new LogInService(connection, sc);
	
				System.out.println("Please enter your username:");
				String username = inputValidation.getValidStringInput();
				
				System.out.println("Please enter your password:");
				String password = inputValidation.getValidStringInput();
				
				String user = login.logInVerification(username, password);
				
				if (user == null) {
					listMenuOptions();
					break;
				} else {
					user = user.toLowerCase();
					AdminMenuFactory menuFactory = new AdminMenuFactory();
					IUserMenu menu = menuFactory.getMenu(login.adminVerification(username));
					menu.menu(sc, user, connection);
					isMenuActive = false;
				}
				break;
			// create a new account
			case (2) :
				UserService creation = new UserService(connection, sc);
				String newUsername = creation.createNewUser();
				MainMenu newUserMenu = new MainMenu();
				newUsername = newUsername.toLowerCase();
				newUserMenu.menu(sc, newUsername, connection);
				isMenuActive = false;
				break;
			case (3) :
				System.out.println("Goodbye");
				isMenuActive = false;
				//sc.close();
				System.exit(0);
				return;
			default :
				System.out.println("Please enter 1 to log in or 2 to create a new account.");
				listMenuOptions();
				option = inputValidation.getValidInt();
				break;
			}
			option = inputValidation.getValidInt();
		}	

	}
	
	private static void listMenuOptions() {
		System.out.println("To continue, please log in or create a new account.");
		System.out.println("To log in, please enter 1.");
		System.out.println("To create a new account, please enter 2.");
		System.out.println("To exit the application, please enter 3.");
	}
	

}
