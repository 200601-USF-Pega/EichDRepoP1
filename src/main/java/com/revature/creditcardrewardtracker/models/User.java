package com.revature.creditcardrewardtracker.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private String username;
	private String password;
	private List<CreditCard> cardsOnFile;
	private boolean isAdmin;
	
	public User() {
		cardsOnFile = new ArrayList<CreditCard>();
		isAdmin = false;
	}
	
	public User(String username, String password) {
		this();
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CreditCard> getCardsOnFile() {
		return cardsOnFile;
	}

	public void addCardsToFile(CreditCard creditCard) {
		cardsOnFile.add(creditCard);
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


}
