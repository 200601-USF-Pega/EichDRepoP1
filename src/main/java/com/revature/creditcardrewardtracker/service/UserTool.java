package com.revature.creditcardrewardtracker.service;

import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.User;

public class UserTool {
	private ValidationService validation;

	public UserTool() {
		validation = new ValidationService();
	}

	public User createNewUser(String username, String password, String passwordVerify) {
		User user = new User();

		if (validation.usernameLengthValidation(username) == true) {
			if (validation.usernameUniqueValidation(username) == true) {
				user.setUsername(username);
			}
		}

		if (password.equals(passwordVerify)) {
			if (validation.passwordLengthValidation(password) == true) {
				user.setPassword(password);
			}
		}

		user.setAdmin(false);
		System.out.println("User " + user.getUsername() + " successfully created.");
		return user;

	}

	public void addCardToUser(User user, CreditCard card) {
		user.addCardsToFile(card);
	}

}
