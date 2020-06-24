package com.revature.creditcardrewardtracker.models;

import java.util.List;

import com.revature.creditcardrewardtracker.exceptions.InvalidNameException;

public class CreditCard  {
	
	//the credit card object contains the unique ID, name, and an ArrayList of CategoryCashBack objects
	
	private String creditCardName;
	private int creditCardID;
	private List<CreditCardReward> cardCashBackCategories;
	
	public CreditCard() {
		
	}
	
	public CreditCard(String creditCardName, int creditCardID) {
		this.setCreditCardName(creditCardName);
		this.setCreditCardID(creditCardID);
	}

	public String getCreditCardName() {
		return creditCardName;
	}

	public void setCreditCardName(String creditCardName) {
		char[] chars = creditCardName.toCharArray();
		for (char c : chars) {
			if(Character.isDigit(c)) {
				throw new InvalidNameException();
			}
		}
		this.creditCardName = creditCardName;
	}

	public int getCreditCardID() {
		return creditCardID;
	}

	public void setCreditCardID(int creditCardID) {
		//needs to check the database that no other card has same ID}
		this.creditCardID = creditCardID;
	}

	public List<CreditCardReward> getCardCashBackCategories() {
		return cardCashBackCategories;
	}

	public void setCardCashBackCategories(List<CreditCardReward> cardCashBackCategories) {
		this.cardCashBackCategories = cardCashBackCategories;
	}

	@Override
	public String toString() {
		return '\n' + creditCardName + ", Card ID = " +  creditCardID + cardCashBackCategories;
	}
	
	public String stringNameAndId() {
		return creditCardName + " - Card ID: " + creditCardID;
	}

}
