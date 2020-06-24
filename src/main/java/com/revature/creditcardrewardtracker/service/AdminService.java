package com.revature.creditcardrewardtracker.service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;

public class AdminService {
	
	private Scanner sc;	
	private IUserRepo d;
	private InputValidationService inputValidation;
	private ValidationService validation;
	
	public AdminService(Scanner sc) {
		this.sc = sc;
		d = new UserRepoDB();
		inputValidation = new InputValidationService(sc);
		validation = new ValidationService();
	}
	
	public List<String> getAllUsers() {
		List<String> results;
		results = d.getAllUsers();
		return results;
	}
	
	public void changeUserPassword() {
		System.out.println("Please enter the username associated with the account to reset password.");
		String username = inputValidation.getValidStringInput();
		boolean usernameExists = validation.usernameExistsValidation(username);
	
		if (usernameExists == true) {
			System.out.println("Please enter new password for user.");
			String newPassword = inputValidation.getValidStringInput();
			boolean isValidPassword = validation.passwordLengthValidation(newPassword);
			
			if (isValidPassword == true) {
				boolean result = d.changePassword(username, newPassword);
				if (result == true) {
					System.out.println(username + " password's updated.");
				} else {
					System.out.println(username + " password not changed.");
				}
			}
		} else {
			System.out.println("User " + username + " not found.");
		}
	}
	
	public void deleteUserAccount() {
		System.out.println("Please enter the username associated with the account to be deleted.");
		String username = inputValidation.getValidStringInput();
		boolean usernameExists = validation.usernameExistsValidation(username);
		
		if (usernameExists == true) {
			boolean result = false;
			System.out.println("Enter YES to verify account deletion for " + username);
			if (sc.next().equalsIgnoreCase("yes")) {
				result = d.deleteUser(username);
			}
			
			if (result == true ) {
				System.out.println(username + "'s account deleted.");
			} else {
				System.out.println(username + "'s account not deleted.");
			}
		} else {
			System.out.println("User " + username + " not found.");
		}
	}
	
	public void promoteUserAccount() {
		System.out.println("Please enter the username associated with the account to be promoted to admin.");
		String username = inputValidation.getValidStringInput();
		boolean usernameExists = validation.usernameExistsValidation(username);
		
		if(usernameExists == true) {
			boolean result = d.promoteAdmin(username);
			if (result == true) {
				System.out.println(username + " promoted to admin successfully.");
			} else {
				System.out.println(username + " not promoted to admin.");
			}
		}else {
			System.out.println("User " + username + " not found.");
		}
	}
	
	public void demoteUserAccount() {
		System.out.println("Please enter the username associated with the account to be demoted to user.");
		String username = inputValidation.getValidStringInput();
		boolean usernameExists = validation.usernameExistsValidation(username);
		
		if (usernameExists == true) {
			boolean result = d.demoteAdmin(username);
			if (result == true) {
				System.out.println(username + " demoted to standard account successfully.");
			} else {
				System.out.println(username + " not demoted to standard account.");
			}
		} else {
			System.out.println("User " + username + " not found.");
		}
	}

}
