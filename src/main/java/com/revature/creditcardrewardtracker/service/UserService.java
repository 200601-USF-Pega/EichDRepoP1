package com.revature.creditcardrewardtracker.service;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.User;

public class UserService {
	
	private Connection connection;
	private ValidationService validation;
	private InputValidationService inputValidation;
	
	public UserService(Connection connection, Scanner sc) {
		this.connection = connection;
		validation = new ValidationService(connection);
		inputValidation = new InputValidationService(sc);
	}
	
	public String createNewUser() {
		User user = new User();
		
		boolean hasUsername = false;
		
		while (hasUsername == false) {
			System.out.println("Please enter a username.");
			System.out.println("Usernames must be between 5 and 25 characters and are not case sensitive.");
			String username = inputValidation.getValidStringInput();
			if (validation.usernameLengthValidation(username) == true) {
				if (validation.usernameUniqueValidation(username) == true) {
					user.setUsername(username);
					hasUsername = true;
				}
			}
		}
		
		boolean hasPassword = false;
		
		while (hasPassword == false) {
			System.out.println("Please enter a password.");
			String password = inputValidation.getValidStringInput();
			
			System.out.println("Please re-enter your password.");
			String passwordVerify = inputValidation.getValidStringInput();
			
			if (password.equals(passwordVerify)) {
				if(validation.passwordLengthValidation(password) == true) {
					hasPassword = true;
					user.setPassword(password);
				}
			} else {
				System.out.println("Passwords do not match.");
			}
		}
		
			user.setAdmin(false);

			IUserRepo d = new UserRepoDB(connection);
			user = d.addUser(user);
			
			System.out.println("User " + user.getUsername() + " successfully created.");
			return user.getUsername();
		
	}
	
	public void addCardToUser(User user, CreditCard card) {
		user.addCardsToFile(card);
	}

}
