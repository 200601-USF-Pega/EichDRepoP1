package com.revature.creditcardrewardtracker.exceptions;

public class InvalidNameException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() { 
		return "Credit card name can only contain letters.";
	}

}
