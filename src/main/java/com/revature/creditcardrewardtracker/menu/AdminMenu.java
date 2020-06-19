package com.revature.creditcardrewardtracker.menu;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.service.AdminService;
import com.revature.creditcardrewardtracker.service.InputValidationService;

public class AdminMenu implements IUserMenu {

	@Override
	public void menu(Scanner sc, String username, Connection connection) {
		InputValidationService inputValidation = new InputValidationService(sc);
		
		listMenuOptions();
		boolean isMenuActive = true;
		int option = inputValidation.getValidInt();
		
		AdminService adminServ = new AdminService(sc, connection);
		
		while (isMenuActive == true) {
			switch (option) {
			case (0) :
				//list usernames
				System.out.println("Printing usernames:");
				System.out.println(adminServ.getAllUsers());
				listMenuOptions();
				break;
			case (1) :
				//reset user password
				adminServ.changeUserPassword();
				listMenuOptions();
				break;
			case (2) :
				//promote user to admin
				adminServ.promoteUserAccount();
				listMenuOptions();
				break;
			case (3) :
				//demote admin to user
				adminServ.demoteUserAccount();
				listMenuOptions();
				break;
			case (4) :
				//delete user account
				adminServ.deleteUserAccount();
				listMenuOptions();
				break;
			case (5) :
				//sign out
				isMenuActive = false;
				LogInMenu logInMenu = new LogInMenu();
				logInMenu.menu(sc, connection);
				break;
			default :
				System.out.println("Invalid entry. Please enter between 0 and 5 to select menu option.");
				listMenuOptions();
			}
			option = inputValidation.getValidInt();
		}
		
		
	}
	
	private static void listMenuOptions() {
		System.out.println("ADMIN MENU");
		System.out.println("[0] List usernames");
		System.out.println("[1] Reset user password");
		System.out.println("[2] Promote user to admin");
		System.out.println("[3] Demote admin to user");
		System.out.println("[4] Delete user account");
		System.out.println("[5] Sign out");
	}

}
