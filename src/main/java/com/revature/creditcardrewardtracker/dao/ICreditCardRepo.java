package com.revature.creditcardrewardtracker.dao;

import java.util.List;

import com.revature.creditcardrewardtracker.models.CreditCard;

public interface ICreditCardRepo {
	
	public void addCreditCard(String username, CreditCard card);
	
	public List<CreditCard> getCreditCards(String username);
	
	public boolean deleteCard(int cardId);
	
	public boolean updateCard(int cardId, String name);

}
