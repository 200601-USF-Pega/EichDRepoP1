package com.revature.creditcardrewardtracker.exceptions;

public class InvalidCreditCardNumberException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() { 
		return "Please enter the last 4 digits of the credit card.";
	}

	
}
